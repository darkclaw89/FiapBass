package com.darkclawstudio.aulafirbase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by leand on 02/05/2017.
 */

public class MyInstanceIDService extends FirebaseInstanceIdService {

    private static final String Tag = "FCM Instance ID";

    DatabaseReference database   = FirebaseDatabase.getInstance().getReference().child("UserToken");
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(Tag, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token){
        database.child(token).setValue(true);
    }
}
