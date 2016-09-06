package com.cezarykluczynski.carmen.lang.stats.util

import spock.lang.Requires
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class LanguageDetectorServerSwitcherTest extends Specification {

    private PrintStream OUT

    private ByteArrayOutputStream byteArrayOutputStream

    def setupSpec() {
        if (LanguageDetectorServerSwitcher.assumeRunningServer) {
            String[] stopArgs = new String[1]
            stopArgs[0] = "stop"
            LanguageDetectorServerSwitcher.main(stopArgs)
        }
    }

    def cleanupSpec() {
        if (LanguageDetectorServerSwitcher.assumeRunningServer) {
            String[] startArgs = new String[1]
            startArgs[0] = "start"
            LanguageDetectorServerSwitcher.main(startArgs)
        }
    }

    def setup() {
        OUT = System.out
        byteArrayOutputStream = new ByteArrayOutputStream()
        PrintStream printStreamInterceptor = new PrintStream(byteArrayOutputStream)
        System.setOut(printStreamInterceptor)
    }

    def cleanup() {
        System.setOut(OUT)
    }

    def "runs unsuccessfully without arguments"() {
        when:
        LanguageDetectorServerSwitcher.main(new String [0])

        then:
        byteArrayOutputStream.toString().contains("Usage")
    }

    def "runs unsuccessfully with more than one argument"() {
        given:
        String[] args = new String[2]
        args[0] = 'one'
        args[1] = 'two'

        when:
        LanguageDetectorServerSwitcher.main(args)

        then:
        byteArrayOutputStream.toString().contains("Usage")
    }

    def "runs unsuccessfully when argument is neither \"start\" nor \"stop\""() {
        given:
        String[] args = new String[1]
        args[0] = 'neither-stop-nor-start'

        when:
        LanguageDetectorServerSwitcher.main(args)

        then:
        byteArrayOutputStream.toString().contains("Usage")
    }

    @Requires({ LanguageDetectorServerSwitcher.getClient() == 'cli' })
    def "starts server, then stops it"() {
        given:
        String[] startArgs = new String[1]
        startArgs[0] = "start"
        String[] stopArgs = new String[1]
        stopArgs[0] = "stop"

        when:
        LanguageDetectorServerSwitcher.main(startArgs)
        LanguageDetectorServerSwitcher.main(stopArgs)
        String output = byteArrayOutputStream.toString()

        then:
        output.contains("Server stopped")
        output.contains("Server started")
    }

    @Requires({ LanguageDetectorServerSwitcher.getClient() == 'cli' })
    def "detects invalid detector client"() {
        given:
        Path productionProperties = Paths.get("./src/main/resources/application.properties")
        byte[] input = Files.readAllBytes(productionProperties)
        String content = new String(input, "UTF-8")
        byte[] out = content.replace("detector.client=cli", "detector.client=unknown").getBytes()

        ClassLoader classLoaderMock = new ClassLoader() {
            @Override
            InputStream getResourceAsStream(String name) {
                return new ByteArrayInputStream(out)
            }
        }
        LanguageDetectorServerSwitcher.CLASS_LOADER = classLoaderMock
        String[] startArgs = new String[1]
        startArgs[0] = "start"

        when:
        LanguageDetectorServerSwitcher.main(startArgs)
        String output = byteArrayOutputStream.toString()

        then:
        output.contains("Client name misconfiguration")
    }

}
