package org.covid19.live.common.factory;


import org.covid19.live.module.manager.DashboardManager;
import org.covid19.live.module.manager.IDashboardManager;


public class ManagerFactory {

    public static IDashboardManager getDashboardDataManager() {
        return DashboardManager.getInstance();
    }
}
