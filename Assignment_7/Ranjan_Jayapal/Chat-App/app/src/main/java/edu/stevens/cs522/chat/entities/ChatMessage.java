package edu.stevens.cs522.chat.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import edu.stevens.cs522.chat.contracts.MessageContract;

/**
 * Created by dduggan.
 */

public class ChatMessage implements Parcelable {

    // Primary key in the database
    public long id;

    // Global id provided by the server
    public long seqNum;

    public String messageText;

    public String chatRoom;

    // When and where the message was sent
    public Date timestamp;

    public Double longitude;

    public Double latitude;

    // Sender username and FK (in local database)
    public String sender;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public ChatMessage() {
        longitude = 0.0;
        latitude = 0.0;
        seqNum = 0;
    }

    //  add operations for parcels (Parcelable), cursors and contentvalues
    protected ChatMessage(Parcel in) {
        this.id = in.readLong();
        this.seqNum = in.readLong();
        this.messageText = in.readString();
        this.chatRoom = in.readString();
        this.timestamp = new Date(in.readLong());
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.sender = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.seqNum);
        dest.writeString(this.messageText);
        dest.writeString(this.chatRoom);
        dest.writeLong(this.timestamp.getTime());
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.sender);
    }

    public ChatMessage(Cursor cursor) {
        //
        this.id = Long.parseLong(MessageContract.getId(cursor));
        this.seqNum = MessageContract.getSequenceNumber(cursor);
        this.messageText = MessageContract.getMessageText(cursor);
        this.chatRoom = MessageContract.getChatRoom(cursor);
        this.timestamp = MessageContract.getTimestamp(cursor);
        this.longitude = MessageContract.getLongitude(cursor);
        this.latitude = MessageContract.getLatitude(cursor);
        this.sender = MessageContract.getSender(cursor);
    }

    public static final Parcelable.Creator<ChatMessage> CREATOR = new Parcelable.Creator<ChatMessage>() {
        public ChatMessage createFromParcel(Parcel source) {
            return new ChatMessage(source);
        }

        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };

    public void writeToProvider(ContentValues values) {
        MessageContract.putSequenceNumberColumn(values, this.seqNum);
        MessageContract.putMessageText(values, messageText);
        MessageContract.putChatRoom(values, this.chatRoom);
        MessageContract.putTimestamp(values, this.timestamp);
        MessageContract.putLatitude(values, latitude);
        MessageContract.putLongitude(values, longitude);
        MessageContract.putSender(values, sender);
    }
}