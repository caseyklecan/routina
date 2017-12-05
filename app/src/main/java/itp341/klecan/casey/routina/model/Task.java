package itp341.klecan.casey.routina.model;

import java.io.Serializable;

import javax.xml.datatype.Duration;

/**
 * Created by caseyklecan on 11/13/17.
 */

public class Task implements Serializable {

    private String name;
    private String time;
    private String snooze;

    private int snoozeCount;
    private int earlyFinishCount;

    /*
     * Default constructor for Task
     */
    public Task() {
        this.name = "";
        this.time = "";
        this.snooze = "";
        snoozeCount = 0;
        earlyFinishCount = 0;
    }

    /*
     * Task constructor when data is available
     */
    public Task(String name, String time, String snooze) {
        this.name = name;
        this.time = time;
        this.snooze = snooze;
        snoozeCount = 0;
        earlyFinishCount = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSnooze() {
        return snooze;
    }

    public void setSnooze(String snooze) {
        this.snooze = snooze;
    }

    public int getSnoozeCount() {
        return snoozeCount;
    }

    public int getEarlyFinishCount() {
        return earlyFinishCount;
    }

    public void snooze() {
        snoozeCount++;
    }

    public void finishEarly() {
        earlyFinishCount++;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", snooze=" + snooze +
                '}';
    }
}
