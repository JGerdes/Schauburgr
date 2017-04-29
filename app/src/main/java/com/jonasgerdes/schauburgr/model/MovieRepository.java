package com.jonasgerdes.schauburgr.model;

import com.jonasgerdes.schauburgr.model.schauburg.SchauburgDataLoader;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Guide;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;
import com.jonasgerdes.schauburgr.model.schauburg.entity.ScreeningDay;
import com.jonasgerdes.schauburgr.model.tmdb.TheMovieDatabaseDataLoader;

import org.joda.time.LocalDate;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.HttpException;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 26.04.2017
 */

public class MovieRepository implements Disposable {

    private final Realm mRealm;
    private SchauburgDataLoader mSchauburgDataLoader;
    private TheMovieDatabaseDataLoader mTheMovieDatabaseDataLoader;
    private CompositeDisposable mDisposables = new CompositeDisposable();
    private BehaviorSubject<NetworkState> mState
            = BehaviorSubject.createDefault(NetworkState.DEFAULT);

    public MovieRepository(SchauburgDataLoader schauburgDataLoader,
                           TheMovieDatabaseDataLoader theMovieDatabaseDataLoader) {
        mSchauburgDataLoader = schauburgDataLoader;
        mTheMovieDatabaseDataLoader = theMovieDatabaseDataLoader;
        mRealm = Realm.getDefaultInstance();
    }

    public Observable<NetworkState> getNetworkState() {
        return mState.hide();
    }

    public void loadMovieData() {
        mState.onNext(NetworkState.LOADING);
        mDisposables.add(mSchauburgDataLoader.fetchGuideData()
                .flatMapIterable(Guide::getMovies)
                .concatMap(mTheMovieDatabaseDataLoader::searchAndSaveMovie)
                .ignoreElements()
                .subscribe(() -> mState.onNext(NetworkState.DEFAULT), this::propagateErrorState)
        );
    }

    private void propagateErrorState(Throwable throwable) {
        NetworkState state = new NetworkState(NetworkState.STATE_ERROR);
        if (throwable instanceof HttpException) {
            HttpException httpError = ((HttpException) throwable);
            state.setHttpStatusCode(httpError.code());
            switch (httpError.code()) {
                case 429:
                    state.setMessage("Zu schnelle Abfragen, warte kurz!");
                    break;
                default:
                    state.setMessage(httpError.getMessage());
            }
        } else {
            if (throwable instanceof SocketTimeoutException
                    || throwable instanceof UnknownHostException
                    || throwable instanceof SocketException) {
                state.setMessage("Keine Internetverbindung :(");
            } else {
                state.setMessage(throwable.getClass().getCanonicalName());
            }
        }

        mState.onNext(state);
    }

    public Observable<RealmResults<ScreeningDay>> getScreeningDays() {
        return RealmObservable.from(mRealm.where(ScreeningDay.class)
                .greaterThanOrEqualTo("date", new LocalDate().toDate())
                .findAllSorted("date", Sort.ASCENDING));
    }

    public Observable<RealmResults<Movie>> getActionMovies() {
        return RealmObservable.from(mRealm.where(Movie.class)
                .contains("genres", "Action")
                .greaterThanOrEqualTo("contentRating", 12)
                .findAllSorted("title", Sort.ASCENDING)
                .where().distinct("title"));
    }

    public Observable<RealmResults<Movie>> getNewMovies() {
        return RealmObservable.from(mRealm.where(Movie.class)
                .greaterThanOrEqualTo("releaseDate", new LocalDate().minusDays(14).toDate())
                .findAllSorted("releaseDate", Sort.DESCENDING)
                .where().distinct("title"));
    }

    public Observable<RealmResults<Movie>> getComedyMovies() {
        return RealmObservable.from(mRealm.where(Movie.class)
                .contains("genres", "Komödie")
                //don't include family and animation movies,
                //otherwise (almost) all kids movies end up here
                .not().contains("genres", "Familie")
                .not().contains("genres", "Animation")
                .findAllSorted("title", Sort.ASCENDING)
                .where().distinct("title"));
    }

    public Observable<RealmResults<Movie>> getThrillerMovies() {
        return RealmObservable.from(mRealm.where(Movie.class)
                .contains("genres", "Thriller")
                .or()
                .contains("genres", "Horror")
                .findAllSorted("title", Sort.ASCENDING)
                .where().distinct("title"));
    }

    public Observable<RealmResults<Movie>> getKidsMovies() {
        return RealmObservable.from(mRealm.where(Movie.class)
                .contains("genres", "Animation")
                .or()
                .contains("genres", "Familie")
                .lessThanOrEqualTo("contentRating", 6)
                .findAllSorted("title", Sort.ASCENDING)
                .where().distinct("title"));
    }

    public Observable<RealmResults<Movie>> getMoviesWithExtra(String extra) {
        return RealmObservable.from(mRealm.where(Movie.class)
                .contains("extras", extra)
                .findAllSorted("title", Sort.ASCENDING)
                .where().distinct("title"));
    }

    public Observable<RealmResults<Movie>> getExcessLengthMovies() {
        return RealmObservable.from(mRealm.where(Movie.class)
                .greaterThanOrEqualTo("duration", Movie.DURATION_EXCESS_LENGTH_STATE_1)
                .not().contains("genres", "Met Opera")
                .findAllSorted("duration", Sort.DESCENDING)
                .where().distinct("title"));
    }

    public Observable<RealmResults<Movie>> getSpecialMovies() {
        return RealmObservable.from(mRealm.where(Movie.class)
                .contains("genres", "Met Opera")
                .or()
                .contains("extras", Movie.EXTRA_REEL)
                .or()
                .contains("extras", Movie.EXTRA_TIP)
                .or()
                .contains("extras", Movie.EXTRA_OT)
                .findAllSorted("releaseDate", Sort.DESCENDING));
    }

    @Override
    public void dispose() {
        mDisposables.dispose();
        mRealm.close();
    }

    @Override
    public boolean isDisposed() {
        return mDisposables.isDisposed();
    }
}