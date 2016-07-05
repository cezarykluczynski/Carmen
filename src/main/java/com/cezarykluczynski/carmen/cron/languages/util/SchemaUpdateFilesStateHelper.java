package com.cezarykluczynski.carmen.cron.languages.util;

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import com.cezarykluczynski.carmen.util.iterator.AnnotationIterator;
import com.cezarykluczynski.carmen.cron.languages.iterator.RefreshableTableIterator;
import com.cezarykluczynski.carmen.cron.languages.iterator.LanguagesIteratorsFactory;
import com.cezarykluczynski.carmen.util.exec.result.Result;
import com.cezarykluczynski.carmen.vcs.git.GitLocal;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SchemaUpdateFilesStateHelper {

    private String classesRelativePrefix = "src/main/java/";

    private String classesSuffix = "java";

    private LanguagesIteratorsFactory languagesIteratorsFactory;

    @Autowired
    public SchemaUpdateFilesStateHelper(LanguagesIteratorsFactory languagesIteratorsFactory) {
        this.languagesIteratorsFactory = languagesIteratorsFactory;
    }

    public SchemaUpdateFilesStateHelper(LanguagesIteratorsFactory languagesIteratorsFactory,
                                        String classesRelativePrefix, String classesSuffix) {
        this(languagesIteratorsFactory);
        this.classesRelativePrefix = classesRelativePrefix;
        this.classesSuffix = classesSuffix;
    }

    public boolean hasFilesChanged() {
        List<String> filesPaths = getAnnotatedFilesPaths();
        Result diffResult = GitLocal.diff(filesPaths);
        return diffResult.isSuccessFul() && StringUtils.trimAllWhitespace(diffResult.getOutput()).length() > 0;
    }

    private List<String> getAnnotatedFilesPaths() {
        AnnotationIterator annotationIterator = languagesIteratorsFactory.createLanguagesAnnotationIterator();
        List<String> filePaths = Lists.newArrayList();

        while(annotationIterator.hasNext()) {
            RefreshableTableIterator refreshableTableIterator = languagesIteratorsFactory
                    .createRefreshableTableIterator(annotationIterator.next());

            while(refreshableTableIterator.hasNext()) {
                RefreshableTable refreshableTable = refreshableTableIterator.next();
                String packagePath = refreshableTable.getBaseClass().getPackage().getName().replaceAll("\\.", "/");
                String className = refreshableTable.getBaseClass().getSimpleName();
                filePaths.add(classesRelativePrefix + packagePath + "/" + className + "." + classesSuffix);
            }
        }

        return filePaths.stream().distinct().collect(Collectors.toList());
    }

}
