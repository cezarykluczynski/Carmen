package com.cezarykluczynski.carmen.lang.stats.util;

import com.cezarykluczynski.carmen.util.exec.Command;
import com.cezarykluczynski.carmen.util.exec.Executor;
import com.cezarykluczynski.carmen.util.exec.Result;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

public class LanguageDetectorServerSwitcher {

    private static final String rubyPidPath = "./target/.ruby_server_pid";

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            exitWithUsage();
        }

        String action = args[0];

        if (!(action.equals("start") || action.equals("stop"))) {
            exitWithUsage();
        }

        String client = getClient();

        if (!(client.equals("cli") || client.equals("http"))) {
            System.out.println("Client name misconfiguration. Allowed values are: \"cli\", \"http\".");
            System.exit(1);
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
        Properties prop = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("config.properties");
        prop.load(stream);
        return prop.getProperty("detector.client");
    }

    private static void startServer() throws FileNotFoundException {
        Result result = Executor.execute(new Command("ruby ruby/bin/server start"));

        if (result.isSuccessFull()) {
            String output = result.getOutput();
            String pidLine = output.substring(output.indexOf("PID is ") + 7);
            String pid = pidLine.substring(0, pidLine.indexOf("."));
            PrintWriter out = new PrintWriter(rubyPidPath);
            out.write(pid);
            out.close();
        }
    }

    private static void stopServer() throws IOException {
        String pid;
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(rubyPidPath);

            try {
                pid = IOUtils.toString(inputStream);

                if (pid != null) {
                    Executor.execute(new Command("ruby ruby/bin/server stop"));
                }
            } catch (FileNotFoundException e) {
            } finally {
                inputStream.close();
            }
        } finally {
        }
    }

    private static void exitWithUsage() {
        System.out.println("Usage: " + LanguageDetectorServerSwitcher.class.getCanonicalName() + ": [start|stop]");
        System.exit(1);
    }

}
