package com.cezarykluczynski.carmen.cron.languages.api;

import com.cezarykluczynski.carmen.cron.languages.filter.AllFieldsFilter;
import com.cezarykluczynski.carmen.cron.languages.filter.LanguageFieldsFilter;
import com.cezarykluczynski.carmen.cron.languages.filter.LanguageDiffFieldsFilter;

import java.util.SortedSet;

public interface FieldsFilter {

    FieldsFilter ALL = new AllFieldsFilter();

    FieldsFilter LANGUAGE_DIFF_CURRENT = new LanguageDiffFieldsFilter();

    FieldsFilter LANGUAGE_CURRENT = new LanguageFieldsFilter();

    SortedSet<String> filterFields(SortedSet<String> fields);

}
