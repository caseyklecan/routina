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

    private String url;

    private DatabaseReference routineRef;
    private DatabaseReference taskRef;

    private static final String ARG_URL = "routina.run_routine.routine_url";

    public RunRoutineFragment() {
        // Required empty public constructor
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_routine_task_run, null);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        url = getArguments().getString(ARG_URL);
        routineRef = db.getReferenceFromUrl(url);
        taskRef = routineRef.child("taskList");

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
        // todo read data, notably the task list
        // also make the title on the top bar the name of the routine that is executing

        textTitle = (TextView) v.findViewById(R.id.text_task_name);
        textDialogue = (TextView) v.findViewById(R.id.text_cat_dialogue);
        imageCat = (ImageView) v.findViewById(R.id.image_tina);
        buttonDone = (Button) v.findViewById(R.id.button_done);
        buttonSnooze = (Button) v.findViewById(R.id.button_snooze);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                setupTask();
            }
        });

        buttonSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snooze();
            }
        });

        return v;
    }

    private void setupTask() {
        currentIndex++;
        if (currentIndex == tasks.size()) finish();
        imageCat.setImageResource(R.drawable.cat_happy);
        textTitle.setText(tasks.get(currentIndex).getName());
        long millis = TimeUnit.MINUTES.toMillis(Integer.valueOf(tasks.get(currentIndex).getTime()));
        timer = new CountDownTimer(millis, 1000) {
            public void onTick(long millis) {
                long minutes = millis / 1000 / 60;
                long seconds = millis / 1000 % 60;
                String rem = "Time remaining:\n" + String.valueOf(minutes) + ":" + String.valueOf(seconds);
                textDialogue.setText(rem);
            }

            public void onFinish() {
                imageCat.setImageResource(R.drawable.cat_stressed);
                buttonSnooze.setEnabled(true);
                ((MainActivity) getActivity()).sendNotification("Time is up!", "Time to get moving onto the next task.");
            }
        }.start();
        buttonSnooze.setEnabled(false);
    }

    private void finish() {
        ((MainActivity) getActivity()).goToFragment(MainActivity.FRAG_FINISH_ROUTINE, url);
    }

    private void snooze() {
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
                textDialogue.setText("Uh oh! You're out of time! Get moving onto the next task!");
                ((MainActivity) getActivity()).sendNotification("Time is up!", "Time to get moving onto the next task.");
            }
        }.start();
    }



}
