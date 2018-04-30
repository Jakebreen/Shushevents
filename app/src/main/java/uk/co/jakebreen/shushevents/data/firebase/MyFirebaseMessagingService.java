package uk.co.jakebreen.shushevents.data.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.view.impl.MyEventListActivity;

import static uk.co.jakebreen.shushevents.view.impl.BaseActivity.mAPIService;

/**
 * Created by Jake on 20/04/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getFrom() != null && remoteMessage != null) {
            sendNotification(remoteMessage);
        }
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, MyEventListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                //.setContentText(remoteMessage.getData().get("message"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    public void sendNotificationToIDs(String tokens, String title, String message) {

        mAPIService.sendNotification(tokens, title, message).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody>  call, Throwable t) {
            }
        });

    }
}
