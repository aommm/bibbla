/**
    Copyright 2012 Fahad Al-Khameesi, Madeleine Appert, Niklas Logren, Arild Matsson and Jonathan Orr�.
    
    This file is part of Bibbla.

    Bibbla is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Bibbla is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Bibbla.  If not, see <http://www.gnu.org/licenses/>.    
 **/

package se.gotlib.bibbla.backend.tasks;

import android.os.AsyncTask;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import se.gotlib.bibbla.util.Observable;

/** 
 * An abstract class, representing a task which is run in a new thread.
 * 
 * Any subclass should:
 * 1. Perform the heavy operations,
 * 2. Notify listener
 *   
 * @author Niklas Logren
 */
public abstract class Task<A, B, C> extends AsyncTask<A, B, C> implements Observable {

    private PropertyChangeSupport pcs;

    /** Creates a new Task.
     */
    public Task() {
        super();
        pcs  = new PropertyChangeSupport(this);
    }

    @Override
    /**
     * Adds a listener to this Task.
     * Should typically be Library
     */
    public void addListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    /**
     * Fires an event to this Task's listeners
     */
    public void fireEvent(String name, Object oldValue, Object newValue) {
        pcs.firePropertyChange(name, oldValue, newValue);
    }

    /**
     * Fires an event to this Task's listeners.
     * Sets 'oldValue' to be null.
     */
    public void fireEvent(String name, Object newValue) {
        fireEvent(name, null, newValue);
    }

}