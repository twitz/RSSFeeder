package ml223vz.dv606.rssfeeder.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * TODO: Make this class more useful...
 */
public class NetworkUtil {

    /**
     * Returns true if there is an internet connection.
     *
     * @param context of the calling application
     * @return true or false
     */
    public static boolean checkInternet(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
