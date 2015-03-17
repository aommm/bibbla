package se.gotlib.gotlibapi.util;

import se.gotlib.gotlibapi.util.Error;

/**
 * Created by Niklas on 2014-10-28.
 */
public class ErrorException extends Exception {
    private Error e;
    public ErrorException(se.gotlib.gotlibapi.util.Error e) {
        this.e = e;
    }
    public Error getError() {
        return e;
    }
}
