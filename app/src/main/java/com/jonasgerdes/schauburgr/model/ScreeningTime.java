package com.jonasgerdes.schauburgr.model;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas on 05.03.2017.
 */

public class ScreeningTime {
    private LocalTime time;
    private List<Screening> screenings = new ArrayList<>();

    public LocalTime getTime() {
        return time;
    }

    public ScreeningTime setTime(LocalTime time) {
        this.time = time;
        return this;
    }

    public List<Screening> getScreenings() {
        return screenings;
    }

    public ScreeningTime setScreenings(List<Screening> screenings) {
        this.screenings = screenings;
        return this;
    }

    public ScreeningTime addScreening(Screening screening) {
        screenings.add(screening);
        return this;
    }
}
