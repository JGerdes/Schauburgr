package com.jonasgerdes.schauburgr.model.schauburg.entity;

import org.joda.time.LocalTime;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Model representation of a time on which screenings are shown. Used for grouping screenings at
 * same times together.
 * Contains a list of screenings.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 05.03.2017
 */

public class ScreeningTime extends RealmObject{

    /**
     * Time on which screenings take place. Date part is ignored and getter provides
     * a {@link LocalTime} instance.
     * {@link Date} is used since Realm can save these.
     */
    private Date time;

    /**
     * Lists of screenings shown on specified time
     */
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
