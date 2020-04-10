package org.covid19.live.utilities.threading;

import android.os.Handler;

/**
 * Created by Rahul Sutradhar on 07/12/18.
 * Email : rahul.sutradhar@mygate.in
 */
public interface IMainExecutor {

    /**
     * Creates one-shot action and executes it ASAP on main thread.
     * Passed command may have to wait for other tasks to complete before
     * running.
     * @pre {@code null != command}
     * @param command command to run on main thread
     */
    public void executeInMainThread(Runnable command);

    public void setMainThreadHandler(Handler handler);

    void scheduleInMainThread(Runnable command, long timeInMillis);

    void removeRunnable(Runnable command);
}
