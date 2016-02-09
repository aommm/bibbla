package se.gotlib.bibbla.backend.tasks;

import android.util.Log;

//import org.jsoup.Connection.Method;
//import org.jsoup.Connection.Response;
//import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by Niklas on 2014-10-28.
 */
public class TestTask extends Task<Void, Void, String> {


    @Override
    protected String doInBackground(Void... voids) {

/*
        // Create a request, and retrieve the response.
        Response response = null;
        try {
            response = Jsoup.connect("http://google.com")
                    .method(Method.GET)
                    .timeout(3000)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null)
            return response.statusMessage();
        return null;
*/
    return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("bibbla", "onPostExecute, result: "+result);
        fireEvent("testJsoupDone", result);
    }
}
