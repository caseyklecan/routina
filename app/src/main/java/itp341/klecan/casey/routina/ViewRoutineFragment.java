package itp341.klecan.casey.routina;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import itp341.klecan.casey.routina.model.Routine;
import itp341.klecan.casey.routina.model.Task;

public class ViewRoutineFragment extends Fragment {

    private static final String ARG_ROUTINE = "routina.view_routine.routine";

    private TextView textName;
    private TextView textDays;
    private TextView textStart;
    private ListView listTasks;

    private Button editButton;
    private Button deleteButton;
    private Button runButton;

    private Routine currentRoutine;

    private DatabaseReference dbRoutine;
    private TaskAdapter adapter;

    public ViewRoutineFragment() {
        // Required empty public constructor
    }

    public static ViewRoutineFragment newInstance(String url) {
        ViewRoutineFragment fragment = new ViewRoutineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROUTINE, url);
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
        final View v = inflater.inflate(R.layout.layout_view_routine, null);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final String url = getArguments().getString(ARG_ROUTINE);
        dbRoutine = db.getReferenceFromUrl(url);
        final Routine routine = new Routine();

        dbRoutine.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Routine r = dataSnapshot.getValue(Routine.class);
                routine.setDaysOn(r.getDaysOn());
                routine.setName(r.getName());
                routine.setStartTime(r.getStartTime());
                routine.setTaskList(r.getTaskList());

                currentRoutine = routine;
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        currentRoutine = routine;

        editButton = (Button) v.findViewById(R.id.button_edit_routine);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToFragment(MainActivity.FRAG_EDIT_ROUTINE, dbRoutine.toString());
            }
        });

        deleteButton = (Button) v.findViewById(R.id.button_delete_routine);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRoutine.removeValue();
                ((MainActivity) getActivity()).goToFragment(MainActivity.FRAG_MY_ROUTINE, "");
            }
        });

        runButton = (Button) v.findViewById(R.id.button_run_routine);
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToFragment(MainActivity.FRAG_RUN_ROUTINE, url);
            }
        });

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
