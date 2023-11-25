import connecting.Connecting;
import database_details.DatabaseDetails;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {

        Connection connection = Connecting.getConnection("postgres");
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});
        while (resultSet.next()) {
            String tableName = resultSet.getString(3);
            ArrayList<DatabaseDetails> columns = new ArrayList<>();
            System.out.println(tableName);
            ResultSet columnsResultSet = metaData.getColumns(null, null, tableName, null);
            while (columnsResultSet.next()) {
                DatabaseDetails databaseDetails = new DatabaseDetails();
                databaseDetails.setTableName(columnsResultSet.getString(3));
                databaseDetails.setColumnName(columnsResultSet.getString(4));
                databaseDetails.setColumnType(columnsResultSet.getString(6));
                databaseDetails.setColumnSize(columnsResultSet.getString(7));
                databaseDetails.setColumnNullable(columnsResultSet.getString(11));
                databaseDetails.setColumnRemarks(columnsResultSet.getString(12));
                columns.add(databaseDetails);

                System.out.println(databaseDetails.getColumnName() + " " + databaseDetails.getColumnType() + " " + databaseDetails.getColumnSize() + " " + databaseDetails.getColumnNullable() + " " + databaseDetails.getColumnRemarks());
            }
        }
    }
}