package com.cezarykluczynski.carmen.data.language.model.repository;

import com.cezarykluczynski.carmen.data.language.model.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}
