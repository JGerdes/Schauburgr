package com.jonasgerdes.schauburgr.network.guide_converter;

import android.text.Html;

import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jonas on 04.03.2017.
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
    private static final String REGEX_GENRE = "Genre:(.*?)<br>";

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

    private static final ExtraMapping[] EXTRA_MAPPINGS = new ExtraMapping[]{
            new ExtraMapping(Movie.EXTRA_3D, "(3D)", "in 3D", "- 3D", "3D"),
            new ExtraMapping(Movie.EXTRA_ATMOS, "in Dolby Atmos", "- Dolby Atmos", "Dolby Atmos"),
            new ExtraMapping(Movie.EXTRA_OT, "Original Ton", "O-Ton", "OTon", " OT"),
            new ExtraMapping(Movie.EXTRA_TIP, "- Filmtipp", "Filmtipp", "Tipp"),
            new ExtraMapping(Movie.EXTRA_REEL, "Filmrolle:", "- der besondere Film"),
    };

    private static final int GENRE_MAX_LENGTH = 16;

    private final Pattern mMoviePattern;
    private final Pattern mGenrePattern;

    public MovieParser() {
        mMoviePattern = Pattern.compile(REGEX_MOVIE);
        mGenrePattern = Pattern.compile(REGEX_GENRE);
    }


    public Movie parse(String line) {
        if (!line.startsWith("movies[")) {
            return null;
        }
        Movie movie = new Movie();
        Matcher matcher = mMoviePattern.matcher(line);
        if (matcher.find()) {
            movie.setResourceId(matcher.group(1));
            movie.setReleaseDate(SchauburgGuideConverter.parseDate(matcher.group(3)));
            movie.setDuration(Long.parseLong(matcher.group(4)));
            movie.setContentRating(Integer.parseInt(matcher.group(5)));
            movie.setDescription(matcher.group(6));

            String rawTitle = matcher.group(2);
            rawTitle = parseExtras(movie, rawTitle);
            rawTitle = rawTitle.trim();
            rawTitle = Html.fromHtml(rawTitle).toString(); //fix &amp; etc
            movie.setTitle(rawTitle);

            //Advanced parsing
            parseGenres(movie);
        }

        return movie;
    }

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

    private void parseGenres(Movie movie) {
        //Search description for genres
        Matcher matcher = mGenrePattern.matcher(movie.getDescription());
        if (matcher.find()) {
            String genreString = matcher.group(1);
            //Split found genre string and remove whitespace
            String[] genres = genreString.split(",");
            for (String genre : genres) {
                genre = genre.trim();
                //Safety threshold in case of parsing error
                if (genre.length() <= GENRE_MAX_LENGTH) {
                    movie.getGenres().add(genre);
                }
            }
        }
        if (movie.getTitle().startsWith("Met Opera Live:")) {
            movie.getGenres().add(Movie.GENRE_MET_OPERA);

            //Tidy up title
            String title = movie.getTitle();
            title = title.substring(15, title.length());
            title = title.trim();
            movie.setTitle(title);
        }

    }

    private ExtraParseResult parseExtra(String rawTitle, String[] hints) {
        ExtraParseResult result = new ExtraParseResult();
        for (String hint : hints) {
            int start = rawTitle.toLowerCase().indexOf(hint.toLowerCase());
            if (start != -1) {
                //cut found string out of
                rawTitle = StringUtil.remove(rawTitle, start, hint.length());
                result.found = true;
            }
        }
        result.newTitle = rawTitle;
        return result;
    }

    private class ExtraParseResult {
        String newTitle;
        boolean found;
    }

    private static class ExtraMapping {
        public ExtraMapping(String extra, String... hints) {
            this.extra = extra;
            this.hints = hints;
        }

        String extra;
        String[] hints;
    }

}
