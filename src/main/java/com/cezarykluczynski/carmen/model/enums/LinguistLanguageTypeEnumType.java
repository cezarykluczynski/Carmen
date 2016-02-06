package com.cezarykluczynski.carmen.model.enums;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.cezarykluczynski.carmen.dao.pub.enums.LinguistLanguageType;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

/**
 *
 * @author lb@octagen.at
 *
 */
public class LinguistLanguageTypeEnumType extends GenericEnumType<String, LinguistLanguageType> {

    public LinguistLanguageTypeEnumType() throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        super(LinguistLanguageType.class, LinguistLanguageType.values(), "getValue", Types.OTHER);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names,
                              SessionImplementor session, Object owner)
            throws HibernateException, SQLException {
        return nullSafeGet(rs, names, owner);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index,
                            SessionImplementor session) throws HibernateException, SQLException {
        nullSafeSet(st, value, index);
    }

}
