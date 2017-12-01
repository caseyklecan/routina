package itp341.klecan.casey.routina;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RunRoutineFragment extends Fragment {

    private TextView textTitle;
    private TextView textDialogue;
    private ImageView imageCat;
    private Button buttonDone;
    private Button buttonSnooze;

    public RunRoutineFragment() {
        // Required empty public constructor
    }

    public static RunRoutineFragment newInstance() {
        RunRoutineFragment fragment = new RunRoutineFragment();
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
