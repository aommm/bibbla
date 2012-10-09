package dat255.grupp06.bibbla.fragments;

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
		
		// Name header
		String name = backend.getUserName();
		TextView nameHeading = (TextView) getActivity().findViewById(R.id.name_heading);
		nameHeading.setText(name);
		// Current debt
		// Loans list
		// Reservations list
	}
}
