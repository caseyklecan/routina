package itp341.klecan.casey.routina;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import itp341.klecan.casey.routina.model.Routine;
import itp341.klecan.casey.routina.model.Task;

public class RunRoutineFragment extends Fragment {

    private TextView textTitle;
    private TextView textDialogue;
    private ImageView imageCat;
    private Button buttonDone;
    private Button buttonSnooze;

    private ArrayList<Task> tasks;
    private int currentIndex = -1;
    private CountDownTimer timer;
    private boolean finishedEarly = true;
    private int snoozeCount = 0;
    private int finishCount = 0;

    private String url;
    private Routine routine;

    private DatabaseReference routineRef;
    private DatabaseReference taskRef;

    private static final String ARG_URL = "routina.run_routine.routine_url";

    public RunRoutineFragment() {
        // Required empty public constructor
    }

    /*
     * newInstance takes the URL of the routine that is currently being run.
     */
    public static RunRoutineFragment newInstance(String url) {
        RunRoutineFragment fragment = new RunRoutineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*
     * Handles the UI elements, along with getting data from the database. Starts the routine by
     * calling setupTask once everything is ready to go.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_routine_task_run, null);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        url = getArguments().getString(ARG_URL);
        routineRef = db.getReferenceFromUrl(url);
        taskRef = routineRef.child(RoutineConstants.NODE_TASK);

        routineRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                routine = dataSnapshot.getValue(Routine.class);
                getActivity().setTitle(routine.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        taskRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tasks = new ArrayList<Task>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Task t = child.getValue(Task.class);
                    tasks.add(t);
                }
                setupTask();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        textTitle = (TextView) v.findViewById(R.id.text_task_name);
        textDialogue = (TextView) v.findViewById(R.id.text_cat_dialogue);
        imageCat = (ImageView) v.findViewById(R.id.image_tina);
        buttonDone = (Button) v.findViewById(R.id.button_done);
        buttonSnooze = (Button) v.findViewById(R.id.button_snooze);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finishedEarly) {
                    tasks.get(currentIndex).finishEarly();
                    finishCount++;
                }
                timer.cancel();
                setupTask();
            }
        });

        buttonSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snooze();
                tasks.get(currentIndex).snooze();
            }
        });

        return v;
    }

    /*
     * Sets up the next task. Sets the cat image to the happy cat instead of the stressed cat,
     * starts the timer with the amount of time for the task, disables the snooze button.
     */
    private void setupTask() {
        currentIndex++;
        if (currentIndex >= tasks.size()) {
            finish();
            return;
        }
        imageCat.setImageResource(R.drawable.cat_happy);
        textTitle.setText(tasks.get(currentIndex).getName());
        long millis = TimeUnit.MINUTES.toMillis(Integer.valueOf(tasks.get(currentIndex).getTime()));
        timer = new CountDownTimer(millis, 1000) {
            /*
             * Sets the amount of time left on the text view.
             */
            public void onTick(long millis) {
                long minutes = millis / 1000 / 60;
                long seconds = millis / 1000 % 60;
                String rem = "Time remaining:\n" + String.valueOf(minutes) + ":" + String.valueOf(seconds);
                textDialogue.setText(rem);
                ((MainActivity) getActivity()).sendNotification("Time is up!", "Time to get moving onto the next task.");
            }

            /*
             * Enables the snooze button, sets the cat image to be stressed, encourages the user to
             * move onto the next task by sending them a notification that it's time to move on.
             */
            public void onFinish() {
                finishedEarly = false;
                imageCat.setImageResource(R.drawable.cat_stressed);
                buttonSnooze.setEnabled(true);
                ((MainActivity) getActivity()).sendNotification("SNOOZE Time is up!", "Time to get moving onto the next task, right now!");
            }
        }.start();
        finishedEarly = true;
        buttonSnooze.setEnabled(false);
    }

    /*
     * Handles the end of the routine-- resets the task node of the database with the new snooze /
     * finish early counts for tasks, then goes to the finish routine fragment.
     */
    private void finish() {
        taskRef.setValue(tasks);
        ((MainActivity) getActivity()).goToFragment(MainActivity.FRAG_FINISH_ROUTINE, url, finishCount, snoozeCount);
    }

    /*
     * Handles the user snoozing the current task. It will restart the timer with the snooze amount,
     * increase the snooze count for the routine, and disable the snooze button.
     */
    private void snooze() {
        finishedEarly = false;
        snoozeCount++;
        long millis = TimeUnit.MINUTES.toMillis(Integer.valueOf(tasks.get(currentIndex).getSnooze()));
        buttonSnooze.setEnabled(false);
        timer = new CountDownTimer(millis, 1000) {
            public void onTick(long millis) {
                long minutes = millis / 1000 / 60;
                long seconds = millis / 1000 % 60;
                String rem = "Time remaining:\n" + String.valueOf(minutes) + ":" + String.valueOf(seconds);
                textDialogue.setText(rem);
            }

            public void onFinish() {
                imageCat.setImageResource(R.drawable.cat_stressed);
                textDialogue.setText(getResources().getString(R.string.label_out_of_time));
                ((MainActivity) getActivity()).sendNotification("Time is up!", "Time to get moving onto the next task.");
            }
        }.start();
    }

    /*
     * Guarantees the correct title will be shown, even when the user presses the back button.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (routine == null) getActivity().setTitle(RoutineConstants.TITLE_RUN);
        else getActivity().setTitle(routine.getName());
    }


}
