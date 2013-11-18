package org.motechproject.server.web.controller;

import org.apache.commons.lang.StringUtils;
import org.motechproject.config.service.ConfigurationService;
import org.motechproject.server.startup.StartupManager;
import org.motechproject.server.ui.LocaleService;
import org.motechproject.server.web.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Login Controller for user authentication.
 */
@Controller
public class LoginController {

    @Autowired
    private LocaleService localeService;
    @Autowired
    private ConfigurationService settingsService;
    @Autowired
    private StartupManager startupManager;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(final HttpServletRequest request) {
        if (startupManager.isBootstrapConfigRequired()) {
            return new ModelAndView("redirect:bootstrap.do");
        }

        if (startupManager.isConfigRequired()) {
            return new ModelAndView("redirect:startup.do");
        }

        ModelAndView view = new ModelAndView("loginPage");

        String contextPath = request.getSession().getServletContext().getContextPath();

        if (StringUtils.isNotBlank(contextPath) && !"/".equals(contextPath)) {
            view.addObject("contextPath", contextPath.substring(1) + "/");
        } else if (StringUtils.isBlank(contextPath) || "/".equals(contextPath)) {
            view.addObject("contextPath", "");
        }

        view.addObject("loginMode", settingsService.getPlatformSettings().getLoginMode());
        view.addObject("openIdProviderName", settingsService.getPlatformSettings().getProviderName());
        view.addObject("openIdProviderUrl", settingsService.getPlatformSettings().getProviderUrl());
        view.addObject("error", request.getParameter("error"));
        view.addObject("loginForm", new LoginForm());
        view.addObject("pageLang", localeService.getUserLocale(request));

        return view;
    }

    @RequestMapping(value = "/accessdenied", method = RequestMethod.GET)
    public ModelAndView accessdenied(final HttpServletRequest request) {
        return new ModelAndView("accessdenied");
    }

}
