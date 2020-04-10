package org.covid19.live.module.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.covid19.live.utilities.eventbus.EventbusImpl;
import org.covid19.live.utilities.eventbus.IEventbus;
import org.covid19.live.utilities.threading.BusinessExecutor;
import org.covid19.live.utilities.threading.IBusinessExecutor;

public class DashboardViewModelFactory implements ViewModelProvider.Factory {

    private static DashboardViewModelFactory dashboardViewModelFactory = new DashboardViewModelFactory();
    private IEventbus eventBus;
    private IBusinessExecutor businessExecutor;

    public static DashboardViewModelFactory getInstance() {
        return dashboardViewModelFactory;
    }

    private DashboardViewModelFactory() {
        this.eventBus = EventbusImpl.getInstance();
        this.businessExecutor = BusinessExecutor.getInstance();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DashboardViewModel.class)) {
            return (T) new DashboardViewModel(eventBus, businessExecutor);
        }
        throw new IllegalArgumentException("Unknown model class " + modelClass);
    }
}
