package com.jonasgerdes.schauburgr;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 20.05.2017
 */

// Runs all unit tests.
@RunWith(Suite.class)
@Suite.SuiteClasses({
        MovieParserTest.class
})
public class UnitTestSuite {}