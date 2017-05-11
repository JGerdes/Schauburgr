package com.jonasgerdes.schauburgr.model.tmdb.entity.video;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 09.05.2017
 */

public class VideoResponse {
    private int id;
    private List<Video> results = new ArrayList<>();

    public int getId() {
        return id;
    }

    public VideoResponse setId(int id) {
        this.id = id;
        return this;
    }

    public List<Video> getResults() {
        return results;
    }

    public VideoResponse setResults(List<Video> results) {
        this.results = results;
        return this;
    }
}
