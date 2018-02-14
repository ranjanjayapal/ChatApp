package edu.stevens.cs522.chat.rest;

import android.app.IntentService;
import android.content.Intent;
import android.os.ResultReceiver;
import android.util.Log;

import static android.app.Activity.RESULT_OK;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class RequestService extends IntentService {

    public static final String SERVICE_REQUEST_KEY = "edu.stevens.cs522.chat.rest.extra.REQUEST";

    public static final String RESULT_RECEIVER_KEY = "edu.stevens.cs522.chat.rest.extra.RECEIVER";

    private RequestProcessor processor;

    public RequestService() {
        super("RequestService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        processor = new RequestProcessor(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Request request = intent.getParcelableExtra(SERVICE_REQUEST_KEY);
        ResultReceiver receiver = intent.getParcelableExtra(RESULT_RECEIVER_KEY);
        Log.e(RequestService.class.getCanonicalName(),"before response");
        Response response = processor.process(request);
        Log.e(RequestService.class.getCanonicalName(),"before if");
        if (receiver != null) {
            //  UI should display a toast message on completion of the operation
            Log.e(RequestService.class.getCanonicalName(),"inside if");
            receiver.send(RESULT_OK, null);
        }
    }

}
