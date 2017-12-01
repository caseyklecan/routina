package itp341.klecan.casey.routina;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MyRoutineFragment extends Fragment {

    private Button createButton;

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

        createButton = (Button) v.findViewById(R.id.button_create_routine);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToFragment(MainActivity.FRAG_CREATE_ROUTINE);
            }
        });

        return v;
    }

}
