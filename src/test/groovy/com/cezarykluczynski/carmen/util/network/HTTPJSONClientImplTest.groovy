package com.cezarykluczynski.carmen.util.network

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import org.mockserver.integration.ClientAndServer
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.Assert
import org.testng.annotations.AfterMethod
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response
import static org.mockserver.integration.ClientAndServer.startClientAndServer
import org.testng.annotations.BeforeMethod
import org.json.JSONObject
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class HTTPJSONClientImplTest extends AbstractTestNGSpringContextTests {

    ClientAndServer mockServer

    HTTPJSONClientImpl httpjsonClient

    private final Integer port = 8080

    @BeforeMethod
    void setUp() {
        mockServer = startClientAndServer port
        httpjsonClient = new HTTPJSONClientImpl("127.0.0.1", port)
    }

    @Test
    void getValid() {
        mockServer.when(
            request()
                .withMethod("GET")
                .withPath("/test/get")
        )
            .respond(
                response()
                    .withStatusCode(200)
                    .withBody("{'success':true}")
        )

        JSONObject response = httpjsonClient.get("/test/get")
        Assert.assertEquals response.toString(), new JSONObject().put("success", true).toString()
    }

    @Test(expectedExceptions = HTTPRequestException.class)
    void getInvalid() {
        httpjsonClient.get("/test/nope")
    }

    @Test
    void postValid() {
        mockServer.when(
            request()
                .withMethod("POST")
                .withPath("/test/post")
                .withBody('key=present')
        )
            .respond(
            response()
                .withStatusCode(200)
                .withBody('{"success":true}')
        )

        Map<String, String> params = new HashMap<>()
        params.put("key", "present")
        JSONObject response = httpjsonClient.post("/test/post", params)
        Assert.assertEquals response.toString(), new JSONObject().put("success", true).toString()
    }

    @Test(expectedExceptions = HTTPRequestException.class)
    void postInvalid() {
        httpjsonClient.post("/test/nope", new HashMap<>())
    }

    @AfterMethod
    void tearDown() {
        mockServer.stop()
    }
}
