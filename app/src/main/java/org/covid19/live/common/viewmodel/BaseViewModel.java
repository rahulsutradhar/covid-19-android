package org.covid19.live.common.viewmodel;


import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import org.covid19.live.utilities.eventbus.IEventbus;
import org.covid19.live.utilities.threading.IBusinessExecutor;

public abstract class BaseViewModel extends ViewModel implements LifecycleObserver {
    protected IEventbus eventBus;
    protected IBusinessExecutor businessExecutor;

    public BaseViewModel(IEventbus eventbus, IBusinessExecutor businessExecutor) {
        super();
        this.eventBus = eventbus;
        this.businessExecutor = businessExecutor;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        eventBus.register(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        eventBus.unregister(this);
    }
}
