package se.gotlib.bibbla.util;

import java.beans.PropertyChangeListener;

/**
 * Created by Master on 2014-10-20.
 */
public interface Observable {
    public void addListener(PropertyChangeListener pcl);
}
