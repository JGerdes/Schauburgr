package com.jonasgerdes.schauburgr.model;

import org.joda.time.LocalTime;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by jonas on 05.03.2017.
 */

public class ScreeningTime extends RealmObject{
    private Date time;
    private RealmList<Screening> screenings = new RealmList<>();

    public LocalTime getTime() {
        return new LocalTime(time);
    }

    public ScreeningTime setTime(LocalTime time) {
        this.time = time.toDateTimeToday().toDate();
        return this;
    }

    public RealmList<Screening> getScreenings() {
        return screenings;
    }

    public ScreeningTime setScreenings(RealmList<Screening> screenings) {
        this.screenings = screenings;
        return this;
    }

    public ScreeningTime addScreening(Screening screening) {
        screenings.add(screening);
        return this;
    }
}
