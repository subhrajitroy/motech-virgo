package org.motechproject.callflow.domain;

/**
 * Typical IVR events triggered from IVR systems to run call flow.
 */
public enum IVREvent {
    NewCall, Dial, Record, GotDTMF, Hangup, Disconnect, Missed
}