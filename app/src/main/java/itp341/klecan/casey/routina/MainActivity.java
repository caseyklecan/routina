package itp341.klecan.casey.routina;

import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private FrameLayout contentFrame;

    private DatabaseReference user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Optimizely.startOptimizelyWithAPIToken(getString(R.string.com_optimizely_api_key), getApplication());
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        // if the user is in the database, get their node, else create it
        String id = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference ref = db.getReference(id);

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

    public void goToFragment(int fragId) {
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
                frag = ViewRoutineFragment.newInstance(new Routine());
                break;
            case FRAG_RUN_ROUTINE: // run routine fragment
                frag = RunRoutineFragment.newInstance();
                break;
            case FRAG_FINISH_ROUTINE: // finish routine fragment
                frag = FinishRoutineFragment.newInstance();
                break;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.content_frame, frag).addToBackStack("myRoutine").commit();
    }

    // Gets the Firebase reference to the current user's node.
    public DatabaseReference getReferenceToCurrentUser() {
        return user;
    }
}
