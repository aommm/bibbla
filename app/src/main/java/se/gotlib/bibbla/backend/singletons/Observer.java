package se.gotlib.bibbla.backend.singletons;

import java.beans.PropertyChangeListener;

/**
 * Created by Master on 2014-10-20.
 */
public interface Observer {
    public void addListener(PropertyChangeListener pcl);
}
