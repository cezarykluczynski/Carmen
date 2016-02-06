package com.cezarykluczynski.carmen.model.pub;

import com.cezarykluczynski.carmen.dao.pub.enums.LinguistLanguageType;
import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.*;


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

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinguistLanguageType getLinguistLanguageType() {
        return linguistLanguageType;
    }

    public void setLinguistLanguageType(LinguistLanguageType linguistLanguageType) {
        this.linguistLanguageType = linguistLanguageType;
    }

    public String getLinguistColor() {
        return linguistColor;
    }

    public void setLinguistColor(String linguistColor) {
        this.linguistColor = linguistColor;
    }

    public Language getLinguistParent() {
        return linguistParent;
    }

    public void setLinguistParent(Language linguistParent) {
        this.linguistParent = linguistParent;
    }

}
