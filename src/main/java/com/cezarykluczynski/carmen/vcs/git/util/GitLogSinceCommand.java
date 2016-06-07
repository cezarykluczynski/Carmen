package com.cezarykluczynski.carmen.vcs.git.util;

import com.cezarykluczynski.carmen.util.DateUtil;
import com.cezarykluczynski.carmen.util.exec.Command;
import com.google.common.base.Joiner;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.stream.Collectors;

public class GitLogSinceCommand extends Command {

    public GitLogSinceCommand(String directory, Date since) {
        super(doExecute(directory, since));
//        super("pushd " + directory + " && git log --pretty=%H,%ct,%ae --date-order --reverse --since=" +
//                DateUtil.toGitReadableDateTime(since) + " && popd");
    }

    public static String doExecute(String directory, Date since) {
        ProcessBuilder pb = new ProcessBuilder("git", "log", "--pretty=%H,%ct,%ae", "--date-order", "--reverse",
                        "--since=" + DateUtil.toGitReadableDateTime(since));
        pb.directory(new File(directory));
        try {
            Process p = pb.start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            String result = Joiner.on(System.getProperty("line.separator"))
                    .join(reader.lines().collect(Collectors.toList()));
            System.out.println(result);
        } catch (IOException e) {
            //
        }

        return "git log -1";
    }


}
