package itp341.klecan.casey.routina;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.optimizely.Optimizely;
import io.fabric.sdk.android.Fabric;
import itp341.klecan.casey.routina.model.Routine;

public class MainActivity extends AppCompatActivity {

    public static final int FRAG_MY_ROUTINE = 0;
    public static final int FRAG_CREATE_ROUTINE = 1;
    public static final int FRAG_CREATE_TASK = 2;
    public static final int FRAG_VIEW_ROUTINE = 3;
    public static final int FRAG_RUN_ROUTINE = 4;
    public static final int FRAG_FINISH_ROUTINE = 5;
    public static final int FRAG_EDIT_ROUTINE = 6;

    private FrameLayout contentFrame;

    private DatabaseReference user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Optimizely.startOptimizelyWithAPIToken("AANm0IcB-fiDA1sA0YiNHbcHXXfEPx_j~9435701948", getApplication());

        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        // if the user is in the database, get their node, else create it
        String id = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference ref = db.getReference(id);
        logUser(id);

        // check to see if user is already in database, if not make sure their data sticks
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    ref.setValue("placeholder");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        user = ref;


        contentFrame = (FrameLayout) findViewById(R.id.content_frame);

        Fragment frag = MyRoutineFragment.newInstance();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_frame, frag).addToBackStack("myRoutine").commit();

    }

    public void goToFragment(int fragId, String routine) {
        Fragment frag = MyRoutineFragment.newInstance();

        switch (fragId) {
            case FRAG_MY_ROUTINE: // my routine list
                break;
            case FRAG_CREATE_ROUTINE: // create routine fragment
                frag = CreateRoutineFragment.newInstance();
                break;
            case FRAG_CREATE_TASK: // create task fragment
                frag = CreateTaskFragment.newInstance();
                break;
            case FRAG_VIEW_ROUTINE: // view routine fragment
                frag = ViewRoutineFragment.newInstance(routine);
                break;
            case FRAG_RUN_ROUTINE: // run routine fragment
                frag = RunRoutineFragment.newInstance(routine);
                break;
            case FRAG_FINISH_ROUTINE: // finish routine fragment
                frag = FinishRoutineFragment.newInstance();
                break;
            case FRAG_EDIT_ROUTINE: // create routine fragment w data
                frag = CreateRoutineFragment.newInstance(routine);
                break;
        }


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.content_frame, frag).addToBackStack("myRoutine").commit();
    }

    private void logUser(String id) {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier(id);
        Crashlytics.setUserEmail("user@fabric.io");
        Crashlytics.setUserName("Test User");
    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }

    // Gets the Firebase reference to the current user's node.
    public DatabaseReference getReferenceToCurrentUser() {
        return user;
    }

    public void sendNotification(String title, String content) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.cat_happy)
                        .setContentTitle(title)
                        .setContentText(content);

        notificationManager.notify(145, mBuilder.build());
    }
}
