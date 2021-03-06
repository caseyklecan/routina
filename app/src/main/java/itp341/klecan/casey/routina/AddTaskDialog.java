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
import android.widget.Toast;

import itp341.klecan.casey.routina.model.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTaskDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTaskDialog extends DialogFragment {

    private Task task;
    private boolean newTask = true;

    private static final String ARG_TASK = "routina.add_task_dialog.task_to_edit";

    public AddTaskDialog() {
        // Required empty public constructor
    }

    /*
     * newInstance to use when adding a new task
     */
    public static AddTaskDialog newInstance() {
        AddTaskDialog fragment = new AddTaskDialog();
        return fragment;
    }

    /*
     * newInstance to use when editing an existing task
     */
    public static AddTaskDialog newInstance(Task toEdit) {
        AddTaskDialog fragment = new AddTaskDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK, toEdit);
        fragment.setArguments(args);
        return fragment;
    }

    /*
     * onCreateDialog handles all the UI elements and their listeners.
     * If there is a task to edit, this will populate the UI elements with the
     * appropriate data.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.layout_create_task_dialog, null);

        final EditText name = (EditText) v.findViewById(R.id.edit_task_name);
        final EditText time = (EditText) v.findViewById(R.id.edit_task_time);
        final EditText snooze = (EditText) v.findViewById(R.id.edit_task_snooze);

        if (getArguments() == null) task = new Task();
        else {
            task = (Task) getArguments().getSerializable(ARG_TASK);
            name.setText(task.getName());
            time.setText(task.getTime());
            snooze.setText(task.getSnooze());
            newTask = false;
        }

        builder.setView(v)
                .setTitle(R.string.label_add_task)
                .setPositiveButton(R.string.label_save,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                String textName = name.getText().toString();
                                String textTime = time.getText().toString();
                                String textSnooze = snooze.getText().toString();
                                if (textName.isEmpty() || textTime.isEmpty() || textSnooze.isEmpty()) {
                                    Toast.makeText(getActivity(), "Invalid input. Please fill out all fields and try again.", Toast.LENGTH_LONG).show();
                                    MyDialogCallback host = (MyDialogCallback) getTargetFragment();
                                    host.deleteTask(task);
                                    return;
                                }

                                task.setName(textName);
                                task.setTime(textTime);
                                task.setSnooze(textSnooze);

                                MyDialogCallback host = (MyDialogCallback) getTargetFragment();
                                host.saveTask(task);
                            }
                        })
                .setNegativeButton(R.string.label_delete,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // just cancel & quit
                                if (!newTask) {
                                    MyDialogCallback host = (MyDialogCallback) getTargetFragment();
                                    host.deleteTask(task);
                                }
                            }
                        });
        return builder.create();
    }

    /*
     * Handles passing the Task data back to the CreateRoutineFragment.
     */
    public interface MyDialogCallback {
        public void saveTask(Task t);
        public void deleteTask(Task t);
    }

}
