package com.example.whatsapp.Model;

public class Messages {
    String uID , message , recieverId,messageId;
    Long timestamp;

    public Messages(String uID,String recieverId, String message, Long timestamp) {
        this.uID = uID;
        this.recieverId = recieverId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Messages(String uID,String recieverId, String message) {
        this.uID = uID;
        this.recieverId = recieverId;
        this.message = message;
    }

    public Messages(String uID, String message) {
        this.uID = uID;

        this.message = message;
    }

    public Messages( ) {
        //Empty constructore
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
