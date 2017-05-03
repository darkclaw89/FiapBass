package com.darkclawstudio.aulafirbase;

import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by leand on 02/05/2017.
 */

public class MyMessagingService extends FirebaseMessagingService {

    private static final String Tag = "FCM Messaging";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(Tag, "From: " + remoteMessage.getFrom());
        Log.d(Tag, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}
