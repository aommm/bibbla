/**
    Copyright 2012 Fahad Al-Khameesi, Madeleine Appert, Niklas Logren, Arild Matsson and Jonathan Orrö.
    
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

package dat255.grupp06.bibbla.frontend;

import dat255.grupp06.bibbla.utils.Callback;

/**
 * Used by fragments and activities to require credentials by user, and give a
 * callback to be called afterwards.   
 * @author arla
 */
public interface LoginCallbackHandler {
	/**
	 * Show login form to user and handle callback afterwards.
	 * @param callback specifies what should be done when user credentials are
	 * filled-in and saved to Backend
	 */
	public void showCredentialsDialog(Callback callback);
}