package com.jonasgerdes.schauburgr.model.schauburg;

import com.jonasgerdes.schauburgr.model.schauburg.entity.Guide;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 28.04.2017
 */

public class SchauburgDataLoader{

    SchauburgApi mSchauburgApi;

    public SchauburgDataLoader(SchauburgApi schauburgApi) {
        mSchauburgApi = schauburgApi;
    }

    public Observable<Guide> fetchGuideData() {
        return mSchauburgApi.getFullGuide()
                .subscribeOn(Schedulers.io())
                .doOnNext(this::saveGuide);

    }

    private void saveGuide(Guide guide) {
        try (Realm r = Realm.getDefaultInstance()) {
            r.executeTransaction((realm) -> {
                realm.deleteAll();
                realm.copyToRealm(guide.getScreeningsGroupedByStartTime());
            });
        }
    }
}
