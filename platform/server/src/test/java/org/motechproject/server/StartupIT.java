package org.motechproject.server;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;
import org.motechproject.testing.utils.TestContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class StartupIT {

    private String HOST = "localhost";
    private int PORT = TestContext.getTomcatPort();

    @Test
    public void shouldStartServerAndMakeAllBundlesActive() throws IOException, JSONException, InterruptedException{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.setCookieStore(new BasicCookieStore());
        login(httpClient);
        JSONArray bundles = null;
        int retryCount = 8;
        do {
            try {
                bundles = getBundleStatusFromServer(httpClient);
                if (!checkIfBundlesStillStarting(bundles)) {
                    break;
                }
            } catch (org.apache.http.client.HttpResponseException e) {
                if (!e.getMessage().contains("Not Found")) {
                    throw e;
                }
            }
            Thread.sleep(45 * 1000L);   //wait before next retry
        } while (--retryCount > 0);

        if (retryCount == 0) {
            fail("Failed to start bundles (TIMEOUT)");
        }

        for (int i = 0; i < bundles.length(); i++) {
            final String symbolicName = bundles.getJSONObject(i).getString("symbolicName");
            final String status = bundles.getJSONObject(i).getString("state");

            if (symbolicName.startsWith("org.motechproject.motech")) {
                assertTrue(symbolicName + " not active after server startup.[" + status + "]", "ACTIVE".equals(status));
            }
        }
    }

    private boolean checkIfBundlesStillStarting(JSONArray bundles) throws JSONException {
        for (int i = 0; i < bundles.length(); i++) {
            final String status = bundles.getJSONObject(i).getString("state");
            if ("STARTING".equals(status)) {
                System.out.println("Bundle " + bundles.getJSONObject(i).getString("symbolicName") + " is in " + status + " status.");
                return true;
            }
        }
        return false;
    }

    private JSONArray getBundleStatusFromServer(DefaultHttpClient httpClient) throws IOException, JSONException {
        JSONArray bundles;
        login(httpClient); /* BugCard #208 remove this once we fix web authentication issue, currently
         till security modules started in osgi env there is not authentication for admin console. */
        String response = httpClient.execute(new HttpGet(String.format("http://%s:%d/motech-platform-server/module/admin/api/bundles",
                HOST, PORT)), new BasicResponseHandler());

        bundles = new JSONArray(response);
        return bundles;
    }

    private void login(DefaultHttpClient defaultHttpClient) throws IOException {
        final HttpPost loginPost = new HttpPost(String.format(
                "http://%s:%d/motech-platform-server/module/server/motech-platform-server/j_spring_security_check", HOST, PORT));

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("j_username", "motech"));
        nvps.add(new BasicNameValuePair("j_password", "motech"));

        loginPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF8"));

        final HttpResponse response = defaultHttpClient.execute(loginPost);
        EntityUtils.consume(response.getEntity());
    }
}
