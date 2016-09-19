package com.cezarykluczynski.carmen.vcs.git.util;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.RepositoryClone;
import com.cezarykluczynski.carmen.util.filesystem.Directory;
import com.cezarykluczynski.carmen.vcs.server.Server;
import org.apache.commons.lang3.StringUtils;

public class DirectoryNameBuilder {

    public static String buildCloneDirectory(Server server, RepositoryClone repositoryClone) {
        return DirectoryNameBuilder.joinDirectoryParts(server.getCloneRoot(),
                repositoryClone.getLocationDirectory(),
                repositoryClone.getLocationSubdirectory());
    }

    private static String joinDirectoryParts(String... directoryParts) {
        return Directory.convertPathToUnixStyleSlashes(StringUtils.join(directoryParts, "/"));
    }

}
