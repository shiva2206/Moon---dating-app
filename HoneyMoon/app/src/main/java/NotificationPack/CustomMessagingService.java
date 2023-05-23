package NotificationPack;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.honeymoon.R;
import com.example.honeymoon.call;
import com.example.honeymoon.chats;
import com.example.honeymoon.login_two;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.chatmodel;

public class CustomMessagingService extends FirebaseMessagingService {

    String receiverid,sendersid,title,message,type,imageurl,savdid,passwd,email,postid;
    Notification notification;
    int hashcode;
//    StatusBarNotification[] activeNotifications = NotificationListenerService.getActiveNotifications();
    Intent inten,cancelIntent;
    PendingIntent pendingIntent,cancelPendingIntent;
    RemoteViews contvew;
    String CHANNEL_ID = "MESSAGE";
    String CHANNEL_NAME = "MESSAGE";
    NotificationManagerCompat manager;
    int i=1;

    @SuppressLint("RemoteViewLayout")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        Data datamdel = (Data)remoteMessage.getData();

        savdid = this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("userid","none");
        passwd= this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("password","none");
        email= this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("email","none");
        receiverid=remoteMessage.getData().get("Towhom");
        title=remoteMessage.getData().get("Title");
        message=remoteMessage.getData().get("Message");
        type=remoteMessage.getData().get("Type");
        sendersid=remoteMessage.getData().get("Whoid");
        imageurl=remoteMessage.getData().get("Imageurl");
        postid=remoteMessage.getData().get("Postid");
        hashcode=Integer.parseInt(remoteMessage.getData().get("Hashid"));
        if (savdid.equals(receiverid)) {
//        NotificationCompat.Builder builder =
//                new NotificationCompat.Builder(getApplicationContext())
//                        .setSmallIcon(R.drawable.ic_launcher_background)
//                        .setContentTitle(title)
//                        .setContentText(message);
//        NotificationManager manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(0,builder.build());



            manager = NotificationManagerCompat.from(getApplicationContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }
            i++;

            if (!type.equals("delete")) {
                contvew = new RemoteViews(getPackageName(), R.layout.custom_notibar);
                contvew.setImageViewResource(R.id.chatpro, R.mipmap.ic_launcher);

                contvew.setTextViewText(R.id.title, title);
                if (!type.equals("message")) {
                    contvew.setTextViewText(R.id.text, message);
                }
                inten = new Intent(getApplicationContext(), login_two.class);
                inten.putExtra("userid", sendersid);
                inten.putExtra("postid",postid);
                switch (type) {
                    case "call":
//                    strhashcode = strhashcode+12;
                        inten = new Intent(getApplicationContext(), call.class);
                        inten.putExtra("userid", sendersid);
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 12, inten, PendingIntent.FLAG_UPDATE_CURRENT);

                        cancelIntent = new Intent(getBaseContext(), NotificationReceiver.class);
                        cancelIntent.putExtra("ID", 12);
                        cancelPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 12, cancelIntent, 0);
                        break;
                    case "tag":
//                    strhashcode=strhashcode+11;
                        inten.putExtra("where", "home");
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 11, inten, PendingIntent.FLAG_UPDATE_CURRENT);

                        cancelIntent = new Intent(getBaseContext(), NotificationReceiver.class);
                        cancelIntent.putExtra("ID", 11);
                        cancelPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 11, cancelIntent, 0);
                        break;
                    case "comment":
//                    strhashcode=strhashcode+10;
                        inten.putExtra("where", "comment");
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 10, inten, PendingIntent.FLAG_UPDATE_CURRENT);

                        cancelIntent = new Intent(getBaseContext(), NotificationReceiver.class);
                        cancelIntent.putExtra("ID", 10);
                        cancelPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 10, cancelIntent, 0);

                        break;
                    case "replied":
//                    strhashcode=strhashcode+13;
                        inten.putExtra("where", "comment");
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 13, inten, PendingIntent.FLAG_UPDATE_CURRENT);

                        cancelIntent = new Intent(getBaseContext(), NotificationReceiver.class);
                        cancelIntent.putExtra("ID", 13);
                        cancelPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 13, cancelIntent, 0);

                        break;
                    case "commentliked":
//                    strhashcode=strhashcode+14;
                        inten.putExtra("where", "comment");
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 14, inten, PendingIntent.FLAG_UPDATE_CURRENT);

                        cancelIntent = new Intent(getBaseContext(), NotificationReceiver.class);
                        cancelIntent.putExtra("ID", 14);
                        cancelPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 14, cancelIntent, 0);

                        break;
                    case "followreq":
//                    strhashcode=strhashcode+15;
                        inten.putExtra("where", "noti");
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 15, inten, PendingIntent.FLAG_UPDATE_CURRENT);

                        cancelIntent = new Intent(getBaseContext(), NotificationReceiver.class);
                        cancelIntent.putExtra("ID", 15);
                        cancelPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 15, cancelIntent, 0);

                        break;
                    case "follows":
//                    strhashcode=strhashcode+16;
                        inten.putExtra("where", "profile");
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 16, inten, PendingIntent.FLAG_UPDATE_CURRENT);

                        cancelIntent = new Intent(getBaseContext(), NotificationReceiver.class);
                        cancelIntent.putExtra("ID", 16);
                        cancelPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 16, cancelIntent, 0);

                        break;
                    case "accfollowreq":
//                    strhashcode=strhashcode+17;
                        inten.putExtra("where", "profile");
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 17, inten, PendingIntent.FLAG_UPDATE_CURRENT);

                        cancelIntent = new Intent(getBaseContext(), NotificationReceiver.class);
                        cancelIntent.putExtra("ID", 17);
                        cancelPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 17, cancelIntent, 0);

                        break;
                    case "accfrireq":
//                    strhashcode=strhashcode+18;
                        inten.putExtra("where", "profile");
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 18, inten, PendingIntent.FLAG_UPDATE_CURRENT);

                        cancelIntent = new Intent(getBaseContext(), NotificationReceiver.class);
                        cancelIntent.putExtra("ID", 18);
                        cancelPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 18, cancelIntent, 0);

                        break;
                    case "crush":
//                    strhashcode=strhashcode+19;
                        inten.putExtra("where", "profile");
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 19, inten, PendingIntent.FLAG_UPDATE_CURRENT);

                        cancelIntent = new Intent(getBaseContext(), NotificationReceiver.class);
                        cancelIntent.putExtra("ID", 19);
                        cancelPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 19, cancelIntent, 0);

                        break;
                    case "match":
//                    strhashcode=strhashcode+20;
                        inten.putExtra("where", "profile");
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 20, inten, PendingIntent.FLAG_UPDATE_CURRENT);

                        cancelIntent = new Intent(getBaseContext(), NotificationReceiver.class);
                        cancelIntent.putExtra("ID", 20);
                        cancelPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 20, cancelIntent, 0);

                        break;
                    case "friendreq":
//                    strhashcode=strhashcode+21;
                        inten.putExtra("where", "noti");
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 21, inten, PendingIntent.FLAG_UPDATE_CURRENT);

                        cancelIntent = new Intent(getBaseContext(), NotificationReceiver.class);
                        cancelIntent.putExtra("ID", 21);
                        cancelPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 21, cancelIntent, 0);

                        break;
                    case "chatliked":
                        inten.putExtra("where", "chats");
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 23, inten, PendingIntent.FLAG_UPDATE_CURRENT);
                        cancelIntent = new Intent(getBaseContext(), NotificationReceiver.class);
                        cancelIntent.putExtra("ID", 23);
                        cancelPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 23, cancelIntent, 0);

                        break;
                    case "message":
//                    strhashcode=strhashcode+22;
                        inten.putExtra("where", "chats");
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 22, inten, PendingIntent.FLAG_UPDATE_CURRENT);
                        cancelIntent = new Intent(getBaseContext(), NotificationReceiver.class);
                        cancelIntent.putExtra("ID", 22);
                        cancelPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 22, cancelIntent, 0);


                        FirebaseDatabase.getInstance().getReference().child("info").child(receiverid).child("chats")
                                .child(sendersid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String mss="";
                                        for(DataSnapshot dsh : snapshot.getChildren()){
                                            for(DataSnapshot asd : snapshot.child(dsh.getKey()).getChildren()){
                                                chatmodel chdel = asd.getValue(chatmodel.class);
                                                if (chdel.getUserid().equals(sendersid) &&
                                                        chdel.getSeen()==null){
                                                    mss = mss+"\n"+chdel.getChat();

                                                }
                                            }
                                        }
                                        contvew.setTextViewText(R.id.text, mss);
                                        no("");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        break;
                    case "defaut":
                        inten = new Intent(getApplicationContext(), chats.class);
                        inten.putExtra("userid", sendersid);
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, inten, PendingIntent.FLAG_UPDATE_CURRENT);

                        notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.whitefilledcircle)
                                .setContentTitle(title)
                                .setContentText(message)
                                .setContentIntent(pendingIntent)
                                .build();

                        break;
                    case "responce":
                        Intent intent = new Intent("responce");
                        intent.putExtra("responce","idk");

                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                        break;
                    default:


                        break;

                }


//            StatusBarNotification[] activeNotifications = service.getActiveNotifications();
//            Then loop through available notifications and use getId()
//
//            if(activeNotifications!=null){
//
//                for(StatusBarNotification notification : activeNotifications ){
//
//                    if (notification.getId() == ID_TO_CHECK) {
//                        // do your operation
//                    }
//
//                }
//            }
//            hashcode = strhashcode.hashCode();
                if (!type.equals("message")) {
                   no(message);
                }
            }else {
                manager.cancel(hashcode);
            }
        }
    }
    public void no(String messae){
        contvew.setOnClickPendingIntent(R.id.layout, pendingIntent);

        notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContent(contvew)
                .setSmallIcon(R.drawable.whitefilledcircle)
                .setContentTitle(title)
                .setContentText(hashcode+"")
                .setAutoCancel(true)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.notify(hashcode, notification);

        }
    }
    public static int getrandomnum(){
        Date dat = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("mmssSS");
        String sg=ft.format(dat);
        return Integer.parseInt(sg);
    }



    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    public Bitmap getbittu(String urll){
        InputStream in;
        try {

            URL url = new URL(urll);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            in = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(in);
            return myBitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
