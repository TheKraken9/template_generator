package database_details;

import java.util.HashMap;
import java.util.Map;

public class DataTypeGenerator {
    private static final Map<String, String> TYPE_MAP = new HashMap<>();

    static {
        TYPE_MAP.put("int4", "int");
        TYPE_MAP.put("varchar", "String");
        TYPE_MAP.put("timestamp", "Timestamp");
        TYPE_MAP.put("bool", "boolean");
        TYPE_MAP.put("float8", "double");
        TYPE_MAP.put("int8", "long");
        TYPE_MAP.put("int2", "int");
        TYPE_MAP.put("serial", "int");
        TYPE_MAP.put("int1", "int");
        TYPE_MAP.put("numeric", "BigDecimal");
        TYPE_MAP.put("date", "Date");
        TYPE_MAP.put("time", "Time");
        TYPE_MAP.put("bytea", "byte[]");
        TYPE_MAP.put("text", "String");
        TYPE_MAP.put("float4", "float");
    }

    private String columnType;

    public DataTypeGenerator(String columnType) {
        this.columnType = columnType;
    }

    public String getJavaTypeOfColumn() {
        String javaType = TYPE_MAP.get(columnType.toLowerCase());
        return (javaType != null) ? javaType : "";
    }

    public static void main(String[] args) {
        DataTypeGenerator generator = new DataTypeGenerator("int4");
        System.out.println(generator.getJavaTypeOfColumn());
    }
}
