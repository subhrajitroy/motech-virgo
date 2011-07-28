package org.motechproject.server.messagecampaign.service;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.dao.MotechJsonReader;
import org.motechproject.server.messagecampaign.dao.AllMessageCampaigns;
import org.motechproject.server.messagecampaign.domain.Campaign;
import org.motechproject.server.messagecampaign.domain.CampaignMessage;

import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class AllMessageCampaignsTest {

    private AllMessageCampaigns allMessageCampaigns;

    @Before
    public void setup() {
        Properties properties = new Properties();
        properties.setProperty("messagecampaign.definition.file", "simple-message-campaign.json");
        MotechJsonReader motechJsonReader = new MotechJsonReader();
        allMessageCampaigns = new AllMessageCampaigns(properties, motechJsonReader);
    }

    @Test
    public void getCampaignWithRelativeScheduleTest() {
        String campaignName = "Weekly Info Child Program";

        Campaign campaign = allMessageCampaigns.get(campaignName);
        assertNotNull(campaign);
        assertEquals(campaignName, campaign.getName());
        List<CampaignMessage> messages = campaign.getMessages();
        assertEquals(3, messages.size());
        assertMessageWithRelativeSchedule(messages.get(0), "Week 1", new String[]{"IVR"}, "child-info-week-1", "1 Week");
        assertMessageWithRelativeSchedule(messages.get(1), "Week 1A", new String[]{"SMS"}, "child-info-week-1a", "1 Week");
        assertMessageWithRelativeSchedule(messages.get(2), "Week 1B", new String[]{"SMS"}, "child-info-week-1b", "9 Days");
    }

    @Test
    public void getCampaignWithAbsoluteScheduleTest() {
        String campaignName = "Absolute Dates Message Program";

        Campaign campaign = allMessageCampaigns.get(campaignName);
        assertNotNull(campaign);
        assertEquals(campaignName, campaign.getName());
        List<CampaignMessage> messages = campaign.getMessages();
        assertEquals(2, messages.size());
        DateTime firstDate = new DateTime(2011, 6, 15, 0, 0, 0, 0);
        DateTime secondDate = new DateTime(2011, 6, 22, 0, 0, 0, 0);
        assertMessageWithAbsoluteSchedule(messages.get(0), "First", new String[]{"IVR", "SMS"}, "random-1", firstDate.toDate());
        assertMessageWithAbsoluteSchedule(messages.get(1), "Second", new String[]{"IVR"}, "random-2", secondDate.toDate());
    }

    private void assertMessageWithAbsoluteSchedule(CampaignMessage message, String name, String[] formats, Object messageKey, Date date) {
        assertMessage(message, name, formats, messageKey);
        assertEquals(date, message.date());
    }

    private void assertMessageWithRelativeSchedule(CampaignMessage message, String name, String[] formats, Object messageKey, String timeOffset) {
        assertMessage(message, name, formats, messageKey);
        assertEquals(timeOffset, message.timeOffset());
    }

    private void assertMessage(CampaignMessage message, String name, String[] formats, Object messageKey) {
        assertEquals(name, message.name());
        assertCollection(formats, message.formats());
        assertCollection(new String[]{"en"}, message.languages());
        assertEquals(messageKey, message.messageKey());
    }

    private void assertCollection(String[] expectedFormats, List<String> actualFormats) {
        assertEquals(new HashSet<String>(Arrays.asList(expectedFormats)), new HashSet<String>(actualFormats));
    }
}