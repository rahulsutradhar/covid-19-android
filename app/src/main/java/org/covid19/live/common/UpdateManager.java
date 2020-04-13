package org.covid19.live.common;

import android.content.Context;
import android.content.IntentSender;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import java.lang.ref.WeakReference;

import static org.covid19.live.common.AppConstant.APP_UPDATE_REQUEST_CODE_IMMEDIATE;
import static org.covid19.live.common.AppConstant.MODERATELY_HIGH_PRIORITY_UPDATE;

public class UpdateManager {

    private static final String TAG = "UpdateManager";

    /**
     * Check for App Udate
     */
    public static void checkforAppUpdate(final WeakReference<Context> activityContext, final AppUpdateLister updateLister) {
        // Creates instance of the manager.
        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(activityContext.get());

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks whether the platform allows the specified type of update,
        // and checks the update priority.

        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {

                /**
                 * If update is Aavailable
                 */
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {

                   /* if (appUpdateInfo.updatePriority() >= MODERATELY_HIGH_PRIORITY_UPDATE
                            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                        //TODO inform user about app update available
                        updateLister.isAppUpdateAvailable(true);

                    } else {
                        //TODO: request flexible update

                    }*/

                    updateLister.isAppUpdateAvailable(true, appUpdateInfo);
                } else {
                    updateLister.isAppUpdateAvailable(false, null);
                }

            }
        });
    }

    /**
     * Immediate update
     *
     * @param activityContext
     * @param appUpdateInfo
     */
    public static void requestImmediateAppUpdate(WeakReference<Context> activityContext, AppUpdateInfo appUpdateInfo) {
        // Creates instance of the manager.
        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(activityContext.get());

        try {
            appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    (AppCompatActivity) activityContext.get(),
                    // Include a request code to later monitor this update request.
                    APP_UPDATE_REQUEST_CODE_IMMEDIATE);

        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public interface AppUpdateLister {
        void isAppUpdateAvailable(boolean isAvailable, AppUpdateInfo appUpdateInfo);
    }
}
