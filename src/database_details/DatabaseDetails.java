package database_details;

import connecting.Connecting;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DatabaseDetails {
    private String databaseName;
    private String tableName;
    private String columnName;
    private String columnType;
    private String columnSize;
    private String columnNullable;
    private String columnRemarks;

    public DatabaseDetails() {
    }

    public DatabaseDetails(String databaseName, String tableName, String columnName, String columnType, String columnSize, String columnNullable, String columnRemarks) {
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.columnName = columnName;
        this.columnType = columnType;
        this.columnSize = columnSize;
        this.columnNullable = columnNullable;
        this.columnRemarks = columnRemarks;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(String columnSize) {
        this.columnSize = columnSize;
    }

    public String getColumnNullable() {
        return columnNullable;
    }

    public void setColumnNullable(String columnNullable) {
        this.columnNullable = columnNullable;
    }

    public String getColumnRemarks() {
        return columnRemarks;
    }

    public void setColumnRemarks(String columnRemarks) {
        this.columnRemarks = columnRemarks;
    }

    public String toString() {
        return "DatabaseDetails{" +
                "databaseName='" + databaseName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", columnType='" + columnType + '\'' +
                ", columnSize='" + columnSize + '\'' +
                ", columnNullable='" + columnNullable + '\'' +
                ", columnRemarks='" + columnRemarks + '\'' +
                '}';
    }

    public String toCSV() {
        return databaseName + "," + tableName + "," + columnName + "," + columnType + "," + columnSize + "," + columnNullable + "," + columnRemarks;
    }

    public ArrayList<DatabaseDetails> getDatabaseDetailsFromDatabase() throws Exception{
        ArrayList<DatabaseDetails> databaseDetails = new ArrayList<>();
        try {
            Connection connection = Connecting.getConnection("postgres");
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            while (resultSet.next()) {
                String tableName = resultSet.getString(3);
                System.out.println(tableName);
                ResultSet columnsResultSet = metaData.getColumns(null, null, tableName, null);
                while (columnsResultSet.next()) {
                    DatabaseDetails databaseDetail = new DatabaseDetails();
                    databaseDetail.setTableName(columnsResultSet.getString(3));
                    databaseDetail.setColumnName(columnsResultSet.getString(4));
                    databaseDetail.setColumnType(columnsResultSet.getString(6));
                    databaseDetail.setColumnSize(columnsResultSet.getString(7));
                    databaseDetail.setColumnNullable(columnsResultSet.getString(11));
                    databaseDetail.setColumnRemarks(columnsResultSet.getString(12));
                    databaseDetails.add(databaseDetail);

                    System.out.println(databaseDetail.getColumnName() + " " + databaseDetail.getColumnType() + " " + databaseDetail.getColumnSize() + " " + databaseDetail.getColumnNullable() + " " + databaseDetail.getColumnRemarks());
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return databaseDetails;
    }

    public String makeCamelCaseIfContainsUnderscore(String string) {
        if (string.contains("_")) {
            String[] splitString = string.split("_");
            String camelCaseString = "";
            for (int i = 0; i < splitString.length; i++) {
                if (i == 0) {
                    camelCaseString += splitString[i];
                } else {
                    camelCaseString += splitString[i].substring(0, 1).toUpperCase() + splitString[i].substring(1);
                }
            }
            return camelCaseString;
        } else {
            return string;
        }
    }
}
