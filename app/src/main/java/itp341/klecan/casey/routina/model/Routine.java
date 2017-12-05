package itp341.klecan.casey.routina.model;

import android.icu.util.GregorianCalendar;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import itp341.klecan.casey.routina.RoutineConstants;

/**
 * Created by caseyklecan on 11/13/17.
 */

public class Routine implements Serializable {

    private String name;
    private String startTime;
    private HashMap<String, Boolean> daysOn;
    private ArrayList<Task> taskList;

    /*
     * Default constructor for the routine
     */
    public Routine() {
        this.name = "";
        this.startTime = "";
        this.daysOn = getEmptyDays();
        this.taskList = new ArrayList<>();
    }


    /*
     * Constructor for a new routine with data.
     */
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

    public HashMap<String, Boolean> getDaysOn() {
        return daysOn;
    }

    public void setDaysOn(HashMap<String, Boolean> daysOn) {
        this.daysOn = daysOn;
    }

    public ArrayList<Task> getTaskList() {
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

    /*
     * Returns a HashMap with every day of the week set to false.
     */
    public static HashMap<String, Boolean> getEmptyDays() {
        HashMap<String, Boolean> map = new HashMap<String, Boolean>();
        map.put(RoutineConstants.SUNDAY, false);
        map.put(RoutineConstants.MONDAY, false);
        map.put(RoutineConstants.TUESDAY, false);
        map.put(RoutineConstants.WEDNESDAY, false);
        map.put(RoutineConstants.THURSDAY, false);
        map.put(RoutineConstants.FRIDAY, false);
        map.put(RoutineConstants.SATURDAY, false);
        return map;
    }
}
