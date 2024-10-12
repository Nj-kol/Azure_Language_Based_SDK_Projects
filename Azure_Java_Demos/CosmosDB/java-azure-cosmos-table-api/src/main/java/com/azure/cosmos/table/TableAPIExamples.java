package com.azure.cosmos.table;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;



// New API
import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.TableServiceClientBuilder;

public class TableAPIExamples {

	private static String connectionString = "DefaultEndpointsProtocol=https;AccountName=njkol-cosmosdb-table-api;AccountKey=1XKisJGhBwNd1RBqxgv9ttFzkO93eCHEUHZI43XsekHVpeWP812VP1EZJ6AoixsASBuH109BXHzTMsmOD9JMiw==;TableEndpoint=https://njkol-cosmosdb-table-api.table.cosmos.azure.com:443/;";;

	public static void main(String[] args) throws InvalidKeyException, URISyntaxException, IllegalArgumentException,
			IllegalStateException, RuntimeException, IOException {

		testNew();
	}

	private static void testNew() {
		   // Create a TableServiceClient with a connection string.
	    TableServiceClient tableServiceClient = new TableServiceClientBuilder()
	        .connectionString(connectionString)
	        .buildClient();
	    
	    NewClient nc = new NewClient(tableServiceClient);
	    
	    //   nc.createTable("Employees");
	   // nc.listTables();
	    // nc.addEntity("Employees");

	    //nc.batchInsert("Employees");
	   // nc.retrieveAllEntriesInPartition();
	  //  nc.retrieveARangeOfEntriesInPartition();
	   // nc.retrieveSingle();
	   // nc.modifyEntity();
	   // nc.querySubset();
	  // nc.testUpsert();
	//   nc.deleteEntity();
	   nc.deleteTable();
	}
	
	
}
