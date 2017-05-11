package com.jonasgerdes.schauburgr.model.schauburg;

import com.jonasgerdes.schauburgr.model.schauburg.entity.Guide;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * DataLoader utilizing {@link SchauburgApi} to fetch movie and screening data from server
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 28.04.2017
 */

public class SchauburgDataLoader{

    SchauburgApi mSchauburgApi;

    public SchauburgDataLoader(SchauburgApi schauburgApi) {
        mSchauburgApi = schauburgApi;
    }

    /**
     * Download and save current guide data from server to local database
     * @return Observable guide data once downloaded and saved
     */
    public Observable<Guide> fetchGuideData() {
        return mSchauburgApi.getFullGuide()
                .subscribeOn(Schedulers.io())
                .doOnNext(this::saveGuide);

    }

    /**
     * Save given guide to local realm db
     * @param guide Guide to save
     */
    private void saveGuide(Guide guide) {
        try (Realm r = Realm.getDefaultInstance()) {
            r.executeTransaction((realm) -> {
                realm.deleteAll();
                realm.copyToRealm(guide.getScreeningsGroupedByStartTime());
            });
        }
    }
}
