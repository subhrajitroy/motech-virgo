package org.motechproject.admin.server.ui.impl;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.WordUtils;
import org.eclipse.gemini.blueprint.context.BundleContextAware;
import org.motechproject.admin.security.service.MotechUserService;
import org.motechproject.admin.server.ui.LocaleService;
import org.motechproject.osgi.web.ModuleRegistrationData;
import org.motechproject.osgi.web.UIFrameworkService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Implementation of the <code>LocaleService</code> interface. Uses the user service for operations
 * related to user language setting/retrieval. It also falls back to a cookie locale resolver for not
 * not logged in users(also users without a language set. It retrieves messages by loading them from registered
 * modules.
 */
@Service
public class LocaleServiceImpl implements LocaleService, BundleContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(LocaleServiceImpl.class);

    private static final String[] I18N_RESOURCES_PATHS = {"resources/messages"};

    @Autowired
    private BundleContext bundleContext;

    @Autowired
    private MotechUserService userService;

    @Autowired
    private CookieLocaleResolver cookieLocaleResolver;

    @Autowired
    private UIFrameworkService uiFrameworkService;

    @Override
    public Locale getUserLocale(final HttpServletRequest request) {
        if (request.getUserPrincipal() == null) {
            return cookieLocaleResolver.resolveLocale(request);
        }

        Locale locale = userService.getLocale(request.getUserPrincipal().getName());

        return (locale == null) ? cookieLocaleResolver.resolveLocale(request) : locale;
    }

    @Override
    public void setUserLocale(final HttpServletRequest request,
                              final HttpServletResponse response, final Locale locale) {
        SecurityContext context = (SecurityContext) request.getSession()
                .getAttribute("SPRING_SECURITY_CONTEXT");

        if (context != null) {
            Authentication authentication = context.getAuthentication();
            User userInSession = (User) authentication.getPrincipal();
            userService.setLocale(userInSession.getUsername(), locale);
        }

        cookieLocaleResolver.setLocale(request, response, locale);
    }

    @Override
    public NavigableMap<String, String> getAvailableLanguages() {
        NavigableMap<String, String> languages = new TreeMap<>();
        Locale english = Locale.ENGLISH;

        for (Bundle bundle : bundleContext.getBundles()) {
            for (String path : I18N_RESOURCES_PATHS) {
                Enumeration resources = bundle.getEntryPaths(path);

                if (resources != null) {
                    while (resources.hasMoreElements()) {
                        String file = resources.nextElement().toString()
                                .replace(path, "");

                        int underscore = file.indexOf('_');
                        int dot = file.lastIndexOf('.');

                        if (underscore != -1 && dot != -1) {
                            String langShort = file.substring(underscore + 1, dot);
                            Locale locale = LocaleUtils.toLocale(langShort);
                            String langFull = WordUtils.capitalize(locale.getDisplayLanguage(locale));
                            languages.put(langShort, langFull);
                        }
                    }
                }
            }
        }

        if (!languages.containsKey(english.toString())) {
            languages.put(english.toString(), english.getDisplayLanguage(english));
        }

        return languages;
    }

    @Override
    public Map<String, String> getMessages(HttpServletRequest request) {
        Map<String, Collection<ModuleRegistrationData>> modules = uiFrameworkService.getRegisteredModules();

        Map<String, String> result = new HashMap<>();

        for (Map.Entry<String, Collection<ModuleRegistrationData>> entry : modules.entrySet()) {
            for (ModuleRegistrationData moduleRegistrationData : entry.getValue()) {
                Map<String, String> map = dataFromModuleRegistrationData(request, moduleRegistrationData.getBundle());
                result.putAll(map);
            }
        }

        return result;
    }

    private Map<String, String> dataFromModuleRegistrationData(HttpServletRequest request, Bundle bundle) {

        Map<String, String> result = new HashMap<>();

        try {
            for (String path : I18N_RESOURCES_PATHS) {
                Enumeration<URL> defaultMsgResources = bundle.findEntries(path, "messages.properties", true);

                if (defaultMsgResources != null) {
                    Properties props = loadFromResources(defaultMsgResources);
                    result.putAll((Map) props);
                }

                String fileName = String.format("messages_%s.properties", getUserLocale(request));
                Enumeration<URL> msgResources = bundle.findEntries(path, fileName, true);

                if (msgResources != null) {
                    Properties props = loadFromResources(msgResources);
                    result.putAll((Map) props);
                }
            }
        } catch (IOException e) {
            LOG.error("Unable to load bundle messages", e);
        }
        return result;
    }

    private Properties loadFromResources(Enumeration<URL> resources) throws IOException {
        Properties props = new Properties();

        while (resources.hasMoreElements()) {
            URL resourceUrl = resources.nextElement();
            try (InputStream in = resourceUrl.openStream()) {
                props.load(in);
            }
        }

        return props;
    }

    @Override
    public void setBundleContext(final BundleContext context) {
        this.bundleContext = context;
    }
}
