package groep_2.app4school.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import groep_2.app4school.Account;
import groep_2.app4school.MainActivity;

public class Util {

    private Context mContext;
    public Util(Context con) {
        mContext = con;
    }
    public static String encodeEmail(String userEmail) {
//        if (userEmail == null){
////            Intent intent = new Intent(MainActivity.class, Account.class)
////            startActivity(intent);
//            return  userEmail = "no";
//        } else {
            return userEmail.replace(".", ",");
//        }
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
