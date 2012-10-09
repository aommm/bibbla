package dat255.grupp06.bibbla.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

import dat255.grupp06.bibbla.R;

/**
 * Main fragment for the profile or "me" tab.
 * @author arla
 */
public class LoginFragment extends SherlockFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.search_fragment, container, false);
	}
}
