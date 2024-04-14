package com.guicedee.vertx.sessionpertransaction;

import com.google.inject.Scope;
import com.google.inject.persist.UnitOfWork;
import com.guicedee.client.IGuiceContext;
import com.guicedee.guicedservlets.servlets.services.IOnCallScopeEnter;

public class StartUnitOfWork implements IOnCallScopeEnter<StartUnitOfWork>
{
    @Override
    public void onScopeEnter(Scope scope)
    {
        UnitOfWork work = IGuiceContext.get(UnitOfWork.class);
        work.begin();
    }
}
