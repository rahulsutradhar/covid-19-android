package org.covid19.live.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import org.covid19.live.lifecycle.ManagerLifeCycleController;
import org.covid19.live.utilities.threading.BusinessExecutor;
import org.covid19.live.utilities.threading.IBusinessExecutor;
import org.covid19.live.utilities.threading.MainExecutor;

public class AppController extends Application {

    public static final String TAG = "AppController";
    private static AppController mInstance;
    private static Context sContext;

    private ManagerLifeCycleController managerLifeCycleController;
    private IBusinessExecutor businessExecutor;

    @Override
    public void onCreate() {
        mInstance = this;
        sContext = this;
        super.onCreate();

        MainExecutor.getInstance().setMainThreadHandler(new Handler());
        managerLifeCycleController = new ManagerLifeCycleController();
        businessExecutor = BusinessExecutor.getInstance();
        businessExecutor.executeInBusinessThread(new Runnable() {
            @Override
            public void run() {
                managerLifeCycleController.init();
            }
        });

    }

    public static AppController getmInstance() {
        return mInstance;
    }

    public static void setmInstance(AppController mInstance) {
        AppController.mInstance = mInstance;
    }

    public static Context getsContext() {
        return sContext;
    }

    public static void setsContext(Context sContext) {
        AppController.sContext = sContext;
    }
}
