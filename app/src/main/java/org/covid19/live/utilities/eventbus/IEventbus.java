package org.covid19.live.utilities.eventbus;

/**
 * Created by Rahul Sutradhar on 07/12/18.
 * Email : rahul.sutradhar@mygate.in
 */
public interface IEventbus {
    /**
     * @see EventbusImpl#register(Object)
     */
    public void register(Object subscriber) throws NullPointerException;

    /**
     * @see EventbusImpl#unregister(Object)
     */
    public void unregister(Object subscriber) throws NullPointerException;

    void postByBusinessThread(Object event);

    /**
     * @see EventbusImpl#post(Object)
     */
    public void post(Object event);
}
