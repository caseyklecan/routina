package itp341.klecan.casey.routina;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import itp341.klecan.casey.routina.model.Task;

public class FinishRoutineFragment extends Fragment {

    private Button buttonDone;
    private TextView textResults;
    private  TextView textTitle;

    private DatabaseReference dbRoutine;
    private DatabaseReference dbTasks;

    private ArrayList<Task> tasks;

    private static final String ARG_URL = "routina.end_routine.routine_url";
    private static final String ARG_FINISH = "routina.end_routine.finish_count";
    private static final String ARG_SNOOZE = "routina.end_routine.snooze_count";

    public FinishRoutineFragment() {
        // Required empty public constructor
    }

    public static FinishRoutineFragment newInstance(String url, int finish, int snooze) {
        FinishRoutineFragment fragment = new FinishRoutineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putInt(ARG_FINISH, finish);
        args.putInt(ARG_SNOOZE, snooze);
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
        View v = inflater.inflate(R.layout.layout_end_routine, null);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String url = getArguments().getString(ARG_URL);
        dbRoutine = db.getReferenceFromUrl(url);
        dbTasks = dbRoutine.child(RoutineConstants.NODE_TASK);

        dbTasks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tasks = new ArrayList<Task>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Task t = child.getValue(Task.class);
                    tasks.add(t);
                }
                int random = new Random().nextInt(2);
                if (random == 0) processRoutine();
                else processTasks();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        textTitle = (TextView) v.findViewById(R.id.text_task_name);
        textResults = (TextView) v.findViewById(R.id.text_cat_dialogue);

        buttonDone = (Button) v.findViewById(R.id.button_done);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToFragment(MainActivity.FRAG_MY_ROUTINE, null);
            }
        });

        return v;
    }

    private void processTasks() {
        int snoozeCount = 0;
        int finishCount = 0;
        String snoozeName = "";
        String finishName = "";
        for (Task t : tasks) {
            if (t.getSnoozeCount() >= snoozeCount) {
                snoozeCount = t.getSnoozeCount();
                snoozeName = t.getName();
            }
            if (t.getEarlyFinishCount() >= finishCount) {
                finishCount = t.getEarlyFinishCount();
                finishName = t.getName();
            }
        }

        if (snoozeCount < finishCount) {
            String praise = "You've finished " + finishName + " early " + String.valueOf(finishCount) + " times! Way to go!";
            String title = "Nice work!";
            textTitle.setText(title);
            textResults.setText(praise);
        } else {
            String meh = "You've snoozed " + snoozeName + " " + String.valueOf(snoozeCount) + " times. Maybe you should work in a little more time for this one.";
            String title = "A for effort";
            textTitle.setText(title);
            textResults.setText(meh);
        }
    }

    private void processRoutine() {
        int snooze = getArguments().getInt(ARG_SNOOZE);
        int finish = getArguments().getInt(ARG_FINISH);

        if (snooze < finish) {
            String praise = "You finished tasks early " + String.valueOf(finish) + " times in this routine! Way to go!";
            String title = getResources().getString(R.string.label_feedback_good);
            textTitle.setText(title);
            textResults.setText(praise);
        } else {
            String meh = "You've snoozed " + String.valueOf(snooze) + " times in this routine. You should work on being a little more efficient.";
            String title = getResources().getString(R.string.label_feedback_bad);
            textTitle.setText(title);
            textResults.setText(meh);
        }
    }

}
