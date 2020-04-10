package org.covid19.live.module.ui.viewmodel;

import org.covid19.live.common.viewmodel.BaseViewModel;
import org.covid19.live.utilities.eventbus.IEventbus;
import org.covid19.live.utilities.threading.IBusinessExecutor;

public class DashboardViewModel extends BaseViewModel {

    public DashboardViewModel(IEventbus eventbus, IBusinessExecutor businessExecutor) {
        super(eventbus, businessExecutor);
    }

}
