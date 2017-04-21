package com.jonasgerdes.schauburgr.model;

import org.joda.time.LocalDate;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Model representation of a day on which screenings are shown. Used for grouping screenings on
 * same days and times together.
 * Contains a list of ScreeningTimes which hold the actual screenings.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 05.03.2017
 */

public class ScreeningDay extends RealmObject {

    /**
     * Primary id for identification
     */
    @PrimaryKey
    private long id;

    /**
     * Date for the day. Time part is ignored and getter provides a {@link LocalDate} instance.
     * {@link Date} is used since Realm can save these.
     */
    private Date date;

    /**
     * List of all {@link ScreeningTime}s happening on this day.
     */
    private RealmList<ScreeningTime> times = new RealmList<>();

    public LocalDate getDate() {
        return new LocalDate(date);
    }

    public ScreeningDay setDate(LocalDate date) {
        this.date = date.toDate();
        this.id = this.date.getTime();
        return this;
    }

    public RealmList<ScreeningTime> getTimes() {
        return times;
    }

    public ScreeningDay setTimes(RealmList<ScreeningTime> times) {
        this.times = times;
        return this;
    }

    /**
     * Add screening to this day. Since day doesn't hold any {@link Screening}s itself, an instance
     * of {@link ScreeningTime} with the same time as the given screening is searched or created if
     * there isn't one yet and then the screening is added to it.
     * @param screening
     * @return
     */
    public ScreeningDay addScreening(Screening screening) {
        boolean found = false;
        //search all existing screening times for time of screening
        for (ScreeningTime screeningTime : times) {
            if (screening.getStartDate().toLocalTime().isEqual(screeningTime.getTime())) {
                screeningTime.addScreening(screening);
                found = true;
            }
        }
        //if no matching time exists already, create a new one
        if (!found) {
            times.add(new ScreeningTime()
                    .setTime(screening.getStartDate().toLocalTime())
                    .addScreening(screening)
            );

        }
        return this;
    }
}
