package se.gotlib.bibbla.util;

/**
 * Created by Niklas on 2014-10-28.
 */
public class ErrorException extends Exception {
    private Error e;
    public ErrorException(Error e) {
        this.e = e;
    }
    public Error getError() {
        return e;
    }
}
