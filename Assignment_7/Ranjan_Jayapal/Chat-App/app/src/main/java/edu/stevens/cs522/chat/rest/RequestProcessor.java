package edu.stevens.cs522.chat.rest;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.stevens.cs522.chat.entities.ChatMessage;
import edu.stevens.cs522.chat.entities.Peer;
import edu.stevens.cs522.chat.managers.RequestManager;
import edu.stevens.cs522.chat.managers.TypedCursor;
import edu.stevens.cs522.chat.util.DateUtils;
import edu.stevens.cs522.chat.util.StringUtils;

/**
 * Created by dduggan.
 */


public class RequestProcessor {

    public static String TAG = RequestProcessor.class.getCanonicalName();

    private Context context;

    private RestMethod restMethod;

    private RequestManager requestManager;

    public RequestProcessor(Context context) {
        this.context = context;
        this.restMethod = new RestMethod(context);
        this.requestManager = new RequestManager(context);
    }

    public Response process(Request request) {
        return request.process(this);
    }

    public Response perform(RegisterRequest request) {
        return restMethod.perform(request);
    }

    public Response perform(PostMessageRequest request) {
        // We will just insert the message into the database, and rely on background sync to upload
        // return restMethod.perform(request)
        if(request.message==null)
            Log.e(RequestProcessor.class.getCanonicalName(), "Request message is null WTF");
        requestManager.persist(request.message);
        return request.getDummyResponse();
    }
    public Response perform(final SynchronizeRequest request) {
        Log.e(TAG, "synchronization request initiates");
        RestMethod.StreamingResponse response = null;
        final TypedCursor<ChatMessage> messages = requestManager.getUnsentMessages();
        request.lastSequenceNumber = requestManager.getLastSequenceNumber();
        request.timestamp = DateUtils.now();
        request.latitude = 0.0;
        request.longitude = 0.0;
        int numMessagesReplaced = messages.getCount();
        Log.e(TAG,"num messages replaced count:= " + numMessagesReplaced + " ------------------------==========");
        if (numMessagesReplaced < 1)
            return new DummyResponse(0);
        try {
            RestMethod.StreamingOutput out = new RestMethod.StreamingOutput() {
                @Override
                public void write(final OutputStream os) throws IOException {
                    try {
                        JsonWriter wr = new JsonWriter(new OutputStreamWriter(new BufferedOutputStream(os)));
                        wr.beginArray();
                        while(messages.moveToNext()) {
                            ChatMessage message = new ChatMessage(messages.getCursor());
                            /*
                             * TODO stream unread messages to the server:
                             * {
                             *   chatroom : ...,
                             *   timestamp : ...,
                             *   latitude : ...,
                             *   longitude : ....,
                             *   text : ...
                             * }
                             */
                            wr.beginObject();
                            Log.e(TAG,"1");
                            wr.name("chatroom");
                            wr.value(message.chatRoom);
                            Log.e(TAG,"2");
                            wr.name("timestamp");
                            wr.value(message.timestamp.getTime());
                            Log.e(TAG,"3");
                            wr.name("latitude");
                            wr.value(message.latitude);
                            Log.e(TAG,"4");
                            wr.name("longitude");
                            wr.value(message.longitude);
                            Log.e(TAG,"5");
                            wr.name("text");
                            Log.e(TAG,"5-1");
                            wr.value(message.messageText);
                            Log.e(TAG,"5-2");
                            wr.endObject();
                        }
                        wr.endArray();
                        wr.flush();
                        Log.e(TAG,"5-3");
                    } finally {
                        messages.close();
                    }
                }
            };
            Log.e(TAG,"5-4");
            response = restMethod.perform(request, out);
            Log.e(TAG,"6");
            JsonReader rd = new JsonReader(new InputStreamReader(new BufferedInputStream(response.getInputStream()), StringUtils.CHARSET));
            List<ChatMessage> chatMessages = new ArrayList<>();
            List<Peer> peers = new ArrayList<>();
            rd.beginObject();
            while (rd.hasNext()) {
                String nextName = rd.nextName();
                Log.e(TAG,"7");
                if (nextName.equals("clients")){
                    rd.beginArray();
                    while(rd.hasNext()){
                        Log.e(TAG,"9");
                        Peer peer = new Peer();
                        rd.beginObject();
                        while(rd.hasNext()){
                            switch (rd.nextName()){
                                case "username":
                                    peer.name = rd.nextString();
                                    Log.e(TAG,"10");
                                    break;
                                case "timestamp":
                                    peer.timestamp =  new Date(rd.nextLong());
                                    Log.e(TAG,"11");
                                    break;
                                case "latitude":
                                    peer.latitude = rd.nextDouble();
                                    Log.e(TAG,"12");
                                    break;
                                case "longitude":
                                    peer.longitude = rd.nextDouble();
                                    Log.e(TAG,"13");
                                    break;
                            }
                        }
                        Log.e(TAG,"14");
                        rd.endObject();
                        peers.add(peer);
                        Log.e(TAG,"13");
                    }
                    rd.endArray();
                }
                else if (nextName.equals("messages")) {
                    rd.beginArray();
                    while (rd.hasNext()) {
                        ChatMessage message = new ChatMessage();
                        rd.beginObject();
                        while (rd.hasNext()) {
                            switch (rd.nextName()) {
                                case "chatroom":
                                    message.chatRoom = rd.nextString();
                                    break;
                                case "timestamp":
                                    message.timestamp = new Date(rd.nextLong());
                                    break;
                                case "latitude":
                                    message.latitude = rd.nextDouble();
                                    break;
                                case "longitude":
                                    message.longitude = rd.nextDouble();
                                    break;
                                case "seqnum":
                                    message.seqNum = rd.nextInt();
                                    break;
                                case "sender":
                                    message.sender = rd.nextString();
                                    break;
                                case "text":
                                    message.messageText = rd.nextString();
                                    break;
                            }
                        }
                        rd.endObject();
                        chatMessages.add(message);
                    }
                    rd.endArray();
                } else
                    rd.skipValue();
            }
            rd.endObject();
            Log.e(TAG,"21");
            requestManager.syncMessages(chatMessages.size(), chatMessages);
            Log.e(TAG,"22");
            requestManager.deletePeers();
            Log.e(TAG,"23");
            for(Peer peer: peers){
                requestManager.persist(peer);
            }
            return response.getResponse();

        } catch (IOException e) {
            return new ErrorResponse(request.id, e);

        } finally {
            if (response != null) {
                response.disconnect();
            }
        }
    }

}
