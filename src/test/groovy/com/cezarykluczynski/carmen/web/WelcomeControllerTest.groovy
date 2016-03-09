package com.cezarykluczynski.carmen.web

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import org.springframework.boot.test.SpringApplicationContextLoader

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@ContextConfiguration(
        classes = TestableApplicationConfiguration.class,
        loader = SpringApplicationContextLoader.class,
        locations = ["classpath:applicationContext.xml"]
)
@WebAppConfiguration
class WelcomeControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WelcomeController welcomeController

    private MockMvc mockMvc

    @BeforeMethod
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(welcomeController).build()
    }

    @Test
    void welcome() throws Exception {
        // exercise, assertion
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
    }

}
