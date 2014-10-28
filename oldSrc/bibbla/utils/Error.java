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

package dat255.grupp06.bibbla.utils;

public enum Error {
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
	MISSING_CREDENTIALS, // Some user credentials are missing.
	LOGIN_NEEDED, // Used by other jobs, if they detect they're not logged in.

	// Session.fetchUserUrl() returned an empty string.
	// May indicate incorrect details, no network, you name it.
	FETCHING_USER_URL_FAILED	 
}