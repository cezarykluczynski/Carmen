package com.cezarykluczynski.carmen.lang.stats.util;

import com.cezarykluczynski.carmen.util.exec.command.ApacheCommonsCommand;
import com.cezarykluczynski.carmen.util.exec.executor.Executor;
import com.cezarykluczynski.carmen.util.exec.result.Result;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;

public class LanguageDetectorServerSwitcher {

    private static final String RUBY_PID_PATH = "./ruby/bin/.server_pid";

    private static ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            exitWithUsage();
            return;
        }

        String action = args[0];

        if (!(action.equals("start") || action.equals("stop"))) {
            exitWithUsage();
        }

        String client = getClient();

        if (!(client.equals("cli") || client.equals("http"))) {
            System.out.println("Client name misconfiguration. Allowed values are: \"cli\", \"http\".");
            exit1();
        }

        if (!client.equals("cli")) {
            System.out.println("Language detector client is not CLI. Server \"" + action + "\" action skipped.");
        } else if (action.equals("start")) {
            startServer();
        } else if (action.equals("stop")) {
            stopServer();
        }
    }

    public static String getClient() throws IOException {
        return getApplicationProperties().getProperty("detector.client");
    }

    public static Boolean getAssumeRunningServer() throws IOException {
        return getApplicationProperties().getProperty("detector.test.assumeRunningServer").equals("true");
    }

    public static Properties getApplicationProperties() throws IOException  {
        Properties prop = new Properties();
        InputStream stream = CLASS_LOADER.getResourceAsStream("application.properties");
        prop.load(stream);
        return prop;
    }

    private static void startServer() throws FileNotFoundException {
        Result result = Executor.execute(new ApacheCommonsCommand("ruby ruby/bin/server start"));

        if (result.isSuccessFul()) {
            String output = result.getOutput();
            String pidLine = output.substring(output.indexOf("PID is ") + 7);
            String pid = pidLine.substring(0, pidLine.indexOf("."));
            try {
                new File(RUBY_PID_PATH).createNewFile();
            } catch (IOException e) {
            }
            PrintWriter out = new PrintWriter(RUBY_PID_PATH);
            out.write(pid);
            out.close();
            System.out.println(output);
        }
    }

    private static void stopServer() throws IOException {
        String pid;
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(RUBY_PID_PATH);

            try {
                pid = IOUtils.toString(inputStream);

                if (pid != null) {
                    Result result = Executor.execute(new ApacheCommonsCommand("ruby ruby/bin/server stop"));
                    System.out.println(result.getOutput());
                }
            } catch (FileNotFoundException e) {
            } finally {
                inputStream.close();
            }
        } catch(FileNotFoundException e) {
        } finally {
        }
    }

    private static void exitWithUsage() {
        System.out.println("Usage: " + LanguageDetectorServerSwitcher.class.getCanonicalName() + ": [start|stop]");
        exit1();
    }

    private static void exit1() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (Arrays.stream(stackTraceElements)
                .anyMatch(stackTraceElement -> stackTraceElement.getClassName().contains("spockframework"))) {
            return;
        }
        System.exit(1);
    }

}
