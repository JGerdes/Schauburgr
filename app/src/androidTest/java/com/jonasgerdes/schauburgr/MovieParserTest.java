package com.jonasgerdes.schauburgr;

import android.support.test.runner.AndroidJUnit4;

import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;
import com.jonasgerdes.schauburgr.model.schauburg.parsing.MovieParser;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 20.05.2017
 */
@RunWith(AndroidJUnit4.class)
public class MovieParserTest {

    @Test
    public void parsingTitle_isCorrect() {
        MovieParser movieParser = new MovieParser();
        Movie movie = movieParser.parse(TestMovieDefinitions.SIMPLE_TITLE);
        String title = "Los Veganeros 2 - die vegane Komödie!";
        assertThat("Parsing title failed", movie.getTitle().equals(title));
    }


    @Test
    public void parsingResourceId_isCorrect() {
        MovieParser movieParser = new MovieParser();
        Movie movie = movieParser.parse(TestMovieDefinitions.SIMPLE_TITLE);
        assertThat("Parsing resource id failed", movie.getResourceId().equals("fw21"));
    }

    @Test
    public void parsingDuration_isCorrect() {
        MovieParser movieParser = new MovieParser();
        Movie movie = movieParser.parse(TestMovieDefinitions.SIMPLE_TITLE);
        assertThat("Parsing duration failed", movie.getDuration() == 80);
    }

    @Test
    public void parsingContentRating_isCorrect() {
        MovieParser movieParser = new MovieParser();
        Movie movie = movieParser.parse(TestMovieDefinitions.SIMPLE_TITLE);
        assertThat("Parsing content rating failed", movie.getContentRating() == 12);
    }

    @Test
    public void parsing2DMovie_isNot3D() {
        MovieParser movieParser = new MovieParser();
        Movie movie = movieParser.parse(TestMovieDefinitions.SIMPLE_TITLE);
        assertThat("2D movie was detected as 3D", !movie.is3D());
    }

    @Test
    public void parsing3DMovie_is3D() {
        MovieParser movieParser = new MovieParser();
        Movie movie = movieParser.parse(TestMovieDefinitions.MOVIE_3D);
        assertThat("3D movie was detected as 2D", movie.is3D());
    }

    @Test
    public void parsingAtmosMovie_isAtmos() {
        MovieParser movieParser = new MovieParser();
        Movie movie = movieParser.parse(TestMovieDefinitions.MOVIE_3D_ATMOS_PREVIEW);
        assertThat("Atmos wasn't detected", movie.isAtmos());
    }

    @Test
    public void parsingAtmosMovie_isPreview() {
        MovieParser movieParser = new MovieParser();
        Movie movie = movieParser.parse(TestMovieDefinitions.MOVIE_3D_ATMOS_PREVIEW);
        assertThat("Preview wasn't detected", movie.getExtras().contains(Movie.EXTRA_PREVIEW));
    }

    @Test
    public void parsingCast_isCorrect() {
        MovieParser movieParser = new MovieParser();
        Movie movie = movieParser.parse(TestMovieDefinitions.MOVIE_3D);
        boolean castCorrect = movie.getCast().contains("Emma Watson")
                && movie.getCast().contains("Dan Stevens")
                && movie.getCast().contains("Luke Evans")
                && movie.getCast().size() == 3;
        assertThat("Cast wasn't parsed correctly", castCorrect);
    }

    @Test
    public void parsingDirectors_isCorrect() {
        MovieParser movieParser = new MovieParser();
        Movie movie = movieParser.parse(TestMovieDefinitions.MOVIE_3D);
        assertThat("Director wasn't parsed correctly", movie.getDirectors().size() == 1
                && movie.getDirectors().contains("Bill Condon"));
    }
}
