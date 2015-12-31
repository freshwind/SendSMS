package com.example.szhangcs.sendsms;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

/**
 * Created by szhangcs on 12/29/15.
 */
public class SmsData {
    // contactName will be null when phone number is not in contact book
    private String contactName;
    private String phoneNumber;
    private String smsData;
    private Date date;
    private boolean isReplyed;
    private boolean isGreeting;
    private boolean isSelected;

    public SmsData () {
    }

    public SmsData (String contractName,
                     String phoneNumber,
                     String smsData,
                     boolean isReplyed) {
        this.contactName = contractName;
        this.phoneNumber = phoneNumber;
        this.smsData = smsData;
        this.isReplyed = isReplyed;
        this.isGreeting = isGreeting();
        if (isReplyed && !isGreeting) {
            isSelected = false;
        } else {
            isSelected = true;
        }
    }

    public SmsData (SmsData input) {
        this.contactName = input.getContactName();
        this.phoneNumber = input.getPhoneNumber();
        this.smsData = input.getSmsData();
        this.date = input.getDate();
        this.isReplyed = input.isReplyed();
        this.isGreeting = input.isGreeting();
        this.isSelected = input.isSelected();
    }

    public String getContactName() {
        return contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSmsData() {
        return smsData;
    }

    public Date getDate() {
        return date;
    }

    // TODO(3feng:P1) should implement this function, need to store data in this app
    public boolean isReplyed() {
        return isReplyed;
    }

    // TODO(3feng:P2) should implement this function
    private boolean isGreeting() {
        return isGreeting;
    }

    public boolean isSelected() {
        return isSelected;
    }

    private void setContactName(String contactName) {
        this.contactName = contactName;
    }

    private void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private void setSmsData(String smsData) {
        this.smsData = smsData;
    }

    private void setReplyed(boolean isReplyed) {
        this.isReplyed = isReplyed;
    }

    private void setDate(Date date) {
        this.date = date;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
