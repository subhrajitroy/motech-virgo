package org.motechproject.admin.core.web.controller;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.admin.server.startup.StartupManager;
import org.motechproject.admin.server.ui.LocaleService;
import org.motechproject.admin.server.web.dto.ModuleMenu;
import org.motechproject.admin.server.web.form.UserInfo;
import org.motechproject.admin.server.web.helper.MenuBuilder;
import org.motechproject.osgi.web.ModuleRegistrationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.lang.management.ManagementFactory;
import java.util.Locale;

import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.motechproject.commons.date.util.DateUtil.now;

@Controller
public class DashboardController {


    @Autowired
    private StartupManager startupManager;


    @Autowired
    private ModuleRegistrationData moduleRegistrationData;

    @Autowired
    private LocaleService localeService;

    @Autowired
    private MenuBuilder menuBuilder;

    @RequestMapping({"/index", "/", "/home"})
    public ModelAndView index(final HttpServletRequest request) {
        ModelAndView mav;

        // check if this is the first run
        if (startupManager.isConfigRequired()) {
            mav = new ModelAndView("redirect:startup.do");
        } else {
            mav = indexPage(request);
        }

        return mav;
    }

    private ModelAndView indexPage(HttpServletRequest request) {
        ModelAndView mav;
        mav = new ModelAndView("index");
        String contextPath = request.getSession().getServletContext().getContextPath();

        if (StringUtils.isNotBlank(contextPath) && !"/".equals(contextPath)) {
            mav.addObject("contextPath", contextPath.substring(1) + "/");
        } else if (StringUtils.isBlank(contextPath) || "/".equals(contextPath)) {
            mav.addObject("contextPath", "");
        }

        if (moduleRegistrationData != null) {
            mav.addObject("currentModule", moduleRegistrationData);
            mav.addObject("criticalNotification", moduleRegistrationData.getCriticalMessage());
        }
        return mav;
    }

    @RequestMapping(value = "/modulemenu", method = RequestMethod.GET)
    @ResponseBody
    public ModuleMenu getModuleMenu(HttpServletRequest request) {
        String username = getUser(request).getUserName();
        return menuBuilder.buildMenu(username);
    }

    @RequestMapping(value = "/gettime", method = RequestMethod.POST)
    @ResponseBody
    public String getTime(HttpServletRequest request) {
        Locale locale = localeService.getUserLocale(request);
        DateTimeFormatter format = forPattern("EEE MMM dd, h:mm a, z yyyy").withLocale(locale);
        return now().toString(format);
    }

    @RequestMapping(value = "/getUptime", method = RequestMethod.POST)
    @ResponseBody
    public DateTime getUptime() {
        return now().minus(ManagementFactory.getRuntimeMXBean().getUptime());
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.POST)
    @ResponseBody
    public UserInfo getUser(HttpServletRequest request) {
        String lang = localeService.getUserLocale(request).getLanguage();
        boolean securityLaunch = request.getUserPrincipal() != null;
        String userName = securityLaunch ? request.getUserPrincipal().getName() : "Admin Mode";

        return new UserInfo(userName, securityLaunch, lang);
    }


}
