package edu.stevens.cs522.chat.rest;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonReader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.UUID;

import edu.stevens.cs522.chat.entities.ChatMessage;

/**
 * Created by dduggan.
 */

public class SynchronizeRequest extends Request implements Parcelable {

    public ChatMessage message;
    public String chatName;
    public UUID clientID;
    // Added by request processor
    public long lastSequenceNumber;

    public SynchronizeRequest(ChatMessage message) {
        super();
        this.message = message;
    }

    @Override
    public String getRequestEntity() throws IOException {
        // We stream output for SYNC, so this always returns null
        return null;
    }

    @Override
    public Response getResponse(HttpURLConnection connection, JsonReader rd) throws IOException{
        assert rd == null;
        return new SynchronizeResponse(connection);
    }

    @Override
    public Response process(RequestProcessor processor) {
        return processor.perform(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO
        //super.writeToParcel(dest,flags);
        dest.writeParcelable(this.message, flags);
        //this.message.writeToParcel(dest,flags);
        dest.writeLong(this.lastSequenceNumber);
    }

    public SynchronizeRequest() {
        super();
    }

    public SynchronizeRequest(String chatName, UUID clientID) {
        super(chatName,clientID);
        this.chatName=chatName;
        this.clientID=clientID;
    }

    public SynchronizeRequest(Parcel in) {
        super();
        this.message = in.readParcelable(ChatMessage.class.getClassLoader());
        this.lastSequenceNumber = in.readLong();
        // TODO
    }

    public static Creator<SynchronizeRequest> CREATOR = new Creator<SynchronizeRequest>() {
        @Override
        public SynchronizeRequest createFromParcel(Parcel source) {
            return new SynchronizeRequest(source);
        }

        @Override
        public SynchronizeRequest[] newArray(int size) {
            return new SynchronizeRequest[size];
        }
    };

}
