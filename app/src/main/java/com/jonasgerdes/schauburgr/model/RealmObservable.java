package com.jonasgerdes.schauburgr.model;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposables;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 27.04.2017
 */
public class RealmObservable {
    private static final String TAG = "RealmObservable";

    public static <E extends RealmModel> Observable<RealmResults<E>>
    from(final RealmResults<E> results) {
        return Observable.create(emitter -> {
            final RealmChangeListener<RealmResults<E>> listener = emitter::onNext;
            emitter.setDisposable(Disposables.fromRunnable(() -> {
                        results.removeChangeListener(listener);
                    })
            );
            results.addChangeListener(listener);
            emitter.onNext(results);
        });
    }

    public static <E extends RealmObject> Observable<E> from(final E object) {
        return Observable.create(emitter -> {
            final RealmChangeListener<E> listener = emitter::onNext;
            emitter.setDisposable(Disposables.fromRunnable(() -> {
                        object.removeChangeListener(listener);
                    })
            );
            object.addChangeListener(listener);
            emitter.onNext(object);
        });
    }

}

