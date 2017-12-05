package itp341.klecan.casey.routina;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;

import itp341.klecan.casey.routina.model.Task;

/**
 * Created by caseyklecan on 12/4/17.
 */

public class TaskAdapter extends FirebaseListAdapter<Task> {

    public TaskAdapter(Activity activity, Class<Task> modelClass, int modelLayout, DatabaseReference ref) {
        super(activity, modelClass, modelLayout, ref);
    }

    /*
     * Populates the row view with the given Task model.
     */
    @Override
    protected void populateView(View v, Task model, int position) {
        TextView name = (TextView) v.findViewById(R.id.text_routine_name);
        TextView time = (TextView) v.findViewById(R.id.text_routine_days);
        TextView snooze = (TextView) v.findViewById(R.id.text_routine_start_time);

        name.setText(model.getName());
        time.setText("Time: " + model.getTime() + " minutes");
        snooze.setText("Snooze: " + model.getSnooze() + " minutes");
    }
}