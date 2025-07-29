package com.guicedee.vertx.sessionpertransaction;

import com.google.inject.Scope;
import com.guicedee.client.IGuiceContext;
import com.guicedee.guicedservlets.servlets.services.IOnCallScopeEnter;
import io.vertx.core.Vertx;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Starts a reactive hibernate session and transaction when a call scope is entered.
 * This class is registered as an IOnCallScopeEnter service provider.
 */
public class StartUnitOfWork implements IOnCallScopeEnter<StartUnitOfWork> {
    private static final Logger logger = Logger.getLogger(StartUnitOfWork.class.getName());

    @Override
    public void onScopeEnter(Scope scope) {
        try {
            // Get the Mutiny.SessionFactory from Guice
            Mutiny.SessionFactory sessionFactory = IGuiceContext.get(Mutiny.SessionFactory.class);
            if (sessionFactory == null) {
                logger.warning("No Mutiny.SessionFactory found in Guice context. Skipping reactive session creation.");
                return;
            }

            // Get the current Vertx instance
            if (Vertx.currentContext() == null) {
                logger.warning("No Vertx context found. Skipping reactive session creation.");
                return;
            }

            Vertx vertx = Vertx.currentContext().owner();

            // Run on the Vertx context to ensure proper async handling
            vertx.runOnContext(v -> {
                try {
                    // Open a new session
                    sessionFactory.openSession()
                        .onItem().invoke(session -> {
                            logger.fine("Reactive session opened successfully");
                            // Store the session in the context
                            ReactiveSessionContext.setSession(session);
                        })
                        .onFailure().invoke(error -> {
                            logger.log(Level.SEVERE, "Failed to open reactive session", error);
                        })
                        .subscribe().asCompletionStage();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error opening reactive session", e);
                }
            });
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error starting reactive unit of work", e);
        }
    }
}
