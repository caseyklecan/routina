package itp341.klecan.casey.routina;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import itp341.klecan.casey.routina.model.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTaskDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTaskDialog extends DialogFragment {

    private Task task;

    public AddTaskDialog() {
        // Required empty public constructor
    }

    public static AddTaskDialog newInstance() {
        AddTaskDialog fragment = new AddTaskDialog();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.layout_create_task_dialog, null);

        task = new Task();

        builder.setView(v)
                .setTitle(R.string.label_add_task)
                .setPositiveButton(R.string.label_save,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                EditText name = (EditText) v.findViewById(R.id.edit_task_name);
                                EditText time = (EditText) v.findViewById(R.id.edit_task_time);
                                EditText snooze = (EditText) v.findViewById(R.id.edit_task_snooze);

                                task.setName(name.getText().toString());
                                task.setTime(time.getText().toString());
                                task.setSnooze(snooze.getText().toString());

                                MyDialogCallback host = (MyDialogCallback) getTargetFragment();
                                host.saveTask(task);

                            }
                        })
                .setNegativeButton(R.string.label_cancel_routine,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // just cancel & quit
                            }
                        });
        return builder.create();
    }

    public interface MyDialogCallback {
        public void saveTask(Task t);
    }

}
