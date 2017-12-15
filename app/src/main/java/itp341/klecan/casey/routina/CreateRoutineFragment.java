package itp341.klecan.casey.routina;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
    private EditText editHour;
    private EditText editMinute;
    private RadioGroup ampm;

    private CheckBox sun;
    private CheckBox mon;
    private CheckBox tues;
    private CheckBox wed;
    private CheckBox thurs;
    private CheckBox fri;
    private CheckBox sat;

    private TextView textTasks;
    private ListView taskList;
    private TaskAdapter adapter;

    private Button saveButton;
    private Button addTaskButton;
    private Button cancelButton;

    private String routineHour;
    private String routineMinute;
    private String routineAM_PM;
    private ArrayList<Task> routineTaskList;
    private ArrayList<Task> originalTaskList;

    private int editIndex = -1;

    private DatabaseReference routine;
    private DatabaseReference tasks;
    private Routine r;

    private static final String ARG_ROUTINE = "routina.create_routine.routine_url";

    public CreateRoutineFragment() {
        // Required empty public constructor
    }

    /*
     * newInstance to use when creating a new routine.
     */
    public static CreateRoutineFragment newInstance() {
        CreateRoutineFragment fragment = new CreateRoutineFragment();
        return fragment;
    }

    /*
     * newInstance to use when editing an existing routine.
     */
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
        editHour = (EditText) v.findViewById(R.id.edit_hours);
        editMinute = (EditText) v.findViewById(R.id.edit_minutes);
        ampm = (RadioGroup) v.findViewById(R.id.group_am_pm);
        routineAM_PM = "AM";

        // handles the Am/Pm radiogroup
        ampm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.button_am) {
                    routineAM_PM = "AM";
                } else {
                    routineAM_PM = "PM";
                }
            }
        });

        saveButton = (Button) v.findViewById(R.id.button_save_routine);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRoutine();

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

            }
        });

        sun = (CheckBox) v.findViewById(R.id.checkbox_sun);
        mon = (CheckBox) v.findViewById(R.id.checkbox_mon);
        tues = (CheckBox) v.findViewById(R.id.checkbox_tue);
        wed = (CheckBox) v.findViewById(R.id.checkbox_wed);
        thurs = (CheckBox) v.findViewById(R.id.checkbox_thu);
        fri = (CheckBox) v.findViewById(R.id.checkbox_fri);
        sat = (CheckBox) v.findViewById(R.id.checkbox_sat);

        textTasks = (TextView) v.findViewById(R.id.text_tasks);
        taskList = (ListView) v.findViewById(R.id.list_tasks);

        if (getArguments() == null) {
            // new routine -- get rid of the add task UI elements
            textTasks.setVisibility(View.GONE);
            taskList.setVisibility(View.GONE);
            addTaskButton.setVisibility(View.GONE);

        } else {
            // edit routine -- populate the UI with the data in the routine
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
                    String hour = time.substring(0 , colonIndex);
                    String minute = time.substring(colonIndex + 1, colonIndex + 3);

                    editHour.setText(hour);
                    editMinute.setText(minute);

                    routineTaskList = r.getTaskList();
                    originalTaskList = r.getTaskList();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            setupAdapter();
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

    /*
     * Saves the routine to Firebase. If it's new, gets all the data and creates a new routine node.
     * If it already exists, just updates the database with the new data.
     */
    private void saveRoutine() {
        String name = editName.getText().toString();
        if (name.length() == 0) {
            Toast.makeText(getActivity(), "Invalid input! Please add a name and try again.", Toast.LENGTH_LONG).show();
            return;
        }

        routineHour = editHour.getText().toString();
        if (routineHour.length() == 0 || Integer.valueOf(routineHour) > 12 || Integer.valueOf(routineHour) < 1) {
            Toast.makeText(getActivity(), "Invalid time! Please change your selection and try again.", Toast.LENGTH_LONG).show();
            return;
        }
        routineMinute = editMinute.getText().toString();
        if (routineMinute.length() == 0 || Integer.valueOf(routineMinute) > 59 || Integer.valueOf(routineMinute) < 0) {
            Toast.makeText(getActivity(), "Invalid time! Please change your selection and try again.", Toast.LENGTH_LONG).show();
            return;
        }
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

        DatabaseReference user = ((MainActivity) getActivity()).getReferenceToCurrentUser();
        if (routine == null) {
            // new routine, need to add to database
            DatabaseReference ref = user.push();
            ref.setValue(newRoutine);
        } else {
            // existing routine, need to update in the database
            routine.setValue(newRoutine);
        }
        ((MainActivity) getActivity()).goToFragment(MainActivity.FRAG_MY_ROUTINE, "");
    }

    /*
     * If the routine hasn't been created yet, just don't save it. Otherwise, make sure it has the
     * original task list and save that.
     */
    private void cancel() {
        if (routine != null) {
            r.setTaskList(originalTaskList);
            routine.setValue(r);
        }
        ((MainActivity) getActivity()).goToFragment(MainActivity.FRAG_MY_ROUTINE, "");
    }

    /*
     * Shows the Task Dialog when a task needs to be added.
     */
    private void showAddTaskDialog() {
        AddTaskDialog dialog = AddTaskDialog.newInstance();
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "AddTaskDialog");
    }

    /*
     * Shows the Task Dialog when the task exists and needs to be edited.
     */
    private void showAddTaskDialog(Task toEdit) {
        AddTaskDialog dialog = AddTaskDialog.newInstance(toEdit);
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "AddTaskDialog");
    }

    /*
     * Saves the task list & notifies the adapter that there has been a change.
     */
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
        if (tasks != null) tasks.setValue(routineTaskList);
        else if (routine != null) {
            if (r != null) {
                r.setTaskList(routineTaskList);
                routine.setValue(r);
            } else {
                routine.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        r = dataSnapshot.getValue(Routine.class);
                        routine.setValue(r);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        else {
            setupAdapter();
        }
    }

    /*
     * Sets up the task list adapter.
     */
    private void setupAdapter() {
        adapter = new TaskAdapter(getActivity(), Task.class, R.layout.layout_row, tasks);
        taskList.setAdapter(adapter);
    }

    /*
     * Deletes the task from the task list.
     */
    @Override
    public void deleteTask(Task t) {
        if (editIndex < 0) return;
        routineTaskList.remove(editIndex);
        tasks.setValue(routineTaskList);
    }

    /*
     * Guarantees the title will display correctly, even when the user presses the Back button.
     */
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(RoutineConstants.TITLE_CREATE);
    }
}
