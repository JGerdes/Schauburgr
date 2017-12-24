package com.jonasgerdes.schauburgr.model;

import android.support.annotation.NonNull;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 23.12.2017
 */

public enum CinemaHost {

    SCHAUBURG_CINEWORLD("schauburg-cineworld.de", "https://kinotickets.online/cineworld-vechta"),
    CENTRAL_CINEWORLD("central-cineworld.de", "https://kinotickets.online/cineworld-diepholz");


    private @NonNull String dataUrl;
    private @NonNull String ticketUrl;

    CinemaHost(@NonNull String dataUrl, @NonNull String ticketUrl) {
        this.dataUrl = dataUrl;
        this.ticketUrl = ticketUrl;
    }

    @NonNull
    public String getDataUrl() {
        return dataUrl;
    }

    @NonNull
    public String getTicketUrl() {
        return ticketUrl;
    }
}
