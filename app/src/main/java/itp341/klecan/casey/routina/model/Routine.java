package itp341.klecan.casey.routina.model;

import android.icu.util.GregorianCalendar;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caseyklecan on 11/13/17.
 */

public class Routine implements Serializable {

    private String name;
    private String startTime;
    private Map<String, Boolean> daysOn;
    private List<Task> taskList;

    public Routine() {
        this.name = "";
    }

    public Routine(String name, String startTime) {
        this.name = name;
        this.startTime = startTime;
        this.daysOn = getEmptyDays();
        this.taskList = new ArrayList<>();
    }

        public Routine(String name, String startTime, HashMap<String, Boolean> daysOn, ArrayList<Task> taskList) {
        this.name = name;
        this.startTime = startTime;
        this.daysOn = daysOn;
        this.taskList = taskList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Map<String, Boolean> getDaysOn() {
        return daysOn;
    }

    public void setDaysOn(HashMap<String, Boolean> daysOn) {
        this.daysOn = daysOn;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    public void addTask(Task toAdd) {
        taskList.add(toAdd);
    }

    public void removeTask(Task toRemove) {
        taskList.remove(toRemove);
    }

    @Override
    public String toString() {
        return "Routine{" +
                "name='" + name + '\'' +
                ", startTime=" + startTime +
                ", daysOn=" + daysOn +
                ", taskList=" + taskList +
                '}';
    }

    public static HashMap<String, Boolean> getEmptyDays() {
        HashMap<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("Sunday", false);
        map.put("Monday", false);
        map.put("Tuesday", false);
        map.put("Wednesday", false);
        map.put("Thursday", false);
        map.put("Friday", false);
        map.put("Saturday", false);
        return map;
    }
}
