package com.hardwaredeals.migration;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import static org.assertj.core.api.Assertions.assertThat;

class MigrationIntegrityTest {
    private static final Pattern INDEX = Pattern.compile("(?i)CREATE\\s+(?:UNIQUE\\s+)?INDEX\\s+([a-z0-9_]+)");

    @Test
    void indexNamesAreUniqueAcrossThePostgresSchema() throws IOException {
        List<String> names = new ArrayList<>();
        try (var files = Files.list(Path.of("src/main/resources/db/migration"))) {
            for (Path file : files.filter(p -> p.toString().endsWith(".sql")).toList()) {
                Matcher matcher = INDEX.matcher(Files.readString(file));
                while (matcher.find()) names.add(matcher.group(1).toLowerCase(Locale.ROOT));
            }
        }
        assertThat(names).doesNotHaveDuplicates();
    }

    @Test
    void productsMigrationEnforcesUniqueEan() throws IOException {
        String migration = Files.readString(Path.of("src/main/resources/db/migration/V1_3__Create_products_table.sql"));
        assertThat(migration).containsPattern("(?i)ean\\s+VARCHAR\\(50\\)\\s+NOT NULL\\s+UNIQUE");
    }
}
