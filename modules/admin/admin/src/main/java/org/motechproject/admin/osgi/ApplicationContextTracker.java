package org.motechproject.admin.osgi;

import org.eclipse.gemini.blueprint.util.OsgiStringUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.springframework.context.ApplicationContext;

public class ApplicationContextTracker extends ServiceTracker {

    public ApplicationContextTracker(BundleContext context) {
        super(context, ApplicationContext.class.getName(), null);
    }

    @Override
    public Object addingService(ServiceReference reference) {
        Object service = super.addingService(reference);
        Bundle bundle = reference.getBundle();
        System.out.println("Added context for Bundle " + OsgiStringUtils.nullSafeSymbolicName(bundle));
        return service;
    }
}
