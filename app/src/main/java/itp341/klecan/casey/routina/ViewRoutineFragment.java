package itp341.klecan.casey.routina;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

import itp341.klecan.casey.routina.model.Routine;
import itp341.klecan.casey.routina.model.Task;

public class ViewRoutineFragment extends Fragment {

    private static final String ARG_ROUTINE = "routina.view_routine.routine";

    private TextView textName;
    private TextView textDays;
    private TextView textStart;
    private ListView listTasks;

    private Routine currentRoutine;

    private DatabaseReference dbRoutine;
    private TaskAdapter adapter;

    public ViewRoutineFragment() {
        // Required empty public constructor
    }

    public static ViewRoutineFragment newInstance(Routine routine) {
        ViewRoutineFragment fragment = new ViewRoutineFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ROUTINE, routine);
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
        View v = inflater.inflate(R.layout.layout_view_routine, null);

        if (getArguments() == null) Log.d("VIEW ROUTINE", "ARGS NULL");

        currentRoutine = (Routine) getArguments().getSerializable(ARG_ROUTINE);

        textName = (TextView) v.findViewById(R.id.text_routine_name);
        textDays = (TextView) v.findViewById(R.id.text_routine_days);
        textStart = (TextView) v.findViewById(R.id.text_routine_start_time);
        listTasks = (ListView) v.findViewById(R.id.list_tasks);

//        adapter = new TaskAdapter(getActivity(), Task.class, R.layout.layout_row, dbRoutine);
//        listTasks.setAdapter(adapter);
        listTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // todo
            }
        });

        textName.setText(currentRoutine.getName());
        textStart.setText(currentRoutine.getStartTime());

        HashMap<String, Boolean> map = currentRoutine.getDaysOn();
        String daysText = "";

        if (map.get("Sunday")) {
            daysText += "Su ";
        }
        if (map.get("Monday")) {
            daysText += "Mo ";
        }
        if (map.get("Tuesday")) {
            daysText += "Tu ";
        }
        if (map.get("Wednesday")) {
            daysText += "We ";
        }
        if (map.get("Thursday")) {
            daysText += "Th ";
        }
        if (map.get("Friday")) {
            daysText += "Fr ";
        }
        if (map.get("Saturday")) {
            daysText += "Sa ";
        }
        textDays.setText(daysText);

        return v;
    }

    private class TaskAdapter extends FirebaseListAdapter<Task> {
        public TaskAdapter(Activity activity, Class<Task> modelClass, int modelLayout, DatabaseReference ref) {
            super(activity, modelClass, modelLayout, ref);
        }

        @Override
        protected void populateView(View v, Task model, int position) {
            TextView textTaskName = (TextView) v.findViewById(R.id.text_routine_name);
            TextView textTime = (TextView) v.findViewById(R.id.text_routine_days);
            TextView textSnooze = (TextView) v.findViewById(R.id.text_routine_start_time);

            textTaskName.setText(model.getName());
            textTime.setText(model.getTime().toString());
            textSnooze.setText(model.getSnooze().toString());
        }
    }

}
