package com.cezarykluczynski.carmen.util;

public class OS {

    private static String os = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return os.indexOf("windows") == 0;
    }

    public static boolean isLinux() {
        return os.indexOf("linux") == 0;
    }

}
