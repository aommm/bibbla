package dat255.grupp06.bibbla.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import dat255.grupp06.bibbla.R;
import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;

/**
 * Main fragment for the profile or "me" tab.
 * @author arla
 */
public class LoginFragment extends SherlockFragment {
	
	Backend backend;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.login_fragment, container, false);
	}
	
	// TODO Extract backend field to super class, or keep it in MainActivity
	public void setBackend(Backend backend) {
		this.backend = backend;
	}
	
	public void login(String name, String card, String pin) {
		Callback loginCallback = new Callback() {
			@Override
			public void handleMessage(Message msg) {
				LoginFragment.this.loginDone(msg);
			}
		};
		backend.login(loginCallback);
	}
	
	public void loginDone(Message msg) {
		// switch to other fragment???
		if (msg.loggedIn) {
			//getActivity().showProfileTab() or something
			android.util.Log.d("A", "Enter Logged-in Profile fragment.");
		} else {
			Toast.makeText(getSherlockActivity(), R.string.login_fail_msg,
				Toast.LENGTH_SHORT).show();
		}
		
	}
}
