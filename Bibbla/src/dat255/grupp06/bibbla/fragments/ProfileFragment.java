package dat255.grupp06.bibbla.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import dat255.grupp06.bibbla.R;
import dat255.grupp06.bibbla.backend.Backend;

/**
 * Main fragment for the profile or "me" tab.
 * @author arla
 */
public class ProfileFragment extends SherlockFragment {

	Backend backend;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.profile_fragment, container, false);
	}
	
	public void setBackend(Backend backend) {
		this.backend = backend;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		updateFromBackend();
	}
	
	private void updateFromBackend() {
		if (backend == null)
			throw new IllegalStateException();
		
		Activity activity = getSherlockActivity();
		// Name header
		String name = backend.getUserName();
		TextView nameHeading = (TextView) activity.findViewById(R.id.name_heading);
		nameHeading.setText(name);
		// Current debt
		int debt = backend.getUserDebt();
		TextView debtView = (TextView) activity.findViewById(R.id.debt_view);
		debtView.setText(String.format(getString(R.string.debt_view_text), debt));
		// Loans list
//		List<Book> loans = backend.getLoans();
//		ListView loansList = (ListView) activity.findViewById(R.id.loans_list);
//		loansList.setSomething(loans.lahdidah()));
		// Reservations list
//		List<Book> reservations = backend.getReservations();
//		ListView reservationsList = (ListView) activity.findViewById(R.id.reservations_list);
//		loansList.setSomething(loans.lahdidah()));
	}
}
