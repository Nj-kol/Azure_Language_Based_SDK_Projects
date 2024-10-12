package com.njkol.synapse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// https://docs.microsoft.com/en-us/sql/connect/jdbc/step-3-proof-of-concept-connecting-to-sql-using-java?view=sql-server-ver15
public class SynapseClient {

	private static String user;
	private static String pass;

	public static void main(String[] args) {

		String connectionUrl = "jdbc:sqlserver://njkolsqlpool.database.windows.net:1433;database=njkolstandalonededicatedsqlpool;user=njkol@njkolsqlpool;password=@Somnath88;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30";
		ResultSet resultSet = null;

		try (Connection connection = DriverManager.getConnection(connectionUrl);
				Statement statement = connection.createStatement();) {

			// Create and execute a SELECT SQL statement.
			String selectSql = "SELECT TOP (100) * FROM [dbo].[Dimproduct]";
			resultSet = statement.executeQuery(selectSql);

			// Print results from select statement
			while (resultSet.next()) {
				System.out.println(resultSet.getString(2) + " " + resultSet.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
