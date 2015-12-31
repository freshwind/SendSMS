package com.example.szhangcs.sendsms;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by szhangcs on 12/29/15.
 */
class ReceiverRowAdapter extends BaseAdapter {

    Context context;
    List<SmsData> data;
    private static LayoutInflater inflater = null;

    public ReceiverRowAdapter(Context context, List<SmsData> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.list_row, null);
        final SmsData smsData = data.get(position);
        TextView nameText = (TextView) vi.findViewById(R.id.select_receiver_row_name);
        TextView isReplyedText = (TextView) vi.findViewById(R.id.select_receiver_row_replyed);
        TextView smsText = (TextView) vi.findViewById(R.id.select_receiver_row_data);
        CheckBox checkBox = (CheckBox) vi.findViewById(R.id.select_receiver_row_checkbox);

        // set name
        String name = smsData.getContactName();
        if (name != null && !name.isEmpty()) {
            nameText.setText(name);
        } else {
            nameText.setText(smsData.getPhoneNumber());
        }

        // set isReplyed
        if (smsData.isReplyed()) {
            isReplyedText.setText("已回复");
        } else {
            isReplyedText.setText("");
        }

        // set sms
        smsText.setText(smsData.getSmsData());

        // set checkBox
        if (smsData.isSelected()) {
            checkBox.setChecked(true);
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    smsData.setIsSelected(true);
                    Log.d("3feng", "The check box is selected and smsData selected, phonenumber = " + smsData.getPhoneNumber());
                } else {
                    smsData.setIsSelected(false);
                    Log.d("3feng", "The check box is un selected and smsData is unselected, phonenumber = " + smsData.getPhoneNumber());
                }
            }
        });
        return vi;
    }
}
