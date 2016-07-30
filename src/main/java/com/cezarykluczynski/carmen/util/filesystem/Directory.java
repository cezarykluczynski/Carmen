package com.cezarykluczynski.carmen.util.filesystem;

import com.cezarykluczynski.carmen.util.OS;
import com.cezarykluczynski.carmen.util.exec.*;
import com.cezarykluczynski.carmen.util.exec.executor.Executor;
import com.cezarykluczynski.carmen.util.exec.result.Result;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class Directory {

    public static Result create(List<String> pathElements) {
        return Executor.execute(new MkDirCommand(sanitizePathForOs(joinRelativeDirectories(pathElements))));
    }

    public static Result delete(String directory) {
        return Executor.execute(new RmDirCommand(sanitizePathForOs(directory)));
    }

    public static String sanitizePathForOs(String path) {
        return OS.isLinux() ? path : path.replace("/", "\\" + File.separator);
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
