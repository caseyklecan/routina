package itp341.klecan.casey.routina;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
    private DatabaseReference dbTasks;
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
        dbTasks = dbRoutine.child("taskList");
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

                adapter = new TaskAdapter(getActivity(), Task.class, R.layout.layout_row, dbTasks);
                listTasks.setAdapter(adapter);

                textName.setText(currentRoutine.getName());
                textStart.setText(currentRoutine.getStartTime());

                HashMap<String, Boolean> map = currentRoutine.getDaysOn();
                String daysText = "";

                if (map.get(RoutineConstants.SUNDAY)) {
                    daysText += RoutineConstants.SUN;
                }
                if (map.get(RoutineConstants.MONDAY)) {
                    daysText += RoutineConstants.MON;
                }
                if (map.get(RoutineConstants.TUESDAY)) {
                    daysText += RoutineConstants.TUES;
                }
                if (map.get(RoutineConstants.WEDNESDAY)) {
                    daysText += RoutineConstants.WED;
                }
                if (map.get(RoutineConstants.THURSDAY)) {
                    daysText += RoutineConstants.THURS;
                }
                if (map.get(RoutineConstants.FRIDAY)) {
                    daysText += RoutineConstants.FRI;
                }
                if (map.get(RoutineConstants.SATURDAY)) {
                    daysText += RoutineConstants.SAT;
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage(getResources().getString(R.string.label_delete_question))
                        .setTitle(R.string.label_delete)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbRoutine.removeValue();
                                ((MainActivity) getActivity()).goToFragment(MainActivity.FRAG_MY_ROUTINE, "");
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // just close the popup
                            }
                        }).show();
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

}
