package ecocupfirebase.ecocup;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends Application {
    public static final String Channel = "channel";
    @Override
    public void onCreate() {
        super.onCreate();


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            Intent intent = new Intent(Home.this, MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            createNotificationChannels();
        }
    }
        private void createNotificationChannels()
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                NotificationChannel channel = new NotificationChannel(Channel, "Channel1", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("This is Channel one");
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }
        }
    }

//ToDo: if firebase user  KAI logged in san epixeirhsh tote:  else (boolean internal login