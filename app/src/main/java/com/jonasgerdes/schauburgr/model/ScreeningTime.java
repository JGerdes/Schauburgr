package com.jonasgerdes.schauburgr.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jonas on 05.03.2017.
 */

public class ScreeningTime {
    private Calendar time;
    private List<Screening> screenings = new ArrayList<>();

    public Calendar getTime() {
        return time;
    }

    public ScreeningTime setTime(Calendar time) {
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
