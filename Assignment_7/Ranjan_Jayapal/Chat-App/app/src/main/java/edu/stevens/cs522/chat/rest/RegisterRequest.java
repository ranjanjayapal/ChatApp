package edu.stevens.cs522.chat.rest;

import android.os.Parcel;
import android.util.JsonReader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.UUID;

/**
 * Created by dduggan.
 */

public class RegisterRequest extends Request {

    public RegisterRequest(String chatName, UUID clientID) {
        super(chatName, clientID);
    }


    @Override
    public String getRequestEntity() throws IOException {
        return null;
    }

    @Override
    public Response getResponse(HttpURLConnection connection, JsonReader rd) throws IOException {
        //return new RegisterResponse(connection, rd);
        return new RegisterResponse(connection);
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
        dest.writeString(this.chatName);
        dest.writeSerializable(this.clientID);
        dest.writeSerializable(this.timestamp);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
    }

    public RegisterRequest(Parcel in) {
        //super(in);
        this.chatName = in.readString();
        this.clientID = (UUID) in.readSerializable();
        this.timestamp = (Date) in.readSerializable();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        // TODO
    }

    public static Creator<RegisterRequest> CREATOR = new Creator<RegisterRequest>() {
        @Override
        public RegisterRequest createFromParcel(Parcel source) {
            return new RegisterRequest(source);
        }

        @Override
        public RegisterRequest[] newArray(int size) {
            return new RegisterRequest[size];
        }
    };

}
