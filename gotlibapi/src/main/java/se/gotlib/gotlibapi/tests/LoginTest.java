package se.gotlib.gotlibapi.tests;

import se.gotlib.gotlibapi.SecretCredentials;
import se.gotlib.gotlibapi.jobs.LoginJob;
import se.gotlib.gotlibapi.model.GotlibCredentials;
import se.gotlib.gotlibapi.model.GotlibSession;
import se.gotlib.gotlibapi.util.Message;

/**
 * Created by Niklas on 2015-03-17.
 */
public class LoginTest {

    public static void run() {
        GotlibCredentials c = new GotlibCredentials(SecretCredentials.name, SecretCredentials.code, SecretCredentials.pin);

        LoginJob j = new LoginJob(c);
        Message<GotlibSession> result =  j.login();
        System.out.println(result);
    }
}
