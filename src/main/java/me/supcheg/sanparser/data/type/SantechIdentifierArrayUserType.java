package me.supcheg.sanparser.data.type;

import me.supcheg.sanparser.id.SantechIdentifier;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.SqlTypes;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class SantechIdentifierArrayUserType implements UserType<SantechIdentifier[]> {
    @Override
    public int getSqlType() {
        return SqlTypes.ARRAY;
    }

    @Override
    public Class<SantechIdentifier[]> returnedClass() {
        return SantechIdentifier[].class;
    }

    @Override
    public boolean equals(SantechIdentifier[] x, SantechIdentifier[] y) {
        return Arrays.equals(x, y);
    }

    @Override
    public int hashCode(SantechIdentifier[] x) {
        return Arrays.hashCode(x);
    }

    @Override
    public SantechIdentifier[] nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        Array array = rs.getArray(position);
        if (array == null) {
            return null;
        }

        return Arrays.stream(((Object[]) array.getArray()))
                .map(String::valueOf)
                .map(SantechIdentifier::fromString)
                .toArray(SantechIdentifier[]::new);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, SantechIdentifier[] value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, SqlTypes.ARRAY);
            return;
        }

        String[] stringArray = Arrays.stream(value)
                .map(SantechIdentifier::toString)
                .toArray(String[]::new);

        Array array = session.getJdbcCoordinator().getLogicalConnection().getPhysicalConnection()
                .createArrayOf("VARCHAR", stringArray);

        st.setArray(index, array);
    }

    @Override
    public SantechIdentifier[] deepCopy(SantechIdentifier[] value) {
        return Arrays.copyOf(value, value.length);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(SantechIdentifier[] value) {
        return value;
    }

    @Override
    public SantechIdentifier[] assemble(Serializable cached, Object owner) {
        return (SantechIdentifier[]) cached;
    }
}
