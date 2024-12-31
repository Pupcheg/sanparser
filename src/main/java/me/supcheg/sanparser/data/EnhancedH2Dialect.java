package me.supcheg.sanparser.data;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.engine.jdbc.Size;
import org.hibernate.metamodel.mapping.SqlExpressible;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.BasicPluralType;
import org.hibernate.type.descriptor.sql.internal.ArrayDdlTypeImpl;
import org.hibernate.type.descriptor.sql.spi.DdlTypeRegistry;

public class EnhancedH2Dialect extends H2Dialect {
    @Override
    protected void registerColumnTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        super.registerColumnTypes(typeContributions, serviceRegistry);
        DdlTypeRegistry ddlTypeRegistry = typeContributions.getTypeConfiguration().getDdlTypeRegistry();

        ddlTypeRegistry.addDescriptor(new TypeFixedArrayDdlTypeImpl(this, false));
    }

    private static class TypeFixedArrayDdlTypeImpl extends ArrayDdlTypeImpl {
        private TypeFixedArrayDdlTypeImpl(Dialect dialect, boolean castRawElementType) {
            super(dialect, castRawElementType);
        }

        @SuppressWarnings("removal")
        @Override
        public String getCastTypeName(Size columnSize, SqlExpressible type, DdlTypeRegistry ddlTypeRegistry) {
            if (!(type instanceof BasicPluralType<?, ?>)) {
                return getCastTypeName(type, columnSize.getLength(), columnSize.getPrecision(), columnSize.getScale());
            }

            return super.getCastTypeName(columnSize, type, ddlTypeRegistry);
        }
    }
}
