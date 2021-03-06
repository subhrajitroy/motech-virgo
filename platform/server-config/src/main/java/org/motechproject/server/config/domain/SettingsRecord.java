package org.motechproject.server.config.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.commons.couchdb.model.MotechBaseDataObject;
import org.motechproject.commons.date.util.DateUtil;

import java.io.IOException;
import java.security.DigestInputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Class for storing settings values
 */

@TypeDiscriminator("doc.type === 'SettingsRecord'")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "couchDbProperties" })
public class SettingsRecord extends MotechBaseDataObject implements MotechSettings {

    private static final long serialVersionUID = -3397067324750312884L;

    private Properties platformSettings = new Properties();

    private boolean platformInitialized;
    private DateTime lastRun;
    private byte[] configFileChecksum = new byte[0];
    private String filePath;

    @JsonIgnore
    @Override
    public String getLanguage() {
        return platformSettings.getProperty(LANGUAGE);
    }

    @JsonIgnore
    @Override
    public String getStatusMsgTimeout() {
        return platformSettings.getProperty(STATUS_MSG_TIMEOUT);
    }

    @JsonIgnore
    @Override
    public Properties getActivemqProperties() {
        Properties activemqProperties = new Properties();
        for (String key : platformSettings.stringPropertyNames()) {
            if (key.startsWith("jms.")) {
                activemqProperties.put(key, platformSettings.getProperty(key));
            }
        }
        return activemqProperties;
    }

    @JsonIgnore
    @Override
    public LoginMode getLoginMode() {
        return LoginMode.valueOf(platformSettings.getProperty(LOGINMODE));
    }

    @JsonIgnore
    public String getLoginModeValue() {
        return platformSettings.getProperty(LOGINMODE);
    }

    @JsonIgnore
    @Override
    public String getProviderName() {
        return platformSettings.getProperty(PROVIDER_NAME);
    }

    @JsonIgnore
    @Override
    public String getProviderUrl() {
        return platformSettings.getProperty(PROVIDER_URL);
    }

    @JsonIgnore
    @Override
    public String getServerUrl() {
        return new MotechURL(platformSettings.getProperty(SERVER_URL)).toString();
    }

    @JsonIgnore
    @Override
    public String getServerHost() {
        return new MotechURL(platformSettings.getProperty(SERVER_URL)).getHost();
    }

    @Override
    public boolean isPlatformInitialized() {
        return platformInitialized;
    }

    @Override
    public void setPlatformInitialized(boolean platformInitialized) {
        this.platformInitialized = platformInitialized;
    }

    @JsonIgnore
    @Override
    public String getUploadSize() {
        return platformSettings.getProperty(UPLOAD_SIZE);
    }

    @Override
    public void setLanguage(final String language) {
        platformSettings.setProperty(LANGUAGE, language);
    }

    @Override
    public void setLoginModeValue(String loginMode) {
        platformSettings.setProperty(LOGINMODE, loginMode);
    }

    @Override
    public void setProviderName(String providerName) {
        platformSettings.setProperty(PROVIDER_NAME, providerName);
    }

    @Override
    public void setProviderUrl(String providerUrl) {
        platformSettings.setProperty(PROVIDER_URL, providerUrl);
    }

    @Override
    public void setStatusMsgTimeout(final String statusMsgTimeout) {
        platformSettings.setProperty(STATUS_MSG_TIMEOUT, statusMsgTimeout);
    }

    @Override
    public DateTime getLastRun() {
        return DateUtil.setTimeZoneUTC(lastRun);
    }

    @Override
    public void setLastRun(final DateTime lastRun) {
        this.lastRun = lastRun;
    }

    @Override
    public void setServerUrl(String serverUrl) {
        platformSettings.setProperty(SERVER_URL, serverUrl);
    }

    @Override
    public void setUploadSize(String uploadSize) {
        platformSettings.setProperty(UPLOAD_SIZE, uploadSize);
    }

    @Override
    public byte[] getConfigFileChecksum() {
        return Arrays.copyOf(configFileChecksum, configFileChecksum.length);
    }

    @Override
    public void setConfigFileChecksum(final byte[] configFileChecksum) {
        this.configFileChecksum = Arrays.copyOf(configFileChecksum, configFileChecksum.length);
    }

    @Override
    public void updateFromProperties(final Properties props) {
        platformSettings.putAll(props);
    }

    @Override
    public void savePlatformSetting(String key, String value) {
        platformSettings.put(key, value);
    }

    @Override
    public synchronized void load(DigestInputStream dis) throws IOException {
        platformSettings.load(dis);
    }

    @Override
    public Properties getPlatformSettings() {
        return platformSettings;
    }

    @Override
    public void updateSettings(SettingsRecord settingsRecord) {
        if (settingsRecord != null) {
            this.configFileChecksum = settingsRecord.getConfigFileChecksum();
            this.filePath = settingsRecord.getFilePath();
            updateFromProperties(settingsRecord.getPlatformSettings());
        }
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void mergeWithDefaults(Properties defaultConfig) {
        for (Map.Entry<Object, Object> entry : defaultConfig.entrySet()) {
            String key = (String) entry.getKey();
            Object value = entry.getValue();

            if (platformSettings.getProperty(key) == null) {
                platformSettings.put(key, value);
            }
        }
    }

    public void removeDefaults(Properties defaultConfig) {
        for (Map.Entry<Object, Object> entry : defaultConfig.entrySet()) {
            String key = (String) entry.getKey();
            Object defaultValue = entry.getValue();

            Object currentVal = platformSettings.getProperty(key);

            if (currentVal != null && Objects.equals(currentVal, defaultValue)) {
                platformSettings.remove(key);
            }
        }
    }
}
