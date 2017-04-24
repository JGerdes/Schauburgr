package com.jonasgerdes.schauburgr.network.guide_converter;

import android.text.Html;

import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for movies retrieved via schauburg-cineworld.de "API".
 * Create instances of {@link Movie} based on single lines which define a movie as
 * JavaScript object (via a constructor though, not as JSON)
 * Sample movie definition:
 * movies[2] = new movie('fw1','A Cure for Wellness','1703051945','146','16',
 * 'Ein junger, ehrgeiziger Manager...','','2','','','','');
 * Attributes like ressource-id, title, release date etc are extracted from this string via regex.
 * Sometimes the title of a movie contains additional information (e.g. whether it's in Dolby Atmos).
 * These attributes are extract and remove from title.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 05.03.2017
 */

public class MovieParser {
    private static final String REGEX_PREFIX = "new movie\\(";
    private static final String REGEX_ID = "'(\\w\\w\\d\\d?)'";
    private static final String REGEX_TITLE = "'(.*?)'";
    private static final String REGEX_RELEASE_DATE = "'(\\d{10})'";
    private static final String REGEX_DURATION = "'(\\d{2,3})'";
    private static final String REGEX_CONTENT_RATING = "'(\\d\\d?)'";
    private static final String REGEX_DESCRIPTION = "'((?>\\s|\\S)*?)'";
    private static final String REGEX_IS_3D = "'(1?)'";
    private static final String REGEX_STUB = "'.*?'";

    //description content patterns
    private static final String REGEX_DESCRIPTION_GENRE = "Genre:? (.*?)<br>";
    private static final String REGEX_DESCRIPTION_DIRECTOR = "(?>Regie|Von):? (.*?)<br>";
    //cast is often start with "Cast:", but sometimes just with "Mit ".
    //also sometimes it ends with "und mehr" or "mehr"
    private static final String REGEX_DESCRIPTION_CAST
            = "(?>Cast|Mit):? (.*?)(?>(?>und)? mehr ?)?<br>";
    private static final String REGEX_DESCRIPTION_DURATION = "Laufzeit:(.*?)<br>";
    private static final String REGEX_DESCRIPTION_CONTENT_RATING = "FSK:(.*?)<br>";
    private static final String REGEX_DESCRIPTION_LOCATION = "Produktionsland:? (.*?)<br>";

    private static final String REGEX_MOVIE = ""
            + REGEX_PREFIX
            + REGEX_ID + ','
            + REGEX_TITLE + ','
            + REGEX_RELEASE_DATE + ','
            + REGEX_DURATION + ','
            + REGEX_CONTENT_RATING + ','
            + REGEX_DESCRIPTION + ','
            + REGEX_STUB + ','
            + REGEX_STUB + ','
            + REGEX_IS_3D;

    /**
     * Possible appearances of extra attributes in title of movie. They are quite inconsistent so it
     * should be testes for a variety of spellings and symbol usage (like hyphens and brackets)
     */
    private static final ExtraMapping[] EXTRA_MAPPINGS = new ExtraMapping[]{
            new ExtraMapping(Movie.EXTRA_3D, "(3D)", "in 3D", "- 3D", "3D"),
            new ExtraMapping(Movie.EXTRA_ATMOS, "in Dolby Atmos", "- Dolby Atmos", "Dolby Atmos"),
            new ExtraMapping(Movie.EXTRA_OT, "Original Ton", "O-Ton", "OTon", " OT"),
            new ExtraMapping(Movie.EXTRA_TIP, "- Filmtipp", "Filmtipp", "Tipp"),
            new ExtraMapping(Movie.EXTRA_REEL, "Filmrolle:", "- der besondere Film"),
    };

    /**
     * Max char length of a single genre title. Is used as kind of security to prevent using
     * hole descriptions as genre in case there isn't a new line after genre definitions or
     * some other weird stuff.
     */
    private static final int GENRE_MAX_LENGTH = 16;

    private final Pattern mMoviePattern;
    private final Pattern mGenrePattern;
    private final Pattern mDirectorPattern;
    private final Pattern mCastPattern;
    private final Pattern mDurationPattern;
    private final Pattern mContentRatingPattern;
    private final Pattern mLocationPattern;

    public MovieParser() {
        mMoviePattern = Pattern.compile(REGEX_MOVIE);
        mGenrePattern = Pattern.compile(REGEX_DESCRIPTION_GENRE);
        mDirectorPattern = Pattern.compile(REGEX_DESCRIPTION_DIRECTOR);
        mCastPattern = Pattern.compile(REGEX_DESCRIPTION_CAST);
        mDurationPattern = Pattern.compile(REGEX_DESCRIPTION_DURATION);
        mContentRatingPattern = Pattern.compile(REGEX_DESCRIPTION_CONTENT_RATING);
        mLocationPattern = Pattern.compile(REGEX_DESCRIPTION_LOCATION);
    }


    /**
     * Creates a {@link Movie} instance by parsing give line from "API"
     *
     * @param line single JavaScript line calling movie constructor (as used in schauburgs "API")
     * @return new instance of {@link Movie} with data parse from string or null if parsing fails
     */
    public Movie parse(String line) {
        //in the javascript to parse, all movies are put in an array, so this is the simples way to
        //test whether we have a line containing a movie construction and can parse it
        if (!line.startsWith("movies[")) {
            return null;
        }
        Movie movie;
        Matcher matcher = mMoviePattern.matcher(line);
        if (matcher.find()) {
            movie = new Movie();
            movie.setResourceId(matcher.group(1));
            movie.setReleaseDate(SchauburgGuideConverter.parseDate(matcher.group(3)));
            movie.setDuration(Long.parseLong(matcher.group(4)));
            movie.setContentRating(Integer.parseInt(matcher.group(5)));

            String rawTitle = matcher.group(2);
            rawTitle = parseExtras(movie, rawTitle);
            rawTitle = rawTitle.trim(); //remove possible whitespace around title
            rawTitle = Html.fromHtml(rawTitle).toString(); //fix &amp; etc
            movie.setTitle(rawTitle);

            //advanced parsing of description
            String description = matcher.group(6);
            description = remove(description, mDurationPattern);
            description = remove(description, mContentRatingPattern);
            description = remove(description, mLocationPattern);
            description = description.trim();
            movie.setDescription(description);

            movie.setGenres(parseFromDescription(movie, mGenrePattern));
            movie.setDirectors(parseFromDescription(movie, mDirectorPattern));
            movie.setCast(parseFromDescription(movie, mCastPattern));

            parseAndAddGenresFromTitle(movie);
        } else {
            movie = null;
        }

        return movie;
    }

    /**
     * Extract genres from given {@link Movie} by parsing its description text. Adds found genres
     * to movie.
     *
     * @param movie Movie to parse genres from and save them into
     */
    private List<String> parseFromDescription(Movie movie, Pattern pattern) {
        List<String> parsed = new ArrayList<>();
        //search description for pattern
        String description = movie.getDescription();
        Matcher matcher = pattern.matcher(description);

        //sometimes description contains some introduction texts, already including information
        //like cast and director - so loop to find all occurrences
        while (matcher.find()) {
            String found = matcher.group(1);
            //split found parts string and remove whitespace
            String[] splitted = found.split(",");
            for (String item : splitted) {
                item = item.trim();
                //only add item not empty
                if (!item.isEmpty()) {
                    parsed.add(item.trim());
                }
            }
            description = description.replace(matcher.group(0), "");
        }
        //remove genre from description
        movie.setDescription(description);

        return parsed;
    }

    private String remove(String string, Pattern toRemove) {
        Matcher matcher = toRemove.matcher(string);
        if (matcher.find()) {
            return string.replace(matcher.group(0), "");
        }
        return string;
    }

    /**
     * Parses some specials genres (like operas) from title of movie. Already adds parsed genres
     * to movie.
     *
     * @param movie Movie to parse and add genres from title
     */
    private void parseAndAddGenresFromTitle(Movie movie) {
        //special "genre" for live operas
        String operaIndicator = "Met Opera Live:";
        List<String> genres = new ArrayList<>();
        genres.addAll(movie.getGenres());
        if (movie.getTitle().startsWith(operaIndicator)) {
            genres.add(Movie.GENRE_MET_OPERA);

            //Tidy up title
            String title = movie.getTitle();
            title = title.substring(operaIndicator.length(), title.length());
            title = title.trim();
            movie.setTitle(title);
        }
        movie.setGenres(genres);
    }

    /**
     * Parses extras like Dolby Atmos, "Filmrolle" etc from raw title. Adds parsed extras to given
     * {@link Movie} instance and removes them from title.
     *
     * @param movie    Movie to add found extras to
     * @param rawTitle Raw title containing extras
     * @return new title with parsed extras removed
     */
    private String parseExtras(Movie movie, String rawTitle) {
        List<String> extras = new ArrayList<>();
        for (ExtraMapping extraMapping : EXTRA_MAPPINGS) {
            ExtraParseResult result = parseExtra(rawTitle, extraMapping.hints);
            if (result.found) {
                extras.add(extraMapping.extra);
                rawTitle = result.newTitle;
            }
        }
        movie.setExtras(extras);
        return rawTitle;
    }

    /**
     * Checks whether given titl contains extra defined by given hints array.
     *
     * @param rawTitle raw title of a movie containing extras
     * @param hints    hints to look for in title to determine if movie has an extra
     * @return result object containing a new title without found extras and whether the extra
     * was found at all
     */
    private ExtraParseResult parseExtra(String rawTitle, String[] hints) {
        ExtraParseResult result = new ExtraParseResult();
        for (String hint : hints) {
            int start = rawTitle.toLowerCase().indexOf(hint.toLowerCase());
            if (start != -1) {
                //cut found string out of title
                rawTitle = StringUtil.remove(rawTitle, start, hint.length());
                result.found = true;
            }
        }
        result.newTitle = rawTitle;
        return result;
    }

    /**
     * Result of a check whether a title contains a hint to an extra
     */
    private class ExtraParseResult {
        String newTitle;
        boolean found;
    }

    /**
     * Mapping between an extra title and hints which determine that a movie has an extra
     */
    private static class ExtraMapping {
        public ExtraMapping(String extra, String... hints) {
            this.extra = extra;
            this.hints = hints;
        }

        String extra;
        String[] hints;
    }

}
