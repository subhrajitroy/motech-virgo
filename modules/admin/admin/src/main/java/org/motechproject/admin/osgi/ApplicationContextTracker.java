package org.motechproject.admin.osgi;

import org.eclipse.gemini.blueprint.util.OsgiStringUtils;
import org.motechproject.osgi.web.ModuleRegistrationData;
import org.motechproject.osgi.web.UIFrameworkService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class ApplicationContextTracker extends ServiceTracker {


    private List<ModuleRegistrationData> moduleRegistrationDataList = new ArrayList<>();

    public ApplicationContextTracker(BundleContext context) {
        super(context, ApplicationContext.class.getName(), null);
    }

    @Override
    public Object addingService(ServiceReference reference) {
        Object service = super.addingService(reference);
        Bundle bundle = reference.getBundle();
        if (service instanceof ApplicationContext) {
            ApplicationContext applicationContext = (ApplicationContext) service;
            if (applicationContext.containsBean("moduleRegistrationData")) {
                ModuleRegistrationData moduleRegistrationData = applicationContext.getBean(ModuleRegistrationData.class);
                moduleRegistrationData.setBundle(bundle);
                tryRegisteringToUIFrameworkService(moduleRegistrationData);
                System.out.println("Module Data registered for bundle " + OsgiStringUtils.nullSafeSymbolicName(bundle));
            }
        }
        System.out.println("Added context for Bundle " + OsgiStringUtils.nullSafeSymbolicName(bundle));
        return service;
    }

    private void tryRegisteringToUIFrameworkService(ModuleRegistrationData moduleRegistrationData) {
        moduleRegistrationDataList.add(moduleRegistrationData);
        ServiceReference serviceReference = context.getServiceReference(UIFrameworkService.class.getName());
        if (serviceReference != null) {
            UIFrameworkService uiFrameworkService = (UIFrameworkService) context.getService(serviceReference);
            for (ModuleRegistrationData registrationData : moduleRegistrationDataList) {
                uiFrameworkService.registerModule(registrationData);
                System.out.println("Registered module data for " + OsgiStringUtils.nullSafeSymbolicName(registrationData.getBundle()));
            }
            moduleRegistrationDataList.clear();
        }
    }

}
