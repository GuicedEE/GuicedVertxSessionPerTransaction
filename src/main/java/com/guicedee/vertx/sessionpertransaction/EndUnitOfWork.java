package com.guicedee.vertx.sessionpertransaction;

import com.guicedee.guicedservlets.servlets.services.IOnCallScopeExit;
import io.vertx.core.Vertx;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ends a reactive hibernate session when a call scope is exited.
 * This class is registered as an IOnCallScopeExit service provider.
 */
public class EndUnitOfWork implements IOnCallScopeExit<EndUnitOfWork> {
    private static final Logger logger = Logger.getLogger(EndUnitOfWork.class.getName());

    @Override
    public void onScopeExit() {
        try {
            // Check if we have a Vertx context
            if (Vertx.currentContext() == null) {
                logger.warning("No Vertx context found. Skipping reactive session cleanup.");
                return;
            }

            // Get the current Vertx instance
            Vertx vertx = Vertx.currentContext().owner();

            // Run on the Vertx context to ensure proper async handling
            vertx.runOnContext(v -> {
                try {
                    // Retrieve the session from the context
                    Object sessionObj = ReactiveSessionContext.getSession();
                    if (sessionObj == null) {
                        logger.fine("No reactive session found in context. Nothing to close.");
                        return;
                    }

                    if (sessionObj instanceof Mutiny.Session) {
                        Mutiny.Session session = (Mutiny.Session) sessionObj;

                        // Close the session
                        session.close()
                            .onItem().invoke(() -> {
                                logger.fine("Reactive session closed successfully");
                            })
                            .onFailure().invoke(error -> {
                                logger.log(Level.SEVERE, "Failed to close reactive session", error);
                            })
                            .subscribe().asCompletionStage();
                    } else {
                        logger.warning("Session object in context is not a Mutiny.Session. Cannot close it properly.");
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error closing reactive session", e);
                } finally {
                    // Always clear the session from the context
                    ReactiveSessionContext.clearSession();
                }
            });
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error ending reactive unit of work", e);
            // Ensure the session is cleared from the context
            ReactiveSessionContext.clearSession();
        }
    }
}
