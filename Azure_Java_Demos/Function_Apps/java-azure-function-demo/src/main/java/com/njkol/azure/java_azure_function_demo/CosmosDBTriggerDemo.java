package com.njkol.azure.java_azure_function_demo;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.CosmosDBTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;

/**
 * Azure Functions with CosmosDB Trigger.
 */
public class CosmosDBTriggerDemo {

	private final String triggerName ="myTrigger";
    private final String databaseName = "testDatabase";
    private final String containerName = "products";
	
	@FunctionName("cosmosDBMonitor")
    public void cosmosDbProcessor(
            @CosmosDBTrigger(name = triggerName,
            databaseName = databaseName, collectionName = containerName,
            createLeaseCollectionIfNotExists = true,
            connectionStringSetting = "AzureCosmosDBConnection") String[] items,
            final ExecutionContext context) {
		
        for (String string : items) {
            System.out.println(string);
        }
        context.getLogger().info(items.length + "item(s) is/are changed.");
    }
}
