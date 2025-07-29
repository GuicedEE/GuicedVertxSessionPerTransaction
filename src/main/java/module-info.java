import com.guicedee.guicedservlets.servlets.services.IOnCallScopeEnter;
import com.guicedee.guicedservlets.servlets.services.IOnCallScopeExit;
import com.guicedee.vertx.sessionpertransaction.EndUnitOfWork;
import com.guicedee.vertx.sessionpertransaction.StartUnitOfWork;

module com.guicedee.vertx.sessionpertransaction {
    requires transitive com.guicedee.vertxpersistence;
    requires io.vertx.core;
    requires java.logging;
    requires org.hibernate.reactive;
    requires io.smallrye.mutiny;

    exports com.guicedee.vertx.sessionpertransaction;

    provides IOnCallScopeEnter with StartUnitOfWork;
    provides IOnCallScopeExit with EndUnitOfWork;
}
