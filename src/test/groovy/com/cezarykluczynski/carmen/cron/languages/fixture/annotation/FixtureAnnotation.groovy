package com.cezarykluczynski.carmen.cron.languages.fixture.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.TYPE])
public @interface FixtureAnnotation {

    String value() default "";

}
