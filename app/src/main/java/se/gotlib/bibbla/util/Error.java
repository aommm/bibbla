package se.gotlib.bibbla.util;

public enum Error {
    /*
	LOGIN_FAILED,
	SEARCH_FAILED,
	FETCHING_INFO_FAILED,
	RENEW_FAILED,
	UNRESERVE_FAILED,
	RESERVE_FAILED,
	MY_RESERVATIONS_FAILED,
	MY_BOOKS_FAILED,
	MY_DEBT_FAILED,
	DETAILED_VIEW_FAILED,
	*/

	// Generic errors
	NETWORK,
	INPUT,

	// Network related
//    CONNECTION_ATTEMPTS_EXCEEDED,
//    TIMEOUT_EXCEEDED,

    // Login related
    INCORRECT_BIBBLA_CREDENTIALS,

    /*
    MISSING_CREDENTIALS, // Some user credentials are missing.
    LOGIN_NEEDED, // Used by other jobs, if they detect they're not logged in.
	// Session.fetchUserUrl() returned an empty string.
	// May indicate incorrect details, no network, you name it.
	FETCHING_USER_URL_FAILED
    */
}