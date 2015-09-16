package com.cezarykluczynski.carmen.web

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml"
])
@WebAppConfiguration
class WelcomeControllerTest {

    @Autowired
    private WelcomeController welcomeController

    private MockMvc mockMvc

    @Before
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
