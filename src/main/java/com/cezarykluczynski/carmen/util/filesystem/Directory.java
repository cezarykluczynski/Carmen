package com.cezarykluczynski.carmen.util.filesystem;

import com.cezarykluczynski.carmen.util.exec.*;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class Directory {

    public static Result create(List<String> pathElements) {
        return Executor.execute(new MkDirCommand(joinRelativeDirectories(pathElements)));
    }

    public static Result delete(String directory) {
        return Executor.execute(new RmDirCommand(sanitizePathForOs(directory)));
    }

    public static String sanitizePathForOs(String path) {
        return path.replace("/", "\\" + File.separator);
    }

    public static String convertPathToUnixStyleSlashes(String path) {
        return path.replace("\\", "/");
    }

    protected static String joinRelativeDirectories(List<String> pathElements) {
        StringBuilder dirNameStringBuilder = new StringBuilder();
        Iterator<String> pathElementsIterator = pathElements.iterator();

        while (pathElementsIterator.hasNext()) {
            dirNameStringBuilder.append(pathElementsIterator.next());
            if (pathElementsIterator.hasNext()) {
                dirNameStringBuilder.append(File.separator);
            }
        }

        return sanitizePathForOs(dirNameStringBuilder.toString());
    }

}