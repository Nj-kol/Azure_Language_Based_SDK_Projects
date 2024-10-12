package com.azure.cosmos.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.azure.core.http.rest.PagedIterable;
//Include the following imports to use table APIs
import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableClientBuilder;
import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.TableServiceClientBuilder;
import com.azure.data.tables.models.ListEntitiesOptions;
import com.azure.data.tables.models.TableEntity;
import com.azure.data.tables.models.TableEntityUpdateMode;
import com.azure.data.tables.models.TableTransactionAction;
import com.azure.data.tables.models.TableTransactionActionType;

public class NewClient {

	private TableServiceClient tableServiceClient;

	public NewClient(TableServiceClient tableServiceClient) {
		this.tableServiceClient = tableServiceClient;
	}

	public void createTable(String tableName) {
		try {
			// final String tableName = "Employees";
			// Create the table if it not exists.
			TableClient tableClient = tableServiceClient.createTableIfNotExists(tableName);

		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
		}
	}

	public void listTables() {
		try {
			// Loop through a collection of table names.
			tableServiceClient.listTables().forEach(tableItem -> System.out.printf(tableItem.getName()));
		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
		}
	}

	public void addEntity(String tableName) {
		try {

			// Create a TableClient with a connection string and a table name.
			TableClient tableClient = tableServiceClient.getTableClient(tableName);

			// Create a new employee TableEntity.
			String partitionKey = "Sales";
			String rowKey = "0001";

			Map<String, Object> personalInfo = new HashMap<>();
			personalInfo.put("FirstName", "Walter");
			personalInfo.put("LastName", "Harp");
			personalInfo.put("Email", "Walter@contoso.com");
			personalInfo.put("PhoneNumber", "425-555-0101");

			TableEntity employee = new TableEntity(partitionKey, rowKey).setProperties(personalInfo);

			// Upsert the entity into the table
			// tableClient.upsertEntity(employee);

			// Insert the entity into the table
			tableClient.createEntity(employee);

		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
		}
	}

	public void batchInsert(String tableName) {
		try {
			// Create a TableClient with a connection string and a table name.
			TableClient tableClient = tableServiceClient.getTableClient(tableName);

			String partitionKey = "Sales";
			List<TableTransactionAction> tableTransactionActions = new ArrayList<>();

			Map<String, Object> personalInfo1 = new HashMap<>();
			personalInfo1.put("FirstName", "Jeff");
			personalInfo1.put("LastName", "Smith");
			personalInfo1.put("Email", "Jeff@contoso.com");
			personalInfo1.put("PhoneNumber", "425-555-0104");

			// Create an entity to add to the table.
			tableTransactionActions.add(new TableTransactionAction(TableTransactionActionType.UPSERT_MERGE,
					new TableEntity(partitionKey, "0001").setProperties(personalInfo1)));

			Map<String, Object> personalInfo2 = new HashMap<>();
			personalInfo2.put("FirstName", "Ben");
			personalInfo2.put("LastName", "Johnson");
			personalInfo2.put("Email", "Ben@contoso.com");
			personalInfo2.put("PhoneNumber", "425-555-0102");

			// Create another entity to add to the table.
			tableTransactionActions.add(new TableTransactionAction(TableTransactionActionType.UPSERT_MERGE,
					new TableEntity(partitionKey, "0002").setProperties(personalInfo2)));

			Map<String, Object> personalInfo3 = new HashMap<>();
			personalInfo3.put("FirstName", "Denise");
			personalInfo3.put("LastName", "Rivers");
			personalInfo3.put("Email", "Denise@contoso.com");
			personalInfo3.put("PhoneNumber", "425-555-0103");

			// Create a third entity to add to the table.
			tableTransactionActions.add(new TableTransactionAction(TableTransactionActionType.UPSERT_MERGE,
					new TableEntity(partitionKey, "0003").setProperties(personalInfo3)));

			// Submit transaction on the "Employees" table.
			tableClient.submitTransaction(tableTransactionActions);
		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
		}
	}

	public void retrieveAllEntriesInPartition() {
		try {
			// Define constants for filters.
			final String PARTITION_KEY = "PartitionKey";
			final String tableName = "Employees";

			// Create a TableClient with a connection string and a table name.
			TableClient tableClient = tableServiceClient.getTableClient(tableName);

			// Create a filter condition where the partition key is "Sales".
			ListEntitiesOptions options = new ListEntitiesOptions().setFilter(PARTITION_KEY + " eq 'Sales'");

			// Loop through the results, displaying information about the entities.
			PagedIterable<TableEntity> ent = tableClient.listEntities(options, null, null);

			ent.forEach(tableEntity -> {
				System.out.println(tableEntity.getPartitionKey() + " " + tableEntity.getRowKey() + "\t"
						+ tableEntity.getProperty("FirstName") + "\t" + tableEntity.getProperty("LastName") + "\t"
						+ tableEntity.getProperty("Email") + "\t" + tableEntity.getProperty("PhoneNumber"));
			});
		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
		}
	}

	// error
	public void retrieveARangeOfEntriesInPartition() {
		try {
			// Define constants for filters.
			final String PARTITION_KEY = "PartitionKey";
			final String ROW_KEY = "RowKey";
			final String tableName = "Employees";

			// Create a TableClient with a connection string and a table name.
			TableClient tableClient = tableServiceClient.getTableClient(tableName);

			// Note: Seems AND is case sensitive
			// Create a filter condition where the partition key is "Sales".
			ListEntitiesOptions options = new ListEntitiesOptions().setFilter(
					PARTITION_KEY + " eq 'Sales' and " + ROW_KEY + " lt '0004' and " + ROW_KEY + " gt '0001'");

			// Loop through the results, displaying information about the entities.
			tableClient.listEntities(options, null, null).forEach(tableEntity -> {
				System.out.println(tableEntity.getPartitionKey() + " " + tableEntity.getRowKey() + "\t"
						+ tableEntity.getProperty("FirstName") + "\t" + tableEntity.getProperty("LastName") + "\t"
						+ tableEntity.getProperty("Email") + "\t" + tableEntity.getProperty("PhoneNumber"));
			});
		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
		}
	}

	public void retrieveSingle() {
		final String tableName = "Employees";
		// Create a TableClient with a connection string and a table name.
		TableClient tableClient = tableServiceClient.getTableClient(tableName);

		// Get the specific entity.
		TableEntity specificEntity = tableClient.getEntity("Sales", "0001");

		// Output the entity.
		if (specificEntity != null) {
			System.out.println(specificEntity.getPartitionKey() + " " + specificEntity.getRowKey() + "\t"
					+ specificEntity.getProperty("FirstName") + "\t" + specificEntity.getProperty("LastName") + "\t"
					+ specificEntity.getProperty("Email") + "\t" + specificEntity.getProperty("PhoneNumber"));
		}
	}

	public void modifyEntity() {
		try {
			final String tableName = "Employees";

			// Create a TableClient with a connection string and a table name.
			TableClient tableClient = tableServiceClient.getTableClient(tableName);

			// Get the specific entity.
			TableEntity specificEntity = tableClient.getEntity("Sales", "0001");

			// Specify a new phone number
			specificEntity.getProperties().put("PhoneNumber", "425-555-0105");

			// Update the specific entity
			tableClient.updateEntity(specificEntity, TableEntityUpdateMode.REPLACE);
		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
		}

	}

	public void querySubset() {
		try {
			final String tableName = "Employees";

			// Create a TableClient with a connection string and a table name.
			TableClient tableClient = tableServiceClient.getTableClient(tableName);

			// Create a filter condition that retrieves only the Email property.
			List<String> attributesToRetrieve = new ArrayList<>();
			attributesToRetrieve.add("Email");

			ListEntitiesOptions options = new ListEntitiesOptions().setSelect(attributesToRetrieve);

			// Loop through the results, displaying the Email values.
			tableClient.listEntities(options, null, null).forEach(tableEntity -> {
				System.out.println(tableEntity.getProperty("Email"));
			});
		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
		}
	}

	public void testUpsert() {
		try {
			final String tableName = "Employees";

			// Create a TableClient with a connection string and a table name.
			TableClient tableClient = tableServiceClient.getTableClient(tableName);

			// Create a new table entity.
			Map<String, Object> properties = new HashMap<>();
			properties.put("FirstName", "Walter");
			properties.put("LastName", "Harp");
			properties.put("Email", "Walter@contoso.com");
			properties.put("PhoneNumber", "425-555-0101");

			TableEntity newEmployee = new TableEntity("Sales", "0004").setProperties(properties);

			// Add the new customer to the Employees table.
			tableClient.upsertEntity(newEmployee);
		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
		}
	}

	public void deleteEntity() {
		try {
			final String tableName = "Employees";

			// Create a TableClient with a connection string and a table name.
			TableClient tableClient = tableServiceClient.getTableClient(tableName);

			// Delete the entity for partition key 'Sales' and row key '0001' from the
			// table.
			tableClient.deleteEntity("Sales", "0001");
		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
		}
	}

	public void deleteTable() {
		try {
			final String tableName = "Employees";

			// Create a TableClient with a connection string and a table name.
			TableClient tableClient = tableServiceClient.getTableClient(tableName);
			
			// Delete the table and all its data.
			tableClient.deleteTable();
		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
		}
	}
}
