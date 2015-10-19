package sas.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import sas.base.BaseFragment;
import sas.app.sms.R;
import sas.utils.ContactsUtils;
import sas.provider.SmsProvider;


/**
 * A placeholder fragment containing a simple view.
 */
public class MessageListFragment extends BaseFragment {


    View rootView;
    ListView listView;

    SmsProvider smsProvider;
    Cursor messageCursor;
    Cursor conservationCursor;

    public MessageListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.message_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rootView = view;

        initListView();
    }

    @Override
    public void onResume() {
        super.onResume();

        initListView();
}

    protected void initListView() {
        if (rootView == null) {
            return;
        }

        if (listView == null) {
            listView = (ListView) rootView.findViewById(R.id.message_list);
        }

        loadCursors();
        listView.setAdapter(new MyAdapter(conservationCursor));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms://"));
                sendIntent.putExtra("address", "123456789");
                sendIntent.putExtra("sms_body", "foo bar");
                startActivity(sendIntent);
            }
        });
    }


    protected void loadCursors() {

        if (smsProvider == null) {
            smsProvider = new SmsProvider(getActivity());
        }

        if (messageCursor == null || messageCursor.isClosed()) {
            messageCursor = smsProvider.loadMessageCursor();
        }
        if (conservationCursor == null || conservationCursor.isClosed()) {
            conservationCursor = smsProvider.loadConservationCursor();
        }
    }

    protected void closeCursors() {
        if (messageCursor != null) {
            messageCursor.close();
        }
        if (conservationCursor != null) {
            conservationCursor.close();
        }
    }

    class MyAdapter extends BaseAdapter {

        Cursor mCursor;

        public MyAdapter(Cursor cursor) {
            this.mCursor = cursor;
        }

        protected String getText(Cursor cursor) {
            if (cursor == null || cursor.isClosed()) {
                return "";
            }

            String text = "";
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                text += String.format("%d, %s : %s \n", i, cursor.getColumnName(i), cursor.getString(i));
            }

            return text;
        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        protected void moveTo(int position) {

            if (mCursor.getPosition() != position) {
                mCursor.moveToPosition(position);
            }

        }

        @Override
        public Object getItem(int position) {

            return getText(mCursor);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv;

            if (convertView instanceof TextView) {
                tv = (TextView) convertView;
            } else {
                tv = new TextView(getActivity());
            }

            moveTo(position);
            String phoneNumber = mCursor.getString(mCursor.getColumnIndex(Telephony.TextBasedSmsColumns.ADDRESS));

            Cursor nameCursor = ContactsUtils.loadContactsByMobile(getActivity(), phoneNumber);
            String displayName;
            if (nameCursor.getCount() > 0) {
                nameCursor.moveToFirst();
                 displayName = getText(nameCursor);
            } else {
                displayName = phoneNumber;
            }
            tv.setText(displayName + "\n" + (String) getItem(position));

            nameCursor.close();
            return tv;

        }
    }

    @Override
    public void onStop() {

        closeCursors();
        super.onStop();
    }


}
