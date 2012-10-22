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

package dat255.grupp06.bibbla.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import dat255.grupp06.bibbla.MainActivity;
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
	public final static String BOOK = "dat255.grupp06.bibbla.BOOK";
	private BookListFragment reservationsList;
	private BookListFragment loansList;
	private boolean dontLogin;
	
	/**
	 * Reference to the class that can produce a login form. Is set on attach.
	 */
	private LoginCallbackHandler loginCallbackHandler;

	private boolean namePending;
	private boolean debtPending;
	private boolean loansPending;
	private boolean reservationsPending;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		if(reservationsList == null) {
			reservationsList = new BookListFragment();
			reservationsList.setReservedListStatus(true);
	        fragmentTransaction.add(R.id.reservations_list, reservationsList);
		} else {
			fragmentTransaction.attach(reservationsList);
		}
		
		if(loansList == null) {
			loansList = new BookListFragment();
			loansList.setLoanedListStatus(true);
	        fragmentTransaction.add(R.id.loans_list, loansList);
		} else {
			fragmentTransaction.attach(loansList);
		}
		
        fragmentTransaction.commit();
        
		return inflater.inflate(R.layout.profile_fragment, container, false);
	}
	
	@Override
	public void onDestroyView() {
		FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach(reservationsList);
        fragmentTransaction.detach(loansList);
        fragmentTransaction.commit();
        super.onDestroyView();
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
		Backend backend = Backend.getBackend();
		
		// These backend calls need user credentials.
		try {
			// The lists take some time so let's use Callback.

			// Name header
			backend.getUserName(new Callback() {
				@Override public void handleMessage(Message msg) {
					getUserNameDone(msg);
			}});
			namePending = true;

			// Current debt
			backend.fetchUserDebt(new Callback() {
				@Override public void handleMessage(Message msg) {
					ProfileFragment.this.fetchDebtDone(msg);
			}});
			debtPending = true;
			
			// Current loans
			backend.fetchLoans(new Callback() {
				@Override public void handleMessage(Message msg) {
					ProfileFragment.this.loansUpdateDone(msg);
			}});
			loansPending = true;
			
			// Current reservations
			backend.fetchReservations(new Callback() {
				@Override public void handleMessage(Message msg) {
					ProfileFragment.this.reservationsUpdateDone(msg);
			}}, true);
			reservationsPending = true;
			
			//updateSpinnerState();
		}
		catch (CredentialsMissingException e) {
			
			if(!dontLogin) {
				loginCallbackHandler.showCredentialsDialog(new Callback() {
					@Override public void handleMessage(Message msg) {
						updateFromBackend();
				}});
			} else {
				Handler handler = new Handler();
				handler.post(new Runnable() {
					@Override
					public void run() {
						((MainActivity)getSherlockActivity()).selectSearchTab();
					}
				});
			}
		}
	}
	
	private void getUserNameDone(Message msg) {
		Activity activity = getSherlockActivity();
		String name = (String) msg.obj;
		TextView nameHeading = (TextView) activity.findViewById(R.id.name_heading);
		if (nameHeading != null) nameHeading.setText(name);
		namePending = false;
		//updateSpinnerState();
	}
	
	/**
	 * Update the debt TextView.
	 * @param msg Backend response, 
	 */
	private void fetchDebtDone(Message msg) {
		Activity activity = getSherlockActivity();
		int debt = (Integer) msg.obj;
		TextView debtView = (TextView) activity.findViewById(R.id.debt_view);
		if (debtView != null) debtView.setText(String.
				format(getString(R.string.debt_view_text), debt));
		debtPending = false;
		//updateSpinnerState();
	}
	
	/**
	 * Update ListView of loans with results.
	 * @param msg Backend response
	 */
	private void loansUpdateDone(Message msg) {
		Activity activity = getSherlockActivity();
		try {
			@SuppressWarnings("unchecked")
			ArrayList<Book> loans = (ArrayList<Book>) msg.obj;
			loansList.updateList(loans);
		} catch (ClassCastException e) {
			Toast.makeText(activity, R.string.loans_list_error, Toast.LENGTH_SHORT).show();
		}
		loansPending = false;
		//updateSpinnerState();
	}
	
	/**
	 * Update ListView of reservations with results.
	 * @param msg Backend response
	 */
	private void reservationsUpdateDone(Message msg) {
		Activity activity = getSherlockActivity();
		try {
			@SuppressWarnings("unchecked")
			ArrayList<Book> reservations = (ArrayList<Book>) msg.obj;
			reservationsList.updateList(reservations);			
		} catch (ClassCastException e) {
			Toast.makeText(activity, R.string.reservations_list_error, Toast.LENGTH_SHORT).show();
		}
		reservationsPending = false;
		//updateSpinnerState();
	}

	/** Hide loading spinner if all fetching is done. */
	private void updateSpinnerState() {
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(
				namePending||debtPending||loansPending||reservationsPending);
	}

	public void cancelUpdate() {
		namePending = debtPending = loansPending = reservationsPending = false;
		updateSpinnerState();
	}
	
	public void toggleLoans() {
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		ft.detach(reservationsList);
	}
	
	public void setDontLogin(boolean choice) {
		dontLogin = choice;
	}
	
}