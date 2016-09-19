package com.cezarykluczynski.carmen

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [TestableApplicationConfiguration.class])
@WebAppConfiguration
@org.springframework.boot.test.IntegrationTest
public abstract class IntegrationTest extends Specification {
}
