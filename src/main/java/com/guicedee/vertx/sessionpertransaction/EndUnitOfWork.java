package com.guicedee.vertx.sessionpertransaction;

import com.google.inject.persist.UnitOfWork;
import com.guicedee.client.IGuiceContext;
import com.guicedee.guicedservlets.servlets.services.IOnCallScopeExit;

public class EndUnitOfWork implements IOnCallScopeExit<EndUnitOfWork>
{
    @Override
    public void onScopeExit()
    {
        UnitOfWork work = IGuiceContext.get(UnitOfWork.class);
        work.end();
    }
}
