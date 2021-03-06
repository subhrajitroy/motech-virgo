package org.motechproject.config.domain;

import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.motechproject.config.MotechConfigurationException;

import static org.junit.Assert.assertThat;

public class BootstrapConfigTest {
    private static final String dbUrl = "http://www.mydb.com:5984";

    @Test(expected = MotechConfigurationException.class)
    public void shouldThrowExceptionIfDbConfigIsNull() {
        new BootstrapConfig(null, "tenantId", ConfigSource.FILE);
    }

    @Test
    public void shouldUseDefaultIfTenantIdIsNull() {
        BootstrapConfig config = new BootstrapConfig(new DBConfig(dbUrl, null, null), null, ConfigSource.FILE);
        assertThat(config.getTenantId(), IsEqual.equalTo("DEFAULT"));
    }

    @Test
    public void shouldUseDefaultIfTenantIdIsBlank() {
        BootstrapConfig config = new BootstrapConfig(new DBConfig(dbUrl, null, null), " ", ConfigSource.FILE);
        assertThat(config.getTenantId(), IsEqual.equalTo("DEFAULT"));
    }

    @Test
    public void shouldUseDefaultIfConfigSourceIsNull() {
        BootstrapConfig config = new BootstrapConfig(new DBConfig(dbUrl, null, null), "tenantId", null);
        assertThat(config.getConfigSource(), IsEqual.equalTo(ConfigSource.UI));
    }
}
