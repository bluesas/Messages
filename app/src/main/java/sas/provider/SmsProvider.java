package sas.provider;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;

/**
 * @author Wei Runqi
 * @date 15/7/17
 */

public class SmsProvider {

    public static final String SMS_INBOX = "content://sms/inbox";
    public static final String SMS_SENT = "content://sms/sent";
    public static final String SMS_DRAFT = "content://sms/draft";
    public static final String SMS = "content://sms";
    public static final String CONVERSATIONS = "content://sms/conversations";

    Activity activity;

    String[] messageProjection = {
            Telephony.TextBasedSmsColumns.THREAD_ID,
            Telephony.TextBasedSmsColumns.TYPE,
            Telephony.TextBasedSmsColumns.ADDRESS,
            Telephony.TextBasedSmsColumns.BODY,
            Telephony.TextBasedSmsColumns.DATE,
            Telephony.TextBasedSmsColumns.DATE_SENT,
            Telephony.TextBasedSmsColumns.PERSON,
    };

    String[] conversationProjection = {
            Telephony.TextBasedSmsColumns.THREAD_ID,
            Telephony.TextBasedSmsColumns.TYPE,
            Telephony.TextBasedSmsColumns.ADDRESS,
            Telephony.TextBasedSmsColumns.BODY,
            Telephony.TextBasedSmsColumns.DATE,
            Telephony.TextBasedSmsColumns.DATE_SENT,
            Telephony.TextBasedSmsColumns.PERSON,
            Telephony.TextBasedSmsColumns.READ,
            String.format("max(%s)", Telephony.TextBasedSmsColumns.DATE),
    };

    public SmsProvider(Activity activity) {
        this.activity = activity;
    }

    public Cursor loadMessageCursor() {

        return activity.getContentResolver().query(
                Uri.parse(SMS), messageProjection, null, null, null
        );

    }

    public Cursor loadDraftMessageCursor() {

        String selection = String.format("%s = %s",
                Telephony.TextBasedSmsColumns.TYPE,
                Telephony.TextBasedSmsColumns.MESSAGE_TYPE_DRAFT);

        return activity.getContentResolver().query(
                Uri.parse(SMS), messageProjection, selection, null, null
        );

    }

    public Cursor loadConservationCursor() {

        String selection = " 0 = 0";
        String groupby = String.format("GROUP BY ( %s", conversationProjection[0]);
        selection = selection + ") " + groupby;

        String order = String.format("%s DESC, %s DESC ",
                conversationProjection[7], conversationProjection[0]);

        return activity.getContentResolver().query(
                Uri.parse(SMS), conversationProjection, selection, null, order
        );

    }

    public Cursor loadCurservations() {
        return activity.getContentResolver().query(Uri.parse(CONVERSATIONS), null, null, null, null);
    }
}
