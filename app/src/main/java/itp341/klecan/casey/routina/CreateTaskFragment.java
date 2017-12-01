package itp341.klecan.casey.routina;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateTaskFragment extends Fragment {

    private EditText editName;
    private EditText editTime;
    private EditText editSnooze;
    private Button buttonSave;

    public CreateTaskFragment() {
        // Required empty public constructor
    }

    public static CreateTaskFragment newInstance() {
        CreateTaskFragment fragment = new CreateTaskFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_create_task, null);

        editName = (EditText) v.findViewById(R.id.edit_task_name);
        editTime = (EditText) v.findViewById(R.id.edit_task_time);
        editSnooze = (EditText) v.findViewById(R.id.edit_task_snooze);

        buttonSave = (Button) v.findViewById(R.id.button_save_task);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString();
                String time = editTime.getText().toString();
                String snooze = editSnooze.getText().toString();

                // todo format time

                // todo save to routine's task list

                // todo go back to create routine page
                ((MainActivity) getActivity()).goToFragment(MainActivity.FRAG_CREATE_ROUTINE);
            }
        });

        return v;
    }

}
