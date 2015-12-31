package com.example.szhangcs.sendsms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectReceiver extends AppCompatActivity {
    final String SORT_ORDER = "date DESC";
    Date startDate;
    List<SmsData> receivers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_receiver);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setStartDate();
        ListView listView = (ListView) findViewById(R.id.selectReceiverListView);
        receivers = getReceivers();
        for (SmsData receiver : receivers) {
            Log.d("3feng", "here!" + receiver.getPhoneNumber() + ":" + receiver.isSelected());
        }
        ReceiverRowAdapter adapter = new ReceiverRowAdapter(this, receivers);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                SmsData smsData = (SmsData) parent.getItemAtPosition(position);
                if (view != null) {
                    CheckBox checkBox = (CheckBox) view.findViewById(
                            R.id.select_receiver_row_checkbox);
                    checkBox.setChecked(!checkBox.isChecked());
                    smsData.setIsSelected(checkBox.isChecked());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBack key pressed");
        Intent intent = new Intent();
        ArrayList<String> selectedReceivers = new ArrayList<>();
        for (SmsData receiver : receivers) {
            if (receiver.isSelected()) {
                selectedReceivers.add(new String(receiver.getPhoneNumber()));
            }
        }
        Log.d("CDA", new Integer(selectedReceivers.size()).toString());
        intent.putStringArrayListExtra("receiver_phone_numbers", selectedReceivers);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Log.d("CDA", "on up key pressed");
                Intent intent = new Intent();
                ArrayList<String> selectedReceivers = new ArrayList<>();
                for (SmsData receiver : receivers) {
                    if (receiver.isSelected()) {
                        selectedReceivers.add(new String(receiver.getPhoneNumber()));
                    }
                }
                Log.d("CDA", new Integer(selectedReceivers.size()).toString());
                intent.putStringArrayListExtra("receiver_phone_numbers", selectedReceivers);
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // TODO(3feng:P3) let users set the number of days to filter
    private void setStartDate() {
        // currently will use 7 days before current date
        // Will add a tag in configuration later
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        startDate = cal.getTime();
    }

    private List<SmsData> getReceivers() {
        List<SmsData> smsList = getSmsHistory();
        Map<String, SmsData> mapPhoneNumberToData = new HashMap<>();
        for (SmsData sms : smsList) {
            if (mapPhoneNumberToData.containsKey(sms.getPhoneNumber())) {
                SmsData mappedData = mapPhoneNumberToData.get(sms.getPhoneNumber());
                if (!mappedData.isSelected()) {
                    mapPhoneNumberToData.put(sms.getPhoneNumber(), sms);
                }
            } else {
                mapPhoneNumberToData.put(sms.getPhoneNumber(), sms);
            }
        }
        return new ArrayList<>(mapPhoneNumberToData.values());
    }

    // Will return a list of short messages that user received in 10 days
    private List<SmsData> getSmsHistory() {
        List<SmsData> smsRecords = new ArrayList<>();

        // get a contract map from phone number to name
        Map<String, String> contactMap = getContactList();

        // prepare sharedPreference to check isReplyed
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference),
                Context.MODE_PRIVATE);

        // search for candidate sms
        String filter = "date>=" + startDate.getTime();
        Cursor smsInboxCursor = getContentResolver().query(
                Uri.parse("content://sms/inbox"), null, filter, null, SORT_ORDER);
        final int indexBody = smsInboxCursor.getColumnIndex("body");
        final int indexAddress = smsInboxCursor.getColumnIndex("address");
        final int indexDate = smsInboxCursor.getColumnIndex("date");
        if (smsInboxCursor.moveToFirst()) { // if sms inbox is not empty
            for (int i = 0; i < smsInboxCursor.getCount(); i++) {
                String body = smsInboxCursor.getString(indexBody);
                String number = smsInboxCursor.getString(indexAddress);
                String dateString = smsInboxCursor.getString(indexDate);
                Date date = new Date(Long.valueOf(dateString));
                String name = contactMap.get(number);
                smsRecords.add(new SmsData(name, number, body, sharedPreferences.getBoolean(number, false)));
                smsInboxCursor.moveToNext();
            }
        }
        smsInboxCursor.close();
        return smsRecords;
    }

    // Map of contact list
    // Key = phoneNumber, Value = userName
    public Map<String, String> getContactList() {
        Map<String, String> result = new HashMap<String, String>();
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        final int indexPhone = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        final int indexName = cursor.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        while(cursor.moveToNext()) {
            String phone = cursor.getString(indexPhone);
            String name = cursor.getString(indexName);
            result.put(phone, name);
        }
        cursor.close();
        return result;
    }

//    private void initilizeReplyRecord() {
//        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
//        if (!sharedPref.getBoolean(getString(R.string.is_first_time), false)) {
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putBoolean(getString(R.string.is_first_time), true);
//
//            Cursor cursor = getContentResolver().query(
//                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
//            final int indexPhone = cursor.getColumnIndex(
//                    ContactsContract.CommonDataKinds.Phone.NUMBER);
//            while(cursor.moveToNext()) {
//                String phone = cursor.getString(indexPhone);
//                editor.putBoolean(phone, true);
//            }
//            editor.commit();
//        }
//
//    }
}
