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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import se.gotlib.bibbla.backend.model.GotlibCredentials;
import se.gotlib.bibbla.backend.model.GotlibSession;
import se.gotlib.bibbla.util.Message;
import se.gotlib.bibbla.util.Observable;

/**
 * Logs the user into gotlib.
 * You should listen to this class!
 *
 * Input:  GotlibCredentials
 * Event name: "loginDone"
 * Event value: GotlibSession or null
 * 
 * @author Niklas Logren
 */
public class LoginTask extends Task<GotlibCredentials, Void, Message<GotlibSession>> implements Observable {

    private PropertyChangeSupport pcs;

	public LoginTask() {
        super();
        pcs = new PropertyChangeSupport(this);
	}

    @Override
    protected Message<GotlibSession> doInBackground(GotlibCredentials... gotlibCredentials) {
        if (gotlibCredentials.length==0) return null;

        LoginJob job = new LoginJob(gotlibCredentials[0]);
        try {
            return job.login();
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Message<GotlibSession> message) {
        pcs.firePropertyChange("loginDone", null, message);
    }

    @Override
    public void addListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }
}
