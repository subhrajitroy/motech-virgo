package org.motechproject.admin.security.email;

import org.motechproject.admin.security.domain.Mail;
import org.motechproject.admin.security.domain.MotechUser;
import org.motechproject.admin.security.domain.PasswordRecovery;

public interface EmailSender {

    void sendResecoveryEmail(PasswordRecovery recovery);

    void sendOneTimeToken(PasswordRecovery recovery);

    void sendLoginInfo(MotechUser user, String password);

    void send(Mail mail);
}
