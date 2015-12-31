package com.example.szhangcs.sendsms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.szhangcs.sendsms";
    List<String> receivers;
    private static String messageContext = "Test Greeting Context";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1 : // receive data from select receiver activity
                if (resultCode == RESULT_OK) {
                    receivers = data.getExtras().getStringArrayList("receiver_phone_numbers");
                }
                break;
            case 2: // receive data from select message activity
                if (resultCode == RESULT_OK) {
                    String message = data.getExtras().getString("message");
                    if (message != null && !message.isEmpty()) {
                        EditText editText = (EditText) findViewById(R.id.edit_message);
                        editText.setText(message);
                    }
                }
                break;
        }
    }

    public void selectMessage(View view) {
        Intent intent = new Intent(this, SelectMessageActivity.class);
        startActivityForResult(intent, 2);
    }

    // TODO(3feng:P2) show progress bar when sending sms
    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_message);
        messageContext = editText.getText().toString();
        if (messageContext.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "请输入短信内容或选择祝福短信!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (receivers == null || receivers.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "请选择收件人!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        for (String destinationPhoneNumber : receivers) {
            sendSMS(destinationPhoneNumber);
            editor.putBoolean(destinationPhoneNumber, true);
            Log.d("3feng", "set " + destinationPhoneNumber + "as true");
        }
        editor.apply();
        Intent intent = new Intent(this, DisplayMessageActivity.class);

        intent.putExtra(EXTRA_MESSAGE, messageContext);
        startActivity(intent);
    }

    public void selectReceiver(View view) {
        Intent intent = new Intent(this, SelectReceiver.class);
        startActivityForResult(intent, 1);
    }

    private boolean sendSMS(String destination) {
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> messageContextArray = smsManager.divideMessage(messageContext);
        smsManager.sendMultipartTextMessage(destination, null, messageContextArray, null, null);
        return true;
    }
}
