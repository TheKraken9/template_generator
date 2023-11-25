package database_details;

import java.util.HashMap;
import java.util.Map;

public class ImportGenerator {

    private static final Map<String, String> IMPORT_MAP = new HashMap<>();

    static {
        IMPORT_MAP.put("timestamp", "java.sql.Timestamp");
        IMPORT_MAP.put("numeric", "java.math.BigDecimal");
        IMPORT_MAP.put("date", "java.sql.Date");
        IMPORT_MAP.put("time", "java.sql.Time");
        IMPORT_MAP.put("bytea", "byte[]");
        IMPORT_MAP.put("text", "String");
        IMPORT_MAP.put("float4", "float");
    }

    private String columnType;

    public ImportGenerator(String columnType) {
        this.columnType = columnType;
    }

    public String getJavaTypeOfColumn() {
        String javaType = IMPORT_MAP.get(columnType.toLowerCase());
        return (javaType != null) ? javaType : "";
    }

    public static void main(String[] args) {
        ImportGenerator generator = new ImportGenerator("numeric");
        System.out.println(generator.getJavaTypeOfColumn());
    }

}
