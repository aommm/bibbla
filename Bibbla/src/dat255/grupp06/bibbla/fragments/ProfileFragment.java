/**
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

package dat255.grupp06.bibbla.fragments;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import dat255.grupp06.bibbla.R;
import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.frontend.LoginCallbackHandler;
import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.model.CredentialsMissingException;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;

/**
 * Main fragment for the profile or "me" tab. You must give a reference to the
 * Backend object before using the fragment.
 * @author arla
 */
public class ProfileFragment extends SherlockFragment {

	Backend backend;
	
	/**
	 * Reference to the class that can produce a login form. Is set on attach.
	 */
	private LoginCallbackHandler loginCallbackHandler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.profile_fragment, container, false);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			loginCallbackHandler = (LoginCallbackHandler) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() +
					"must implement LoginCallbackHandler");
		}
	}
	
	/**
	 * Give a reference to the Backend
	 * @param backend The Backend object used by the application
	 */
	public void setBackend(Backend backend) {
		this.backend = backend;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		updateFromBackend();
	}
	
	/**
	 * Update the contents of GUI elements with information from Backend, such
	 * as name, current debt, books on loan and pending reservations.
	 * @throws IllegalStateException if the backend is not set.
	 * @see setBackend(Backend)
	 */
	public void updateFromBackend() throws IllegalStateException {
		if (backend == null)
			throw new IllegalStateException();
		
		// These backend calls need user credentials.
		try {
			// The lists take some time so let's use Callback.
			// TODO Loading spinner

			// Name header
			backend.getUserName(new Callback() {
				@Override public void handleMessage(Message msg) {
					getUserNameDone(msg);
			}});

			// Current debt
			backend.fetchUserDebt(new Callback() {
				@Override public void handleMessage(Message msg) {
					ProfileFragment.this.fetchDebtDone(msg);
			}});
			
			// Current loans
			backend.fetchLoans(new Callback() {
				@Override public void handleMessage(Message msg) {
					ProfileFragment.this.loansUpdateDone(msg);
			}});
			
			// Current reservations
			backend.fetchReservations(new Callback() {
				@Override public void handleMessage(Message msg) {
					ProfileFragment.this.reservationsUpdateDone(msg);
			}});
		}
		catch (CredentialsMissingException e) {
			loginCallbackHandler.showCredentialsDialog(new Callback() {
				@Override public void handleMessage(Message msg) {
					updateFromBackend();
			}});
		}
	}
	
	private void getUserNameDone(Message msg) {
		Activity activity = getSherlockActivity();
		String name = (String) msg.obj;
		TextView nameHeading = (TextView) activity.findViewById(R.id.name_heading);
		nameHeading.setText(name);
	}
	
	/**
	 * Update the debt TextView.
	 * @param msg Backend response, 
	 */
	private void fetchDebtDone(Message msg) {
		Activity activity = getSherlockActivity();
		int debt = (Integer) msg.obj;
		TextView debtView = (TextView) activity.findViewById(R.id.debt_view);
		debtView.setText(String.format(getString(R.string.debt_view_text), debt));
	}
	
	/**
	 * Update ListView of loans with results.
	 * @param msg Backend response
	 */
	private void loansUpdateDone(Message msg) {
		Activity activity = getSherlockActivity();
		try {
			@SuppressWarnings("unchecked")
			List<Book> loans = (List<Book>) msg.obj;
			ListView loansList = (ListView) activity.findViewById(R.id.loans_list);
			if (loansList != null)
				loansList.setAdapter(new BookListAdapter(activity, loans, false)); //
		} catch (ClassCastException e) {
			Toast.makeText(activity, R.string.loans_list_error, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Update ListView of reservations with results.
	 * @param msg Backend response
	 */
	private void reservationsUpdateDone(Message msg) {
		Activity activity = getSherlockActivity();
		try {
			@SuppressWarnings("unchecked")
			List<Book> reservations = (List<Book>) msg.obj;
			ListView reservationsList = (ListView) activity.findViewById(R.id.reservations_list);
			if (reservationsList != null)
				reservationsList.setAdapter(new BookListAdapter(activity, reservations, false));
		} catch (ClassCastException e) {
			Toast.makeText(activity, R.string.reservations_list_error, Toast.LENGTH_SHORT).show();
		}
		
	}
}
