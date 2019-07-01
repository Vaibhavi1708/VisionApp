package com.example.vision.models;

public class MessageModel {

    private String m_id;
    private String mPhone;
    private String mMessage;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return m_id;
    }

    public void set_id(String _id) {
        this.m_id = _id;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setAddress(String phone) {
        this.mPhone = phone;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public MessageModel(String _id, String phone, String message) {

        m_id = _id;
        mPhone = phone;
        mMessage = message;

    }

    public MessageModel(String phone,String message){
        this.mPhone=phone;
        this.mMessage=message;
    }


}
