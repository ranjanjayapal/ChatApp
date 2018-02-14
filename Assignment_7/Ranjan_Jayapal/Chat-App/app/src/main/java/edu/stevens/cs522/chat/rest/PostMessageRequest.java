package edu.stevens.cs522.chat.rest;

import android.os.Parcel;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.util.UUID;

import edu.stevens.cs522.chat.entities.ChatMessage;

/**
 * Created by dduggan.
 */

public class PostMessageRequest extends Request {
    public UUID clientID;
    public ChatMessage message;
    public String chatName;
    public String chatRoom;

    public PostMessageRequest(ChatMessage message, String chatName, UUID clientID) {
        //super();
        super(chatName, clientID);
        this.chatRoom = message.chatRoom;
        this.message = message;
        this.message.messageText=message.messageText;
        this.clientID = clientID;
        this.chatName = chatName;
    }

    @Override
    public String getRequestEntity() throws IOException {
        StringWriter wr = new StringWriter();
        JsonWriter jw = new JsonWriter(wr);
        return null;
    }

    @Override
    public Response getResponse(HttpURLConnection connection, JsonReader rd) throws IOException {
        throw new IllegalStateException("PostMessage request should only return dummy response");
    }

    public Response getDummyResponse() {
        return new DummyResponse(id);
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
        Log.e(PostMessageRequest.class.getCanonicalName(), ":WriteToParcel");
        super.writeToParcel(dest, flags);
        dest.writeString(this.chatRoom);
        message.writeToParcel(dest,flags);
    }

    public PostMessageRequest(String chatName, UUID clientID) {
        super(chatName, clientID);
    }

    public PostMessageRequest(Parcel in) {
        super(in);
        // TODO
        this.chatRoom = in.readString();
        this.message= ChatMessage.CREATOR.createFromParcel(in);
    }

    public static Creator<PostMessageRequest> CREATOR = new Creator<PostMessageRequest>() {
        @Override
        public PostMessageRequest createFromParcel(Parcel source) {
            return new PostMessageRequest(source);
        }

        @Override
        public PostMessageRequest[] newArray(int size) {
            return new PostMessageRequest[size];
        }
    };

}
