package com.example.szhangcs.sendsms;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SelectMessageActivity extends AppCompatActivity {
    // TODO(3feng:P1) add more messages here
    String[] messages = {"新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐新年快乐!",
            "新年快乐"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ListView listView = (ListView) findViewById(R.id.selectMessageListView);
        ArrayAdapter<String> messagesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, messages);
        listView.setAdapter((messagesAdapter));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (view != null) {
                    String message = (String) parent.getItemAtPosition(position);
                    Intent intent = new Intent();
                    intent.putExtra("message", message);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBack key pressed");
        Intent intent = new Intent();
        intent.putExtra("message", "");
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
                intent.putExtra("message", "");
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
