package com.jonasgerdes.schauburgr.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jonas on 05.03.2017.
 */

public class Day {
    private Calendar date;
    private List<Screening> screenings = new ArrayList<>();

    public Calendar getDate() {
        return date;
    }

    public Day setDate(Calendar date) {
        this.date = date;
        return this;
    }

    public List<Screening> getScreenings() {
        return screenings;
    }

    public Day setScreenings(List<Screening> screenings) {
        this.screenings = screenings;
        return this;
    }

    public Day addScreening(Screening screening) {
        screenings.add(screening);
        return this;
    }
}
