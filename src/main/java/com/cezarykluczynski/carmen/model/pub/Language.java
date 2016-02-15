package com.cezarykluczynski.carmen.model.pub;

import com.cezarykluczynski.carmen.dao.pub.enums.LinguistLanguageType;
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
    @Column(name = "id")
    private Long id;

    @Column
    private String name;

    @Column(name="linguist_type")
    @Enumerated(EnumType.STRING)
    @Type(type="com.cezarykluczynski.carmen.model.enums.LinguistLanguageTypeEnumType")
    private LinguistLanguageType linguistLanguageType;

    @Column(name="linguist_color")
    private String linguistColor;

    @OneToOne(optional=true)
    @JoinColumn(name="linguist_parent_id", nullable=true)
    private Language linguistParent;

}
