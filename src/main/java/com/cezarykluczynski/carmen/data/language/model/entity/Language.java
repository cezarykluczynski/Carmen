package com.cezarykluczynski.carmen.data.language.model.entity;

import com.cezarykluczynski.carmen.data.language.model.entity.enums.LinguistLanguageType;
import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.*;

@Data
@Entity(name = "public.Language")
@Table(schema = "public", name = "languages")
public class Language extends CarmenRelationalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name="linguist_type")
    @Enumerated(EnumType.STRING)
    @Type(type="com.cezarykluczynski.carmen.model.enums.LinguistLanguageTypeEnumType")
    private LinguistLanguageType linguistLanguageType;

    private String linguistColor;

    @OneToOne
    @JoinColumn(name="linguist_parent_id")
    private Language linguistParent;

}
