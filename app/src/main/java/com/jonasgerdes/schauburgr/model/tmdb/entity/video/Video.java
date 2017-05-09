package com.jonasgerdes.schauburgr.model.tmdb.entity.video;

import io.realm.RealmObject;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 09.05.2017
 */

public class Video extends RealmObject {

    private String id;
    private String iso6391;
    private String iso31661;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public String getId() {
        return id;
    }

    public Video setId(String id) {
        this.id = id;
        return this;
    }

    public String getIso6391() {
        return iso6391;
    }

    public Video setIso6391(String iso6391) {
        this.iso6391 = iso6391;
        return this;
    }

    public String getIso31661() {
        return iso31661;
    }

    public Video setIso31661(String iso31661) {
        this.iso31661 = iso31661;
        return this;
    }

    public String getKey() {
        return key;
    }

    public Video setKey(String key) {
        this.key = key;
        return this;
    }

    public String getName() {
        return name;
    }

    public Video setName(String name) {
        this.name = name;
        return this;
    }

    public String getSite() {
        return site;
    }

    public Video setSite(String site) {
        this.site = site;
        return this;
    }

    public int getSize() {
        return size;
    }

    public Video setSize(int size) {
        this.size = size;
        return this;
    }

    public String getType() {
        return type;
    }

    public Video setType(String type) {
        this.type = type;
        return this;
    }
}
