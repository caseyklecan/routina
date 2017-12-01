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

import com.google.firebase.database.DatabaseReference;

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

    public CreateRoutineFragment() {
        // Required empty public constructor
    }

    public static CreateRoutineFragment newInstance() {
        CreateRoutineFragment fragment = new CreateRoutineFragment();
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
                ((MainActivity) getActivity()).goToFragment(MainActivity.FRAG_MY_ROUTINE);
            }
        });

        sun = (CheckBox) v.findViewById(R.id.checkbox_sun);
        mon = (CheckBox) v.findViewById(R.id.checkbox_mon);
        tues = (CheckBox) v.findViewById(R.id.checkbox_tue);
        wed = (CheckBox) v.findViewById(R.id.checkbox_wed);
        thurs = (CheckBox) v.findViewById(R.id.checkbox_thu);
        fri = (CheckBox) v.findViewById(R.id.checkbox_fri);
        sat = (CheckBox) v.findViewById(R.id.checkbox_sat);

        return v;
    }

    // saves the current routine in firebase (as if it's a new routine)
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
        DatabaseReference ref = user.push();
        ref.setValue(newRoutine);
    }

}
