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
        return Executor.execute(new RmDirCommand(sanitizeForOs(directory)));
    }

    public static String sanitizeForOs(String path) {
        return path.replaceAll("/", "\\" + File.separator);
    }

    protected static String joinRelativeDirectories(List<String> pathElements) {
        StringBuilder dirNameStringBuilder = new StringBuilder();
        Iterator<String> pathElementsIterator = pathElements.iterator();

        while (pathElementsIterator.hasNext()) {
            dirNameStringBuilder.append(sanitizeForOs(pathElementsIterator.next()));
            if (pathElementsIterator.hasNext()) {
                dirNameStringBuilder.append(File.separator);
            }
        }

        return dirNameStringBuilder.toString();
    }

}
