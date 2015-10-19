package sas.utils;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * @author Wei Runqi
 * @date 15/7/17
 */

public class ContactsUtils {

    public static Cursor loadContactsByMobile(Activity activity, String mobile) {

        Uri uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(mobile)
        );

        return activity.getContentResolver().query(
                uri,
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                null,
                null,
                null
        );
    }

}
