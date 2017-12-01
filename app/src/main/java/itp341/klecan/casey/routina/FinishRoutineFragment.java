package itp341.klecan.casey.routina;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FinishRoutineFragment extends Fragment {

    private Button buttonDone;
    private TextView textResults;
    private  TextView textTitle;

    public FinishRoutineFragment() {
        // Required empty public constructor
    }

    public static FinishRoutineFragment newInstance() {
        FinishRoutineFragment fragment = new FinishRoutineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_end_routine, null);

        textTitle = (TextView) v.findViewById(R.id.text_task_name);
        textResults = (TextView) v.findViewById(R.id.text_cat_dialogue);

        buttonDone = (Button) v.findViewById(R.id.button_done);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToFragment(MainActivity.FRAG_MY_ROUTINE);
            }
        });

        return v;
    }

}
