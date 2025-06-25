package com.closeddoor.client;

import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class SessionController {

    private static final Logger logger = Logger.getLogger(SessionController.class.getName());

    private final Instant sessionStart;
    private final Timer timer;
    private boolean logoutScheduled;
    private boolean restartScheduled;

    private static final long MAX_SESSION_DURATION_MS = Duration.ofHours(6).toMillis(); // Simulate a long human session
    private static final long INACTIVITY_LOGOUT_MS = Duration.ofMinutes(30).toMillis(); // Auto-logout if idle too long

    public SessionController() {
        this.sessionStart = Instant.now();
        this.timer = new Timer(true);
        this.logoutScheduled = false;
        this.restartScheduled = false;

        startInactivityMonitor();
    }

    private void startInactivityMonitor() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                long runtime = Duration.between(sessionStart, Instant.now()).toMillis();

                if (!logoutScheduled && runtime > MAX_SESSION_DURATION_MS) {
                    logger.info("Session too long, scheduling logout.");
                    scheduleLogout();
                }

                // Add more runtime decisions here if needed.
            }
        }, 0, 60_000); // Check every minute
    }

    public void scheduleLogout() {
        logoutScheduled = true;
        // Inject logout logic here
        logger.warning("Logout has been scheduled. Preparing safe exit.");
    }

    public void scheduleRestart() {
        restartScheduled = true;
        // Inject restart logic here
        logger.warning("Client restart has been scheduled. Preparing reboot.");
    }

    public boolean isLogoutScheduled() {
        return logoutScheduled;
    }

    public boolean isRestartScheduled() {
        return restartScheduled;
    }

    public Duration getSessionDuration() {
        return Duration.between(sessionStart, Instant.now());
    }

    public void shutdown() {
        timer.cancel();
        logger.info("SessionController has been shut down.");
    }

    public void logSessionInfo() {
        logger.info("Session Duration: " + getSessionDuration().toMinutes() + " minutes.");
        logger.info("Logout Scheduled: " + logoutScheduled);
        logger.info("Restart Scheduled: " + restartScheduled);
    }
}
