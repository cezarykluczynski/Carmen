package com.cezarykluczynski.carmen.util.network

import com.google.common.collect.Maps
import org.json.JSONObject
import org.mockserver.integration.ClientAndServer
import spock.lang.Specification

import static org.mockserver.integration.ClientAndServer.startClientAndServer
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response

class HTTPJSONClientImplTest extends Specification {

    private final Integer PORT = 8085

    private ClientAndServer mockServer

    private HTTPJSONClientImpl httpjsonClient

    def setup() {
        mockServer = startClientAndServer PORT
        httpjsonClient = new HTTPJSONClientImpl("127.0.0.1", PORT)
    }

    def cleanup() {
        mockServer.stop()
    }

    def "gets valid url"() {
        given:
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

        when:
        JSONObject response = httpjsonClient.get("/test/get")

        then:
        response.toString() == new JSONObject().put("success", true).toString()
    }

    def "throws exception for invalid url"() {
        when:
        httpjsonClient.get("/test/nope")

        then:
        thrown HTTPRequestException
    }

    def "posts to valid url"() {
        given:
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

        when:
        Map<String, String> params = Maps.newHashMap()
        params.put("key", "present")
        JSONObject response = httpjsonClient.post("/test/post", params)

        then:
        response.toString() == new JSONObject().put("success", true).toString()
    }

    def "posts to invalid url"() {
        when:
        httpjsonClient.post("/test/nope", Maps.newHashMap())

        then:
        thrown HTTPRequestException
    }

}
