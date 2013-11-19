package org.motechproject.admin.security.service;

import java.util.List;

import org.motechproject.admin.security.domain.MotechSecurityConfiguration;
import org.motechproject.admin.security.domain.MotechURLSecurityRule;
import org.motechproject.admin.security.repository.AllMotechSecurityRules;
import org.motechproject.admin.security.domain.MotechSecurityConfiguration;
import org.motechproject.admin.security.domain.MotechURLSecurityRule;
import org.motechproject.admin.security.repository.AllMotechSecurityRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("motechURLSecurityService")
public class MotechURLSecurityServiceImpl implements MotechURLSecurityService {

    @Autowired
    private AllMotechSecurityRules allSecurityRules;

    @Autowired
    private MotechProxyManager proxyManager;

    @Override
    public List<MotechURLSecurityRule> findAllSecurityRules() {
        return allSecurityRules.getRules();
    }

    @Override
    public void updateSecurityConfiguration(MotechSecurityConfiguration configuration) {
        allSecurityRules.addOrUpdate(configuration);
        proxyManager.rebuildProxyChain();
    }
}
