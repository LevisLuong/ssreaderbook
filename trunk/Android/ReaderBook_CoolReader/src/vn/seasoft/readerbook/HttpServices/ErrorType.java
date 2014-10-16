package vn.seasoft.readerbook.HttpServices;

import android.content.Context;
import org.holoeverywhere.widget.Toast;
import vn.seasoft.readerbook.R;

public class ErrorType {
    public static final int ERROR_TIMEOUT = 101;
    public static final int NO_NETWORK_CONNECTION = 102;

    public static final int ERROR_NOTFOUND = 404;
    public static final int ERROR_NOTALLOWED = 405;
    public static final int ERROR_INTERNALSERVERERROR = 500;

    public static void getErrorMessage(final Context c, final int errortype) {
        switch (errortype) {
            case ERROR_TIMEOUT:
                Toast.makeText(c, R.string.error_timeout, Toast.LENGTH_SHORT).show();
                break;
            case NO_NETWORK_CONNECTION:
                Toast.makeText(c, R.string.error_connect, Toast.LENGTH_SHORT).show();
                break;
            case ERROR_INTERNALSERVERERROR:
                Toast.makeText(c, R.string.error_server, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}