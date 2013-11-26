package org.motechproject.tasks.service.impl;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.server.api.BundleIcon;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ChannelRequest;
import org.motechproject.tasks.domain.Channel;
import org.motechproject.tasks.domain.ChannelDeregisterEvent;
import org.motechproject.tasks.domain.ChannelRegisterEvent;
import org.motechproject.tasks.domain.TaskError;
import org.motechproject.tasks.ex.ValidationException;
import org.motechproject.tasks.json.ActionEventRequestDeserializer;
import org.motechproject.tasks.repository.AllChannels;
import org.motechproject.tasks.service.ChannelService;
import org.motechproject.tasks.validation.ChannelValidator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.motechproject.tasks.events.constants.EventDataKeys;
import org.motechproject.tasks.events.constants.EventSubjects;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.eclipse.gemini.blueprint.util.OsgiStringUtils.nullSafeSymbolicName;
import static org.motechproject.server.api.BundleIcon.ICON_LOCATIONS;

/**
 * A {@link org.motechproject.tasks.service.ChannelService}, used to manage CRUD operations for a {@link org.motechproject.tasks.domain.Channel} over a couchdb database.
 */
@Service("channelService")
public class ChannelServiceImpl implements ChannelService {
    private static final Logger LOG = LoggerFactory.getLogger(ChannelServiceImpl.class);

    private static final String DEFAULT_ICON = "/webapp/img/iconTaskChannel.png";

    private static Map<Type, Object> typeAdapters = new HashMap<>();

    private AllChannels allChannels;
    private MotechJsonReader motechJsonReader;
    private ResourceLoader resourceLoader;
    private EventRelay eventRelay;

    static {
        typeAdapters.put(ActionEventRequest.class, new ActionEventRequestDeserializer());
    }

    private BundleContext bundleContext;
    private IconLoader iconLoader;

    @Autowired
    public ChannelServiceImpl(AllChannels allChannels, ResourceLoader resourceLoader, EventRelay eventRelay, IconLoader iconLoader) {
        this.allChannels = allChannels;
        this.eventRelay = eventRelay;
        this.resourceLoader = resourceLoader;
        this.iconLoader = iconLoader;
        this.motechJsonReader = new MotechJsonReader();
    }

    @Override
    public void registerChannel(ChannelRequest channelRequest) {
        addOrUpdate(new Channel(channelRequest));
        eventRelay.sendEventMessage(new ChannelRegisterEvent(channelRequest.getModuleName()).toMotechEvent());
    }

    @Override
    public void registerChannel(final InputStream stream, String moduleName, String moduleVersion) {
        Type type = new TypeToken<ChannelRequest>() {
        }.getType();
        StringWriter writer = new StringWriter();

        try {
            IOUtils.copy(stream, writer);
            ChannelRequest channelRequest = (ChannelRequest) motechJsonReader.readFromString(writer.toString(), type, typeAdapters);
            channelRequest.setModuleName(moduleName);
            channelRequest.setModuleVersion(moduleVersion);

            registerChannel(channelRequest);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void deregisterChannel(String moduleName) {
        Channel channel = getChannel(moduleName);
        if (channel != null) {
            deregisterChannel(channel);
        }
    }

    @Override
    public void addOrUpdate(Channel channel) {
        Set<TaskError> errors = ChannelValidator.validate(channel);

        if (!isEmpty(errors)) {
            throw new ValidationException(ChannelValidator.CHANNEL, errors);
        }

        boolean update = allChannels.addOrUpdate(channel);
        LOG.info(String.format("Saved channel: %s", channel.getDisplayName()));

        if (update) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put(EventDataKeys.CHANNEL_MODULE_NAME, channel.getModuleName());

            eventRelay.sendEventMessage(new MotechEvent(EventSubjects.CHANNEL_UPDATE_SUBJECT, parameters));
        }
    }

    @Override
    public List<Channel> getAllChannels() {
        return allChannels.getAll();
    }

    @Override
    public Channel getChannel(final String moduleName) {
        return allChannels.byModuleName(moduleName);
    }

    @Override
    public void deregisterAllChannels() {
        for (Channel channel : getAllChannels()) {
            deregisterChannel(channel);
        }
    }

    private void deregisterChannel(Channel channel) {
        allChannels.remove(channel);
        eventRelay.sendEventMessage(new ChannelDeregisterEvent(channel.getModuleName()).toMotechEvent());
    }

    @Override
    public BundleIcon getChannelIcon(String moduleName) throws IOException {
        Bundle bundle = getModule(moduleName);
        BundleIcon bundleIcon = null;
        URL iconURL;

        if (bundle != null) {
            for (String iconLocation : ICON_LOCATIONS) {
                iconURL = bundle.getResource(iconLocation);

                if (iconURL != null) {
                    bundleIcon = iconLoader.load(iconURL);
                    break;
                }
            }
        }

        if (bundleIcon == null) {
            iconURL = resourceLoader.getResource(DEFAULT_ICON).getURL();
            bundleIcon = iconLoader.load(iconURL);
        }

        return bundleIcon;
    }

    @Autowired(required = false)
    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    private Bundle getModule(String moduleSymbolicName) {
        if (bundleContext == null) {
            throw new IllegalArgumentException("Bundle context not set");
        }


        for (Bundle bundle : bundleContext.getBundles()) {
            if (nullSafeSymbolicName(bundle).equalsIgnoreCase(moduleSymbolicName)) {
                return bundle;
            }
        }

        LOG.warn(String.format("Module with moduleName: %s not found", moduleSymbolicName));
        return null;
    }

}
