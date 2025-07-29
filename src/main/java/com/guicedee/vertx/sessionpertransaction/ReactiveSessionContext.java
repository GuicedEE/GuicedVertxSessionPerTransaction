package com.guicedee.vertx.sessionpertransaction;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import com.guicedee.client.IGuiceContext;

/**
 * A context class for storing the reactive session in the Vertx context.
 * This allows the session to be accessed across different parts of the application
 * within the same Vertx context.
 */
public class ReactiveSessionContext {
    private static final String CONTEXT_KEY = "REACTIVE_SESSION";

    /**
     * Stores the session in the current Vertx context.
     *
     * @param session The reactive session to store
     */
    public static void setSession(Object session) {
        Context context = Vertx.currentContext();
        if (context != null) {
            context.put(CONTEXT_KEY, session);
        }
    }

    /**
     * Retrieves the session from the current Vertx context.
     *
     * @return The stored reactive session, or null if not found
     */
    public static Object getSession() {
        Context context = Vertx.currentContext();
        if (context != null) {
            return context.get(CONTEXT_KEY);
        }
        return null;
    }

    /**
     * Clears the session from the current Vertx context.
     */
    public static void clearSession() {
        Context context = Vertx.currentContext();
        if (context != null) {
            context.remove(CONTEXT_KEY);
        }
    }
}
