package itp341.klecan.casey.routina;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RunRoutineFragment extends Fragment {

    private TextView textTitle;
    private TextView textDialogue;
    private ImageView imageCat;
    private Button buttonDone;
    private Button buttonSnooze;

    private DatabaseReference routineRef;
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
        String url = getArguments().getString(ARG_URL);
        routineRef = db.getReferenceFromUrl(url);
        // todo read data, notably the task list
        // also make the title on the top bar the name of the routine that is executing

        Log.d("RUN ROUTINE", "MADE IT!");


        textTitle = (TextView) v.findViewById(R.id.text_task_name);
        textDialogue = (TextView) v.findViewById(R.id.text_cat_dialogue);

        imageCat = (ImageView) v.findViewById(R.id.image_tina);

        buttonDone = (Button) v.findViewById(R.id.button_done);
        buttonSnooze = (Button) v.findViewById(R.id.button_snooze);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo move on to next task (same fragment, different task input)
            }
        });

        buttonSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo add time to timer, refresh
            }
        });

        return v;
    }

}
