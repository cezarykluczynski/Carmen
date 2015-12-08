package com.cezarykluczynski.carmen.vcs.git.util;

import org.apache.commons.codec.digest.DigestUtils;

public class DirectoryNameGenerator {

    public static String generateLocationDirectory(String refname) {
        String refnameSha1 = DigestUtils.sha1Hex(refname);
        return refnameSha1.substring(0, 1) + "/" + refnameSha1.substring(0, 2);
    }

}
