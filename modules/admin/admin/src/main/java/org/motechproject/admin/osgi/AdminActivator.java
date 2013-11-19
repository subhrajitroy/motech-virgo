package org.motechproject.admin.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class AdminActivator implements BundleActivator {

    private ApplicationContextTracker applicationContextTracker;

    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("ACTIVATOR ACTIVATED !!!!!!");
        applicationContextTracker = new ApplicationContextTracker(context);
        applicationContextTracker.open();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        if (applicationContextTracker != null) {
            applicationContextTracker.close();
        }
    }
}
