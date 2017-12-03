package itp341.klecan.casey.routina;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

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

public class CreateRoutineFragment extends Fragment {

    private EditText editName;
    private TimePicker editTime;
    private CheckBox sun;
    private CheckBox mon;
    private CheckBox tues;
    private CheckBox wed;
    private CheckBox thurs;
    private CheckBox fri;
    private CheckBox sat;

    // TODO task list

    private Button saveButton;

    private String routineName;
    private String routineHour;
    private String routineMinute;
    private String routineAM_PM;
    private ArrayList<Task> routineTaskList;

    private DatabaseReference currentRef;

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
            currentRef = db.getReferenceFromUrl(url);
            currentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Routine r = dataSnapshot.getValue(Routine.class);

                    editName.setText(r.getName());

                    HashMap<String, Boolean> map = r.getDaysOn();
                    if (map.get("Sunday")) {
                        sun.setChecked(true);
                    }
                    if (map.get("Monday")) {
                        mon.setChecked(true);
                    }
                    if (map.get("Tuesday")) {
                        tues.setChecked(true);
                    }
                    if (map.get("Wednesday")) {
                        wed.setChecked(true);
                    }
                    if (map.get("Thursday")) {
                        thurs.setChecked(true);
                    }
                    if (map.get("Friday")) {
                        fri.setChecked(true);
                    }
                    if (map.get("Saturday")) {
                        sat.setChecked(true);
                    }

                    String time = r.getStartTime();
                    int colonIndex = time.indexOf(':');
                    int hour = Integer.valueOf(time.substring(0 , colonIndex));
                    int minute = Integer.valueOf(time.substring(colonIndex + 1, colonIndex + 3));

                    editTime.setHour(hour);
                    editTime.setMinute(minute);

                    routineTaskList = r.getTaskList();
                    // todo set task list

                    routineName = r.getName();
                    // todo am / pm
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        // todo task list & adapter

        return v;
    }

    // saves the current routine in firebase (if it's new, creates it, else updates it)
    private void saveRoutine() {
        String name = editName.getText().toString();
        String time = routineHour + ":" + routineMinute + " " + routineAM_PM;

        HashMap<String, Boolean> days = Routine.getEmptyDays();
        if (sun.isChecked()) {
            days.put("Sunday", true);
        }
        if (mon.isChecked()) {
            days.put("Monday", true);
        }
        if (tues.isChecked()) {
            days.put("Tuesday", true);
        }
        if (wed.isChecked()) {
            days.put("Wednesday", true);
        }
        if (thurs.isChecked()) {
            days.put("Thursday", true);
        }
        if (fri.isChecked()) {
            days.put("Friday", true);
        }
        if (sat.isChecked()) {
            days.put("Saturday", true);
        }

        // todo add task functionality

        Routine newRoutine = new Routine(name, time, days, new ArrayList<Task>());
        DatabaseReference user = ((MainActivity) getActivity()).getReferenceToCurrentUser();
        if (currentRef == null) {
            // new routine, need to add to database
            DatabaseReference ref = user.push();
            ref.setValue(newRoutine);
        } else {
            // existing routine, need to update in the database
            currentRef.setValue(newRoutine);
        }
        startCheckout(getView());

    }

    public void startCheckout(View view) {
        if (currentRef == null) {
            Optimizely.trackEvent("create_new_routine");
        } else {
            Optimizely.trackEvent("update_routine");
        }
    }


}
