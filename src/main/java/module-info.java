import com.guicedee.guicedservlets.servlets.services.IOnCallScopeEnter;
import com.guicedee.guicedservlets.servlets.services.IOnCallScopeExit;
import com.guicedee.vertx.sessionpertransaction.EndUnitOfWork;
import com.guicedee.vertx.sessionpertransaction.StartUnitOfWork;

module com.guicedee.vertx.sessionpertransaction {
    requires com.guicedee.guicedpersistence;
    requires com.guicedee.client;

    provides IOnCallScopeEnter with StartUnitOfWork;
    provides IOnCallScopeExit with EndUnitOfWork;

}