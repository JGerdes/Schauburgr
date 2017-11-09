package com.jonasgerdes.schauburgr.model;

import android.util.Log;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.schauburg.SchauburgDataLoader;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Guide;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Screening;
import com.jonasgerdes.schauburgr.model.schauburg.entity.ScreeningDay;
import com.jonasgerdes.schauburgr.model.tmdb.TheMovieDatabaseDataLoader;

import org.joda.time.LocalDate;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.HttpException;

/**
 * Repository for easy access to local cache of movies and fetching data from server
 *
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

    /**
     * Gets observable network state. State changes when some network request are triggered
     *
     * @return Observable NetworkState
     */
    public Observable<NetworkState> getNetworkState() {
        return mState.hide();
    }

    /**
     * Loads movie and screening data from schauburg and try to get matching movie data via
     * TMDb. Invalidates local cache as soon as data from schauburg is available and saves all data
     * in local cache. Triggers {@link NetworkState} got via {@link #getNetworkState()} accordingly.
     */
    public void loadMovieData() {
        mState.onNext(NetworkState.LOADING);
        mDisposables.add(mSchauburgDataLoader.fetchGuideData()
                .flatMapIterable(Guide::getMovies)
                //ignore operas, since TMDb doesn't list those and may return movies
                //with similar name(s) which leads to confusion
                .filter(movie -> !movie.getGenres().contains(Movie.GENRE_MET_OPERA))
                .concatMap(mTheMovieDatabaseDataLoader::searchAndSaveMovie)
                .ignoreElements()
                .subscribe(() -> mState.onNext(NetworkState.DEFAULT), this::propagateErrorState)
        );
    }

    /**
     * Loads data of relevant videos (like trailers, teasers) for given movie
     *
     * @param movie Movie to load video data for
     */
    public void loadVideos(Movie movie) {
        if (movie.getTmdbId() == Movie.NO_ID || movie.getVideos().size() > 0) {
            return;
        }
        mState.onNext(NetworkState.LOADING);
        mDisposables.add(mTheMovieDatabaseDataLoader.getVideosAndSave(movie)
                .ignoreElements()
                .subscribe(() -> mState.onNext(NetworkState.DEFAULT), this::propagateErrorState)
        );
    }

    /**
     * Propagates error state via {@link #mState} by breaking down given throwable to get an
     * matching error message
     *
     * @param throwable Thrown error
     */
    private void propagateErrorState(Throwable throwable) {
        Log.d("MovieRepository", "Error during network request", throwable);
        NetworkState state = new NetworkState(NetworkState.STATE_ERROR);
        if (throwable instanceof HttpException) {
            HttpException httpError = ((HttpException) throwable);
            state.setHttpStatusCode(httpError.code());
            switch (httpError.code()) {
                case 429:
                    state.setMessage(R.string.network_error_request_limit);
                    break;
                default:
                    state.setMessage(httpError.getMessage());
            }
        } else {
            if (throwable instanceof SocketTimeoutException
                    || throwable instanceof UnknownHostException
                    || throwable instanceof SocketException) {
                state.setMessage(R.string.network_error_no_connection);
            } else {
                state.setMessage(throwable.getClass().getCanonicalName());
            }
        }

        mState.onNext(state);
    }


    /**
     * Gets all locally cached screenings grouped by time and date in {@link ScreeningDay}s
     *
     * @return Cached {@link ScreeningDay}s
     */
    public Observable<RealmResults<ScreeningDay>> getScreeningDays() {
        return RealmObservable.from(mRealm.where(ScreeningDay.class)
                .greaterThanOrEqualTo("date", new LocalDate().toDate())
                .findAllSorted("date", Sort.ASCENDING));
    }

    /**
     * Gets all movies which were released during last 14 days or shows as preview
     * Ordered by release date
     *
     * @return Observable RealmResults of recent released movies
     */
    public Observable<RealmResults<Movie>> getNewMovies() {
        return RealmObservable.from(mRealm.where(Movie.class)
                .greaterThanOrEqualTo("releaseDate", new LocalDate().minusDays(14).toDate())
                .or().contains("extras", Movie.EXTRA_PREVIEW)
                .findAllSorted("releaseDate", Sort.DESCENDING)
                .where().isNotEmpty("description").distinct("title"));
    }

    /**
     * Gets all movies having comedy as genre and a contentRating of 12+.
     * Ordered by title
     *
     * @return Observable RealmResults of action movies
     */
    public Observable<RealmResults<Movie>> getActionMovies() {
        return RealmObservable.from(mRealm.where(Movie.class)
                .contains("genres", "Action")
                .greaterThanOrEqualTo("contentRating", 12)
                .findAllSorted("title", Sort.ASCENDING)
                .where().isNotEmpty("description").distinct("title"));
    }

    /**
     * Gets all movies having comedy as genre and aren't movies for kids
     * Ordered by title
     *
     * @return Observable RealmResults of comedy movies
     */
    public Observable<RealmResults<Movie>> getComedyMovies() {
        return RealmObservable.from(mRealm.where(Movie.class)
                .contains("genres", "Komödie")
                //don't include family and animation movies,
                //otherwise (almost) all kids movies end up here
                .not().contains("genres", "Familie")
                .not().contains("genres", "Animation")
                .findAllSorted("title", Sort.ASCENDING)
                .where().isNotEmpty("description").distinct("title"));
    }


    /**
     * Gets all movies having thriller or horror as genre
     * Ordered by title
     *
     * @return Observable RealmResults of action movies
     */
    public Observable<RealmResults<Movie>> getThrillerMovies() {
        return RealmObservable.from(mRealm.where(Movie.class)
                .contains("genres", "Thriller")
                .or()
                .contains("genres", "Horror")
                .findAllSorted("title", Sort.ASCENDING)
                .where().isNotEmpty("description").distinct("title"));
    }

    /**
     * Gets all movies fitting for kids (having animation or family as genre
     * and  a contentRating of 0 or 6)
     * Ordered by title
     *
     * @return Observable RealmResults of movies for kids
     */
    public Observable<RealmResults<Movie>> getKidsMovies() {
        return RealmObservable.from(mRealm.where(Movie.class)
                .contains("genres", "Animation")
                .or()
                .contains("genres", "Familie")
                .or()
                .contains("genres", "Family")
                .lessThanOrEqualTo("contentRating", 6)
                .findAllSorted("title", Sort.ASCENDING)
                .where().isNotEmpty("description").distinct("title"));
    }

    /**
     * Gets all movies having given extra
     * Ordered by title
     *
     * @return Observable RealmResults of matching movies
     */
    public Observable<RealmResults<Movie>> getMoviesWithExtra(String extra) {
        return RealmObservable.from(mRealm.where(Movie.class)
                .contains("extras", extra)
                .findAllSorted("title", Sort.ASCENDING)
                .where().isNotEmpty("description").distinct("title"));
    }

    /**
     * Gets all movies having a duration of {@link Movie#DURATION_EXCESS_LENGTH_STATE_1} or longer
     * Ordered by duration
     *
     * @return Observable RealmResults of movies with excess length
     */
    public Observable<RealmResults<Movie>> getExcessLengthMovies() {
        return RealmObservable.from(mRealm.where(Movie.class)
                .greaterThanOrEqualTo("duration", Movie.DURATION_EXCESS_LENGTH_STATE_1)
                .not().contains("genres", "Met Opera")
                .findAllSorted("duration", Sort.DESCENDING)
                .where().isNotEmpty("description").distinct("title"));
    }

    /**
     * Gets all "special" movies like Met Opera streamings, movies of "Filmrolle", movies screened
     * with original audio ("O-Ton") or generally got a "Film-Tipp" recommendation.
     * Ordered by title
     *
     * @return Observable RealmResults of "special" movies
     */
    public Observable<RealmResults<Movie>> getSpecialMovies() {
        return RealmObservable.from(mRealm.where(Movie.class)
                .contains("genres", "Met Opera")
                .or()
                .contains("extras", Movie.EXTRA_REEL)
                .or()
                .contains("extras", Movie.EXTRA_TIP)
                .or()
                .contains("extras", Movie.EXTRA_OT)
                .or()
                .contains("title", "Malteser")
                .findAllSorted("releaseDate", Sort.DESCENDING));
    }

    /**
     * Gets all movies which are not in any of the given observables
     *
     * @param exclude List of Observable RealmResults of Movies to exclude
     * @return Observable Movies not contained in parameters
     */
    @SuppressWarnings("unchecked")
    public Observable<RealmResults<Movie>> getRemainingMovies(List<Observable<RealmResults<Movie>>> exclude) {
        return Observable.zip(exclude, objects -> {
            List<String> moviesIds = new ArrayList<>();
            for (Object object : objects) {
                for (Movie movie : (RealmResults<Movie>) object) {
                    moviesIds.add(movie.getResourceId());
                }
            }
            return mRealm.where(Movie.class)
                    .not().in("resourceId", moviesIds.toArray(new String[0]))
                    .findAll();
        });
    }

    /**
     * Gets a movie with given id ({@link Movie#resourceId})
     *
     * @param movieId id of movie
     * @return Movie with given id
     */
    public Observable<Movie> getMovieById(String movieId) {
        return RealmObservable.from(mRealm.where(Movie.class)
                .equalTo("resourceId", movieId)
                .findFirst());
    }

    /**
     * Finds all screenings for given movie and movies which are actually the same by looking at
     * the title of the movie and ignoring different extras like 3D, atmos etc.
     *
     * @param movie Movie to find screenings for
     * @return Observable RealmResult list of screenings for given and similar movies
     */
    public Observable<RealmResults<Screening>> getAllScreeningsFor(Movie movie) {
        return RealmObservable.from(mRealm.where(Screening.class)
                .equalTo("movie.title", movie.getTitle())
                .greaterThan("startDate", new Date())
                .findAllSorted("startDate", Sort.ASCENDING));
    }

    /**
     * Disposes repository by removing all internal listeners and subcribers and closing the db.
     */
    @Override
    public void dispose() {
        mDisposables.dispose();
        mRealm.close();
    }

    /**
     * Checks whether repository is already disposed
     *
     * @return true if repository was already disposed, false otherwise
     */
    @Override
    public boolean isDisposed() {
        return mDisposables.isDisposed();
    }
}
