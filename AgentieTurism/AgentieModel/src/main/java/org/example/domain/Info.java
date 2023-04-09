package org.example.domain;

import java.time.LocalTime;

public class Info {
    private String landmark;
    private LocalTime t1;
    private LocalTime t2;

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public LocalTime getT1() {
        return t1;
    }

    public void setT1(LocalTime t1) {
        this.t1 = t1;
    }

    public LocalTime getT2() {
        return t2;
    }

    public void setT2(LocalTime t2) {
        this.t2 = t2;
    }

    public Info(String landmark, LocalTime t1, LocalTime t2) {
        this.landmark = landmark;
        this.t1 = t1;
        this.t2 = t2;
    }
}
