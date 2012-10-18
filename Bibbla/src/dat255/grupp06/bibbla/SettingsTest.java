package com.example.settings;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Toast;
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.activity_list_item);
        loadPreferneces();
    }
      
    private void loadPreferneces(){
    	SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
    	String Code = sharedPreferences.getString("Code", "");
    	String Pin = sharedPreferences.getString("Pin", "");
    	savePrefernces("Code", "62565236523527");
    	savePrefernces("Pin", "1234");
    	
    	Code = sharedPreferences.getString("Code", "");
    	Pin = sharedPreferences.getString("Pin", "");
    	Toast.makeText(this,"Code: "+ Code, Toast.LENGTH_LONG).show();
    	Toast.makeText(this,"Pin: "+Pin, Toast.LENGTH_LONG).show();
    }
    
    private void savePrefernces(String key, String value){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();      
    }
   
}
