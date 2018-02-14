package edu.stevens.cs522.chat.rest;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;
import android.util.Log;

import java.util.UUID;

import edu.stevens.cs522.chat.entities.ChatMessage;
import edu.stevens.cs522.chat.settings.Settings;
import edu.stevens.cs522.chat.util.DateUtils;
import edu.stevens.cs522.chat.util.ResultReceiverWrapper;


/**
 * Created by dduggan.
 */

public class ChatHelper {

    public static final String DEFAULT_CHAT_ROOM = "_default";

    private Context context;

    public static final String TAG = ChatHelper.class.getCanonicalName();

    private ResultReceiverWrapper resultReceiverWrapper;

    public ChatHelper(Context context, ResultReceiverWrapper resultReceiverWrapper) {
        this.resultReceiverWrapper = resultReceiverWrapper;
        this.context = context;
    }

    //  provide a result receiver that will display a toast message upon completion
    public void register(String chatName, UUID clientID) {
        if (chatName != null && !chatName.isEmpty()) {
            Settings.saveChatName(context, chatName);
            RegisterRequest request = new RegisterRequest(chatName, clientID);
            Log.e(TAG, "after register request");
            Log.e(TAG, "after register request, -" + request.chatName + " ,arugment chatname-:" + chatName);
            addRequest(request, resultReceiverWrapper);
            Log.e(TAG, "after add request method");
        }
    }

    //  provide a result receiver that will display a toast message upon completion
    public void postMessage(String chatRoom, String text) {
        if (text != null && !text.isEmpty()) {
            if (chatRoom == null || chatRoom.isEmpty()) {
                chatRoom = DEFAULT_CHAT_ROOM;
            }
            ChatMessage message = new ChatMessage();
            message.chatRoom = chatRoom;
            message.messageText = text;
            message.sender = Settings.getChatName(context);
            message.latitude = 0.0;
            message.longitude = 0.0;
            message.timestamp = DateUtils.now();
            message.seqNum = 0;
            PostMessageRequest request = new PostMessageRequest(message, Settings.getChatName(context), Settings.getClientId(context));
            Log.e(TAG, "Post message request testing, message=: " + request.message.getMessageText());
            addRequest(request, resultReceiverWrapper);
        }
    }

    private void addRequest(Request request, ResultReceiver receiver) {
        context.startService(createIntent(context, request, receiver));
    }

    private void addRequest(Request request) {
        addRequest(request, null);
    }

    /**
     * Use an intent to send the request to a background service. The request is included as a Parcelable extra in
     * the intent. The key for the intent extra is in the RequestService class.
     */
    public static Intent createIntent(Context context, Request request, ResultReceiver receiver) {
        Log.e(TAG, "Chat helper: Create Intent Function Started");
        Intent requestIntent = new Intent(context, RequestService.class);
        Log.e(TAG, "Chat helper: before putting extra");
        requestIntent.putExtra(RequestService.SERVICE_REQUEST_KEY, request);
        Log.e(TAG, "Chat helper: After putting extra");
        if (receiver != null) {
            requestIntent.putExtra(RequestService.RESULT_RECEIVER_KEY, receiver);
        }
        Log.e(TAG, "Service about to start");
        return requestIntent;
    }

    public static Intent createIntent(Context context, Request request) {
        return createIntent(context, request, null);
    }

}
