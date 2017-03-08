package com.jonasgerdes.schauburgr.model;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas on 05.03.2017.
 */

public class ScreeningDay {
    private LocalDate date;
    private List<ScreeningTime> times = new ArrayList<>();

    public LocalDate getDate() {
        return date;
    }

    public ScreeningDay setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public List<ScreeningTime> getTimes() {
        return times;
    }

    public ScreeningDay setTimes(List<ScreeningTime> times) {
        this.times = times;
        return this;
    }

    public ScreeningDay addScreening(Screening screening) {
        boolean found = false;
        for (ScreeningTime screeningTime : times) {
            if (screening.getStartDate().toLocalTime().isEqual(screeningTime.getTime())) {
                screeningTime.addScreening(screening);
                found = true;
            }
        }
        if (!found) {
            times.add(new ScreeningTime()
                    .setTime(screening.getStartDate().toLocalTime())
                    .addScreening(screening)
            );

        }
        return this;
    }
}
