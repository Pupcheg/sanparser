package me.supcheg.sanparser.data.type;

import org.hibernate.HibernateException;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.usertype.EnhancedUserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

abstract class StringConvertingImmutableType<T extends Serializable> implements EnhancedUserType<T> {
    @Override
    public String toSqlLiteral(T value) {
        return String.valueOf(value);
    }

    @Override
    public String toString(T value) throws HibernateException {
        return String.valueOf(value);
    }

    @Override
    public int getSqlType() {
        return SqlTypes.VARCHAR;
    }

    @Override
    public int hashCode(T x) {
        return x == null ? 0 : x.hashCode();
    }

    @Override
    public T nullSafeGet(ResultSet rs, int position, WrapperOptions options) throws SQLException {
        String raw = rs.getString(position);
        return raw == null ? null : fromStringValue(raw);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, T value, int index, WrapperOptions options) throws SQLException {
        if (value == null) {
            st.setNull(index, getSqlType());
            return;
        }

        st.setString(index, toString(value));
    }

    @Override
    public T deepCopy(T value) {
        return value;
    }

    @Override
    public final boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(T value) {
        return value;
    }

    @Override
    public T assemble(Serializable cached, Object owner) {
        @SuppressWarnings("unchecked")
        T cast = (T) cached;
        return cast;
    }
}
