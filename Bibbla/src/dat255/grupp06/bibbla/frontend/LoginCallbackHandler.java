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

//Copyright 2012 Fahad Al-Khameesi, Madeleine Appert, Niklas Logren, Arild Matsson and Jonathan Orrö.