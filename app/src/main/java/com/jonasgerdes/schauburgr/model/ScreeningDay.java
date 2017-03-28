package com.jonasgerdes.schauburgr.model;

import org.joda.time.LocalDate;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by jonas on 05.03.2017.
 */

public class ScreeningDay extends RealmObject{
    private Date date;
    private RealmList<ScreeningTime> times = new RealmList<>();

    public LocalDate getDate() {
        return new LocalDate(date);
    }

    public ScreeningDay setDate(LocalDate date) {
        this.date = date.toDate();
        return this;
    }

    public RealmList<ScreeningTime> getTimes() {
        return times;
    }

    public ScreeningDay setTimes(RealmList<ScreeningTime> times) {
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
