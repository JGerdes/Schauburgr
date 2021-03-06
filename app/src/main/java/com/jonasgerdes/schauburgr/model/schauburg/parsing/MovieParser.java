package com.jonasgerdes.schauburgr.model.schauburg.parsing;

import android.text.Html;

import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;
import com.jonasgerdes.schauburgr.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
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
    private static final String REGEX_DURATION = "'(\\d{0,3})'"; //0 if there is nothing provided
    private static final String REGEX_CONTENT_RATING = "'(\\d\\d?)'";
    private static final String REGEX_DESCRIPTION = "'((?>\\s|\\S)*?)'";
    private static final String REGEX_IS_3D = "'(1?)'";
    private static final String REGEX_STUB = "'.*?'";

    //description content patterns
    //find all "Genre: ... <br>", "Genres ... <br>", "(*) ... *"
    //[\s\w,\-\/öüäÖÜÄß] is used instead of . to prevent "(*) Darsteller:" to be detected as genre
    private static final String REGEX_DESCRIPTION_GENRE =
            "(?>Genres?:?|\\(\\*\\)) (.[\\s\\w,\\-\\/öüäÖÜÄß]*?)(<br>|\\*)";
    //safety threshold of max. 30 (and min 5) chars as name for director to prevent
    //capturing sentences starting with "Von" as director
    private static final String REGEX_DESCRIPTION_DIRECTOR = "(?>Regie|Von):? (.{5,30}?)(<br>|;)";
    //cast is often start with "Cast:", but sometimes just with "Mit ".
    //also sometimes it ends with "und mehr" or "mehr"
    //alternative pattern sometimes start with "(*) Darsteller:" and ends with ";" (but names
    //sometimes contain "&apos;", some make sure not word char is follorws by ;
    private static final String REGEX_DESCRIPTION_CAST
            //= "(?>^|<br>|\\(\\*\\) ?)(?>Cast|Mit|Darsteller):? (.*?)(?>(?>und)? mehr ?)?(?><br>|;\\W)";
            = "(?>^|<br>|\\(\\*\\) ?)(?>Cast|Mit|Darsteller):? (.*?)(?>(?>und)? mehr ?)?;?<br>";
    private static final String REGEX_DESCRIPTION_DURATION = "(?>Laufzeit|Länge):(.*?)(>?<br>|$)";
    private static final String REGEX_DESCRIPTION_CONTENT_RATING = "FSK:(.*?)(>?<br>|$)";
    private static final String REGEX_DESCRIPTION_LOCATION = "Produktionsland:? (.*?)(>?<br>|$)";

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
            new ExtraMapping(Movie.EXTRA_ATMOS, ": in Dolby Atmos", "in Dolby Atmos",
                    "- Dolby Atmos", "Dolby Atmos"),
            new ExtraMapping(Movie.EXTRA_OT, "Original Ton", "O-Ton", "OTon", " OT",
                    "(OV englisch)", "OV englisch", " OV"),
            new ExtraMapping(Movie.EXTRA_TIP, "- Filmtipp", "Filmtipp", "Tipp"),
            new ExtraMapping(Movie.EXTRA_REEL, "Filmrolle:", "- der besondere Film"),
            new ExtraMapping(Movie.EXTRA_LADIES_NIGHT, "Ladies Night:", "Ladies Night -"),
            new ExtraMapping(Movie.EXTRA_PREVIEW, ": Vorpremiere", "- Vorpremiere",
                    "Vorpremiere:", "Vorp:", "Vorp.:", "- Vorp", ":Vorp", "Vorp."),
            new ExtraMapping(Movie.EXTRA_LAST_SCREENINGS,
                    ": Letzte Chance!",
                    ": Letzte Chance",
                    "- Letzte Chance!",
                    "- Letzte Chance",
                    ": Letzte Gelegenheit!",
                    ": Letzte Gelegenheit",
                    "- Letzte Gelegenheit!",
                    "- Letzte Gelegenheit"),

            //various stuff which might is added to title and should be removed
            new ExtraMapping(Movie.EXTRA_IGNORE,
                    "- Jetzt in 2D",
                    "- in 2D",
                    " in 2D",
                    "- Das Kino Event!",
                    "CDU Vechta presents:"
            ),
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
            movie.setDuration(Long.parseLong(matcher.group(4)));
            movie.setContentRating(Integer.parseInt(matcher.group(5)));

            String rawTitle = matcher.group(2);
            rawTitle = parseExtras(movie, rawTitle);
            rawTitle = rawTitle.trim(); //remove possible whitespace around title
            rawTitle = removeHtmlEntities(rawTitle);
            movie.setTitle(rawTitle);

            //advanced parsing of description
            String description = matcher.group(6);
            description = remove(description, mDurationPattern);
            description = remove(description, mContentRatingPattern);
            description = remove(description, mLocationPattern);
            movie.setDescription(description);

            movie.setGenres(parseFromDescription(movie, mGenrePattern));
            movie.setDirectors(parseFromDescription(movie, mDirectorPattern));
            movie.setCast(parseFromDescription(movie, mCastPattern));

            //clean up whitespace
            description = movie.getDescription();
            description = cleanDescription(description);
            description = description.trim();
            description = removeLineBreaks(description);
            movie.setDescription(description);

            parseAndAddGenresFromTitle(movie);
        } else {
            movie = null;
            // TODO: 02-Nov-17 somehow (remotly) log this to debug movies which couldn't be parsed
        }

        return movie;
    }

    /**
     * Cleans description from junk (like "* *")
     * @param description description to clean
     * @return cleaned description
     */
    private String cleanDescription(String description) {
        //replace "* *" sometimes appear in description
        description = description.replaceAll("\\* \\*", "<br>");
        return description;
    }

    /**
     * Remove/Replace Html entities like &amp; etc with respective char
     *
     * @param string
     * @return
     */
    private String removeHtmlEntities(String string) {
        return Html.fromHtml(string).toString(); //fix &amp; etc
    }

    /**
     * Removes leading HTML line break tags
     *
     * @param description to remove line breaks from
     * @return Cleaned description
     */
    private String removeLineBreaks(String description) {
        while (description.startsWith("<br>")) {
            description = description.substring(4, description.length());
        }
        return description;
    }

    /**
     * Extract data from given {@link Movie} by parsing its description text with help of provided
     * regex pattern. Automatically splits multiple values (concatenated by either , or /).
     * Removes everything matching the regex pattern from description.
     *
     * @param movie   Movie to parse genres from and save them into
     * @param pattern Compiled regex pattern to find data with
     * @return List of found data items
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
            String[] splitted = found.split("(,|/)");
            for (String item : splitted) {
                item = item.trim();
                //only add item not empty
                if (!item.isEmpty()) {
                    item = removeHtmlEntities(item);
                    parsed.add(item.trim());
                }
            }
            description = description.replace(matcher.group(0), "");
        }
        //remove found item(s) from description
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
        List<String> operaIndicators = Arrays.asList("Met Opera Live:", "MET Opera:");
        List<String> genres = new ArrayList<>();
        genres.addAll(movie.getGenres());
        for (String operaIndicator : operaIndicators) {
            if (movie.getTitle().toLowerCase().startsWith(operaIndicator.toLowerCase())) {

                //only add genre once, but remove all indicators from title
                if (!genres.contains(Movie.GENRE_MET_OPERA)) {
                    genres.add(Movie.GENRE_MET_OPERA);
                }

                //tidy up title
                String title = movie.getTitle();
                title = title.substring(operaIndicator.length(), title.length());
                title = title.trim();
                movie.setTitle(title);
            }
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
                //only save extra when not already found and not to be ignored
                if (!extras.contains(extraMapping.extra) &&
                        !extraMapping.extra.equals(Movie.EXTRA_IGNORE)) {
                    extras.add(extraMapping.extra);
                }
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
