package com.serkan.peri.configuration.db;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ForeignKeyNormalizationMigration {

    private final JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void normalizeForeignKeys() {
        ensureForeignKeyOnDeleteSetNull(
                "company",
                "company_admin_id",
                "company_administrator",
                "id",
                "fk_company_company_admin_id"
        );
        ensureForeignKeyOnDeleteSetNull(
                "company",
                "system_admin_id",
                "system_administrator",
                "id",
                "fk_company_system_admin_id"
        );
        ensureForeignKeyOnDeleteSetNull(
                "users",
                "company_id",
                "company",
                "id",
                "fk_users_company_id"
        );
    }

    private void ensureForeignKeyOnDeleteSetNull(
            String tableName,
            String columnName,
            String referencedTableName,
            String referencedColumnName,
            String targetConstraintName
    ) {
        validateIdentifier(tableName);
        validateIdentifier(columnName);
        validateIdentifier(referencedTableName);
        validateIdentifier(referencedColumnName);
        validateIdentifier(targetConstraintName);

        List<String> existingConstraintNames = jdbcTemplate.queryForList(
                """
                        SELECT con.conname
                        FROM pg_constraint con
                        JOIN pg_class rel ON rel.oid = con.conrelid
                        JOIN pg_namespace nsp ON nsp.oid = rel.relnamespace
                        JOIN pg_attribute att ON att.attrelid = rel.oid
                                             AND att.attnum = ANY (con.conkey)
                        WHERE con.contype = 'f'
                          AND nsp.nspname = current_schema()
                          AND rel.relname = ?
                          AND att.attname = ?
                        """,
                String.class,
                tableName,
                columnName
        );

        for (String existingConstraintName : existingConstraintNames) {
            jdbcTemplate.execute(
                    "ALTER TABLE " + tableName + " DROP CONSTRAINT " + existingConstraintName
            );
        }

        jdbcTemplate.execute(
                "ALTER TABLE " + tableName
                        + " ADD CONSTRAINT " + targetConstraintName
                        + " FOREIGN KEY (" + columnName + ") REFERENCES "
                        + referencedTableName + "(" + referencedColumnName + ") ON DELETE SET NULL"
        );
    }

    private void validateIdentifier(String identifier) {
        if (identifier == null || !identifier.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            throw new IllegalArgumentException("Geçersiz SQL identifier: " + identifier);
        }
    }
}

