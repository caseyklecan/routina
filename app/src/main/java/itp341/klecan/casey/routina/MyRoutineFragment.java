package itp341.klecan.casey.routina;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;

import itp341.klecan.casey.routina.model.Routine;

public class MyRoutineFragment extends Fragment {

    private Button createButton;
    private RoutineAdapter adapter;
    private ListView routineList;
    private DatabaseReference user;

    public MyRoutineFragment() {
        // Required empty public constructor
    }

    public static MyRoutineFragment newInstance() {
        MyRoutineFragment fragment = new MyRoutineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_routine_list, null);

        user = ((MainActivity) getActivity()).getReferenceToCurrentUser();

        createButton = (Button) v.findViewById(R.id.button_create_routine);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToFragment(MainActivity.FRAG_CREATE_ROUTINE, null);
            }
        });

        adapter = new RoutineAdapter(getActivity(), Routine.class, R.layout.layout_row, user);

        routineList = (ListView) v.findViewById(R.id.list_routines);
        routineList.setAdapter(adapter);
        routineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseReference dbClicked = adapter.getRef(i);
                ((MainActivity) getActivity()).goToFragment(MainActivity.FRAG_VIEW_ROUTINE, dbClicked.toString());
            }
        });

        return v;
    }

    private class RoutineAdapter extends FirebaseListAdapter<Routine> {
        public RoutineAdapter(Activity activity, Class<Routine> modelClass, int modelLayout, DatabaseReference ref) {
            super(activity, modelClass, modelLayout, ref);
        }

        @Override
        protected void populateView(View v, Routine model, int position) {
            TextView name = (TextView) v.findViewById(R.id.text_routine_name);
            TextView days = (TextView) v.findViewById(R.id.text_routine_days);
            TextView time = (TextView) v.findViewById(R.id.text_routine_start_time);

            name.setText(model.getName());
            time.setText(model.getStartTime());

            HashMap<String, Boolean> map = model.getDaysOn();
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

            days.setText(daysText);
        }
    }
}
