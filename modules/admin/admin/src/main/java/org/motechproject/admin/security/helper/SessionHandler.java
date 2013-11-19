package org.motechproject.admin.security.helper;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class SessionHandler {

    private static final Map<String, HttpSession> SESSIONS = new HashMap<>();

    public void addSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        SESSIONS.put(session.getId(), session);
    }

    public void removeSession(HttpServletRequest request) {
        SESSIONS.remove(request.getSession().getId());
    }

    public Collection<HttpSession> getAllSessions() {
        return SESSIONS.values();
    }
}
