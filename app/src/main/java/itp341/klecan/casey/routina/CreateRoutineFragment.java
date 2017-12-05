package itp341.klecan.casey.routina;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.optimizely.Optimizely;

import java.util.ArrayList;
import java.util.HashMap;

import itp341.klecan.casey.routina.model.Routine;
import itp341.klecan.casey.routina.model.Task;

public class CreateRoutineFragment extends Fragment implements AddTaskDialog.MyDialogCallback {

    private EditText editName;
    private TimePicker editTime;
    private CheckBox sun;
    private CheckBox mon;
    private CheckBox tues;
    private CheckBox wed;
    private CheckBox thurs;
    private CheckBox fri;
    private CheckBox sat;

    private ListView taskList;
    private TaskAdapter adapter;

    private Button saveButton;
    private Button addTaskButton;
    private Button cancelButton;

    private String routineName;
    private String routineHour;
    private String routineMinute;
    private String routineAM_PM;
    private ArrayList<Task> routineTaskList;

    private int editIndex = -1;

    private DatabaseReference routine;
    private DatabaseReference tasks;
    private Routine r;

    private static final String ARG_ROUTINE = "routina.create_routine.routine_url";

    public CreateRoutineFragment() {
        // Required empty public constructor
    }

    public static CreateRoutineFragment newInstance() {
        CreateRoutineFragment fragment = new CreateRoutineFragment();
        return fragment;
    }

    public static CreateRoutineFragment newInstance(String url) {
        CreateRoutineFragment fragment = new CreateRoutineFragment();
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
        View v = inflater.inflate(R.layout.layout_create_routine, null);

        editName = (EditText) v.findViewById(R.id.edit_routine_name);
        editTime = (TimePicker) v.findViewById(R.id.time_routine_start);
        editTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                if (i < 12) {
                    routineAM_PM = "AM";
                } else {
                    routineAM_PM = "PM";
                }

                routineHour = String.valueOf(i);
                routineMinute = String.valueOf(i1);
            }
        });

        saveButton = (Button) v.findViewById(R.id.button_save_routine);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRoutine();
                ((MainActivity) getActivity()).goToFragment(MainActivity.FRAG_MY_ROUTINE, null);
            }
        });

        addTaskButton = (Button) v.findViewById(R.id.button_add_task);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskDialog();
            }
        });

        cancelButton = (Button) v.findViewById(R.id.button_cancel_routine);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               cancel();
                ((MainActivity) getActivity()).goToFragment(MainActivity.FRAG_MY_ROUTINE, "");

            }
        });

        sun = (CheckBox) v.findViewById(R.id.checkbox_sun);
        mon = (CheckBox) v.findViewById(R.id.checkbox_mon);
        tues = (CheckBox) v.findViewById(R.id.checkbox_tue);
        wed = (CheckBox) v.findViewById(R.id.checkbox_wed);
        thurs = (CheckBox) v.findViewById(R.id.checkbox_thu);
        fri = (CheckBox) v.findViewById(R.id.checkbox_fri);
        sat = (CheckBox) v.findViewById(R.id.checkbox_sat);

        if (getArguments() == null) {
            // new routine
            routineTaskList = new ArrayList<>();
        } else {
            // edit routine
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            String url = getArguments().getString(ARG_ROUTINE);
            routine = db.getReferenceFromUrl(url);
            tasks = routine.child(RoutineConstants.NODE_TASK);

            routine.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    r = dataSnapshot.getValue(Routine.class);

                    editName.setText(r.getName());

                    HashMap<String, Boolean> map = r.getDaysOn();
                    if (map.get(RoutineConstants.SUNDAY)) {
                        sun.setChecked(true);
                    }
                    if (map.get(RoutineConstants.MONDAY)) {
                        mon.setChecked(true);
                    }
                    if (map.get(RoutineConstants.TUESDAY)) {
                        tues.setChecked(true);
                    }
                    if (map.get(RoutineConstants.WEDNESDAY)) {
                        wed.setChecked(true);
                    }
                    if (map.get(RoutineConstants.THURSDAY)) {
                        thurs.setChecked(true);
                    }
                    if (map.get(RoutineConstants.FRIDAY)) {
                        fri.setChecked(true);
                    }
                    if (map.get(RoutineConstants.SATURDAY)) {
                        sat.setChecked(true);
                    }

                    String time = r.getStartTime();
                    int colonIndex = time.indexOf(':');
                    int hour = Integer.valueOf(time.substring(0 , colonIndex));
                    int minute = Integer.valueOf(time.substring(colonIndex + 1, colonIndex + 3));

                    editTime.setHour(hour);
                    editTime.setMinute(minute);

                    routineTaskList = r.getTaskList();

                    routineName = r.getName();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            adapter = new TaskAdapter(getActivity(), Task.class, R.layout.layout_row, tasks);
            taskList = (ListView) v.findViewById(R.id.list_tasks);
            taskList.setAdapter(adapter);
            taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    editIndex = i;
                    Task t = (Task) adapterView.getItemAtPosition(i);
                    showAddTaskDialog(t);
                }
            });

        }

        return v;
    }

    // saves the current routine in firebase (if it's new, creates it, else updates it)
    private void saveRoutine() {
        String name = editName.getText().toString();
        String time = routineHour + ":" + routineMinute + " " + routineAM_PM;

        HashMap<String, Boolean> days = Routine.getEmptyDays();
        if (sun.isChecked()) {
            days.put(RoutineConstants.SUNDAY, true);
        }
        if (mon.isChecked()) {
            days.put(RoutineConstants.MONDAY, true);
        }
        if (tues.isChecked()) {
            days.put(RoutineConstants.TUESDAY, true);
        }
        if (wed.isChecked()) {
            days.put(RoutineConstants.WEDNESDAY, true);
        }
        if (thurs.isChecked()) {
            days.put(RoutineConstants.THURSDAY, true);
        }
        if (fri.isChecked()) {
            days.put(RoutineConstants.FRIDAY, true);
        }
        if (sat.isChecked()) {
            days.put(RoutineConstants.SATURDAY, true);
        }

        if (routineTaskList == null) routineTaskList = new ArrayList<>();

        Routine newRoutine = new Routine(name, time, days, routineTaskList);
//        newRoutine.setStatus(RoutineConstants.STATUS_CREATED);

        DatabaseReference user = ((MainActivity) getActivity()).getReferenceToCurrentUser();
        if (routine == null) {
            // new routine, need to add to database
            DatabaseReference ref = user.push();
            ref.setValue(newRoutine);
        } else {
            // existing routine, need to update in the database
            routine.setValue(newRoutine);
        }

        startCheckout(getView());

    }

    /*
     * If the routine hasn't been created yet, just don't save it. Otherwise, see if it was created
     * to store the tasks, or if it was an existing routine being edited.
     */
    private void cancel() {
        if (routine != null) {
            if (r != null && r.getStatus().equals(RoutineConstants.STATUS_PENDING)) {
                // need to delete
                routine.removeValue();
            }
        }
    }

    public void startCheckout(View view) {
        if (routine == null) {
            Optimizely.trackEvent("create_new_routine");
        } else {
            Optimizely.trackEvent("update_routine");
        }
    }

    private void showAddTaskDialog() {
        AddTaskDialog dialog = AddTaskDialog.newInstance();
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "AddTaskDialog");
    }

    private void showAddTaskDialog(Task toEdit) {
        AddTaskDialog dialog = AddTaskDialog.newInstance(toEdit);
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "AddTaskDialog");
    }

    @Override
    public void saveTask(Task t) {
        if (routineTaskList == null) {
            routineTaskList = new ArrayList<>();
        }
        if (editIndex < 0) {
            routineTaskList.add(t);
        } else {
            routineTaskList.set(editIndex, t);
            editIndex = -1;
        }
    }

    @Override
    public void deleteTask(Task t) {
        if (editIndex < 0) return;
        routineTaskList.remove(editIndex);
        tasks.setValue(routineTaskList);
    }
}
