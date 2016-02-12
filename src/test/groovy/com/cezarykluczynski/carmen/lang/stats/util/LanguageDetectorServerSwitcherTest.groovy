package com.cezarykluczynski.carmen.lang.stats.util

import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.contrib.java.lang.system.ExpectedSystemExit
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4.class)
class LanguageDetectorServerSwitcherTest {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none()

    String client

    @Before
    void setup() {
        client = LanguageDetectorServerSwitcher.getClient()
    }

    @Test
    void cannotPassZeroParameters() {
        exit.expectSystemExitWithStatus 1
        LanguageDetectorServerSwitcher.main()
    }

    @Test
    void cannotPassMoreThanOneArgument() {
        exit.expectSystemExitWithStatus 1
        LanguageDetectorServerSwitcher.main "one", "two"
    }

    @Test
    void onlyStopAndStartAreValidParameters() {
        exit.expectSystemExitWithStatus 1
        LanguageDetectorServerSwitcher.main "neither-stop-or-start"
    }

    @Test
    void serverCanBeStartedThenStopped() {
        if (client != "cli") {
            return
        }

        LanguageDetectorServerSwitcher.main "start"
        LanguageDetectorServerSwitcher.main "stop"

        Assert.assertTrue true
    }

}
