package com.jonasgerdes.schauburgr.model;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposables;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Factory for creating {@link Observable} wrappers for Realm's {@link RealmObject} and
 * {@link RealmResults}. Necessary since Realm doesn't support RxJava 2 yet.
 * <p>
 * Observables are updated as soon as realm triggers an update event on underlying RealmObject or
 * RealmResults. Listener used for this are removed as soon as the the subscriber
 * disposes the stream.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 27.04.2017
 */
public class RealmObservable {
    private static final String TAG = "RealmObservable";

    /**
     * Get an Observable wrapping given RealmResults
     *
     * @param results RealmResults to wrap inside an Observable
     * @param <E>     Type given RealmResults is a list of
     * @return Observable of given RealmResult
     */
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

    /**
     * Get an Observable wrapping given RealmObject
     *
     * @param object RealmObject to wrap inside an Observable
     * @param <E>    Type of given RealmObject
     * @return Observable of given RealmObject
     */
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

