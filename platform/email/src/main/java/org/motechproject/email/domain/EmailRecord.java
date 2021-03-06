package org.motechproject.email.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.commons.couchdb.model.MotechBaseDataObject;
import org.motechproject.commons.date.util.DateUtil;

import java.util.Objects;

/**
 * The <code>EmailRecord</code> class represents a single Email record stored in CouchDB
 */

@TypeDiscriminator("doc.type === 'EmailRecord'")
public class EmailRecord extends MotechBaseDataObject {

    @JsonProperty
    private String fromAddress;
    @JsonProperty
    private String toAddress;
    @JsonProperty
    private String subject;
    @JsonProperty
    private String message;
    @JsonProperty
    private DateTime deliveryTime;
    @JsonProperty
    private DeliveryStatus deliveryStatus;

    public EmailRecord() {
        this(null, null, null, null, null, null);
    }

    public EmailRecord(String fromAddress, String toAddress, String subject, String message, DateTime deliveryTime, DeliveryStatus deliveryStatus) {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.subject = subject;
        this.message = message;
        this.deliveryTime = deliveryTime;
        this.deliveryStatus = deliveryStatus;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public DateTime getDeliveryTime() {
        return DateUtil.setTimeZoneUTC(deliveryTime);
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromAddress, toAddress, subject, message, deliveryTime, deliveryStatus);
    }

    @Override
    public String toString() {
        return String.format("EmailRecord{fromAddress='%s', toAddress='%s', subject='%s', message='%s', deliveryTime='%s', deliveryStatus='%s'}",
                fromAddress, toAddress, subject, message, deliveryTime, deliveryStatus);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        EmailRecord other = (EmailRecord) obj;

        return Objects.equals(this.toAddress, other.toAddress) &&
               Objects.equals(this.fromAddress, other.fromAddress) &&
               Objects.equals(this.subject, other.subject) &&
               Objects.equals(this.message, other.message) &&
               Objects.equals(this.deliveryTime, other.deliveryTime) &&
               Objects.equals(this.deliveryStatus, other.deliveryStatus);
    }

}
