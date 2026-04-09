package app.config;
import jakarta.servlet.SessionTrackingMode;
import org.eclipse.jetty.ee10.servlet.SessionHandler;

import java.util.EnumSet;

public class SessionConfig {

    public static SessionHandler sessionConfig()
    {
        // create session handler (handles sessions)
        SessionHandler sessionHandler = new SessionHandler();

        // enable cookies for storing session IDs
        sessionHandler.setUsingCookies(true);

        // use only cookies for session tracking
        sessionHandler.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));

        // make session cookie inaccessible to JavaScript (security)
        sessionHandler.setHttpOnly(true);
        return sessionHandler;
    }
}