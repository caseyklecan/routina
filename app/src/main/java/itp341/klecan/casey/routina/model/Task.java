package itp341.klecan.casey.routina.model;

import java.io.Serializable;

import javax.xml.datatype.Duration;

/**
 * Created by caseyklecan on 11/13/17.
 */

public class Task implements Serializable {

    private String name;
    private Duration time;
    private Duration snooze;

    private int snoozeCount;
    private int earlyFinishCount;

    public Task() {
        this.name = "";
    }

    public Task(String name, Duration time, Duration snooze) {
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

    public Duration getTime() {
        return time;
    }

    public void setTime(Duration time) {
        this.time = time;
    }

    public Duration getSnooze() {
        return snooze;
    }

    public void setSnooze(Duration snooze) {
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
