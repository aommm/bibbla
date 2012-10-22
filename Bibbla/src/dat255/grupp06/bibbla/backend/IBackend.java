package dat255.grupp06.bibbla.backend;

import java.util.List;

import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.model.CredentialsMissingException;
import dat255.grupp06.bibbla.utils.Callback;

/**
 * Performs tasks like searching, reserving and logging in.
 * Does all the heavy lifting.
 * @author Fahad
 *
 */

public interface IBackend {
	
	/**
	 *  @returns the user's user name.
	 *  If no name is saved, returns empty string.
	 */
	
	public void getUserName(Callback frontendCallback) throws CredentialsMissingException;
	
	/**
	 *  Starts fetching the user's current debt. Reports results using callback.
	 *  Uses cached values if available; otherwise, simply runs the job.
	 *  
	 *  @param frontendCallback - the callback object which will be called
	 *   when logging in is done.
	 */
	
	public void fetchUserDebt(final Callback frontendCallback)
	throws CredentialsMissingException;

	/**
	 * Returns the logged in state of the backend.
	 */
	
	public boolean isLoggedIn();
	
	/**
	 *  Starts fetching the user's current debt. Reports results using callback.
	 *  
	 *  @param frontendCallback - the callback object which will be called
	 *   when logging in is done.
	 *  @param refresh - should we run the job directly, and bypass caching? 
	 */
	
	public void fetchUserDebt(final Callback frontendCallback, boolean refresh)
	throws CredentialsMissingException;
	
	/**
	 *  Searches backend for the supplied string, and reports results using callback.
	 *  
	 *  @param s - The string to search for.
	 *  @param page - which page of search results to fetch.
	 *   If too high, returns empty list.
	 *  @param frontendCallback - the callback object which will be called
	 *   when searching is done.
	 */
	public void search(final String s, final int page, final Callback frontendCallback);
	
	/**
	 *  Fetches the DetailedView of the supplied book.
	 *  Sends a new book with all the additional information to the callback.
	 *  Uses cached values if available; otherwise, simply runs the job.
	 *  
	 *  @param book - the book whose detailed view we're going to fetch.
	 *   Needs to have its url set.
	 *  @param frontendCallback - the callback object which will be called
	 *   when fetching detailed view is done.
	 */
	
	public void fetchDetailedView(final Book book, final Callback frontendCallback);
	
	/**
	 *  Fetches the DetailedView of the supplied book.
	 *  Sends a new book with all the additional information to the callback.
	 *  
	 *  @param book - the book whose detailed view we're going to fetch.
	 *   Needs to have its url set.
	 *  @param frontendCallback - the callback object which will be called
	 *   when fetching detailed view is done.
	 *  @param refresh - should we run the job directly, and bypass caching?
	 */
	
	public void fetchDetailedView(final Book book, final Callback frontendCallback, boolean refresh);
	
	
	/**
	 *  Fetches a list of the user's current reservations.
	 *  Returns it using callback.
	 *  Uses cached values if available; otherwise, simply runs the job.
	 *  
	 *  @param frontendCallback - the callback object which will be
	 *   called when fetching reservations is done.
	 */
	
	public void fetchReservations(final Callback frontendCallback)
			throws CredentialsMissingException;
	
	
	/**
	 *  Fetches a list of the user's current reservations.
	 *  Returns it using callback.
	 *  
	 *  @param frontendCallback - the callback object which will be called
	 *  when fetching reservations is done.
	 *  @param refresh - should we run the job directly, and bypass caching?
	 */
	
	public void fetchReservations(final Callback frontendCallback, boolean refresh)
	throws CredentialsMissingException;
	
	/**
	 *  Fetches a list of the user's currently loaned books. Returns it using callback.
	 *  Uses cached values if available; otherwise, simply runs the job.
	 *  
	 *  @param frontendCallback - the callback object which will be called
	 *   when fetching loans is done.
	 */
	
	public void fetchLoans(final Callback frontendCallback)
			throws CredentialsMissingException;
	
	/**
	 *  Fetches a list of the user's currently loaned books. Returns it using callback.
	 *  
	 *  @param frontendCallback - the callback object which will be called
	 *   when fetching loans is done.
	 *  @param refresh - should we run the job directly, and bypass caching?
	 */
	
	public void fetchLoans(final Callback frontendCallback, boolean refresh)
			throws CredentialsMissingException;
	
	/**
	 *  Reserves the supplied book. Result is reported via callback.
	 *  
	 *  @param book - the book to reserve. Needs to have its reserveUrl property set.
	 *  @param libraryCode - the code of the library the book should be sent to.
	 *  @param frontendCallback - the callback object which will be called
	 *   when reserving is done.
	 */
	
	public void reserve(final Book book, final String libraryCode,final Callback frontendCallback)
	throws CredentialsMissingException;
	

	/**
	 *  Unreserves the supplied book. Result is reported via callback.
	 *  
	 *  @param book - The book to unreserve. Needs to have its unreserveId property set.
	 *  @param frontendCallback - the callback object which will be called
	 *   when unreserving is done.
	 */
	
	public void unreserve(final Book book, final Callback frontendCallback)
	throws CredentialsMissingException;
	
	/**
	 *  Unreserves all supplied books. Result is reported via callback.
	 *  
	 *  @param books - A list of books to unreserve.
	 *   Needs to have their unreserveIds property set.
	 *  @param frontendCallback - the callback object which will be called
	 *   when unreserving is done.
	 */
	
	public void unreserve(final List<Book> books, final Callback frontendCallback)
	throws CredentialsMissingException;
	
	
	/**
	 *  Unreserves all reserved books. Result is reported via callback.
	 *  
	 *  @param frontendCallback - the callback object which will be called
	 *   when unreserving is done.
	 */
	
	public void unreserve(final Callback frontendCallback)
	throws CredentialsMissingException;
	
	/**
	 *  Renews the supplied book. Result is reported via callback.
	 *  
	 *  @param book - The book to renew. Needs to have its renewId property set.
	 *  @param frontendCallback - the callback object which will be called
	 *   when renewal is done.
	 */
	
	public void renew(final Book book, final Callback frontendCallback)
	throws CredentialsMissingException;
	
	/**
	 *  Renews all supplied books. Result is reported via callback.
	 *  
	 *  @param books - A list of books to renew. Needs to have their renewId properties set.
	 *  @param frontendCallback - the callback object which will be called
	 *   when renewal is done.
	 */
	
	public void renew(final List<Book> books, final Callback frontendCallback)
	throws CredentialsMissingException;
	
	/**
	 *  Renews all loaned books. Result is reported via callback.
	 *  
	 *  @param frontendCallback - the callback object which will be called
	 *   when renewal is done.
	 */
	
	public void renew(final Callback frontendCallback)throws CredentialsMissingException;
	
	/**
	 * Logs the user out.
	 */
	
	public void logOut();
	
	/**
	 * Saves credentials in settings
	 * @param cred
	 */
	
	public void saveCredentials(Credentials cred);
	
	/**
	 *  Searches backend for the library information.
	 *  Uses cached values if available; otherwise, simply runs the job.
	 *  
	 *  @param frontendCallback - the callback object which will be called
	 *   when fetching library info is done.
	 */
	
	public void libInfo(final Callback frontendCallback, boolean refresh);
	
	
	
}
