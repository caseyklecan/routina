package itp341.klecan.casey.routina;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import itp341.klecan.casey.routina.model.Routine;

public class ViewRoutineFragment extends Fragment {

    private static final String ARG_ROUTINE = "routina.view_routine.routine";

    private TextView textName;
    private TextView textDays;
    private TextView textStart;
    private ListView listTasks;

    private Routine currentRoutine;

    public ViewRoutineFragment() {
        // Required empty public constructor
    }

    public static ViewRoutineFragment newInstance(Routine routine) {
        ViewRoutineFragment fragment = new ViewRoutineFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ROUTINE, routine);
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

        currentRoutine = (Routine) getArguments().getSerializable(ARG_ROUTINE);

        textName = (TextView) v.findViewById(R.id.text_routine_name);
        textDays = (TextView) v.findViewById(R.id.text_routine_days);
        textStart = (TextView) v.findViewById(R.id.text_routine_start_time);
        listTasks = (ListView) v.findViewById(R.id.list_tasks); // todo set adapter

        // todo populate
        textName.setText(currentRoutine.getName());


        return v;
    }

}
