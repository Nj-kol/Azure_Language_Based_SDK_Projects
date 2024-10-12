package com.njkol.azure.eventhub;

import com.azure.messaging.eventhubs.*;
import com.azure.messaging.eventhubs.checkpointstore.blob.BlobCheckpointStore;
import com.azure.messaging.eventhubs.models.*;
import com.azure.storage.blob.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.function.Consumer;

public class EventHubConsumerDemo {

	private static final String connectionString = "Endpoint=sb://njkolhub.servicebus.windows.net/;SharedAccessKeyName=salesauthorule;SharedAccessKey=JR8ZYBbsH15JU5ZWy3v/Wu6w5tWfDBZXJq9fsElCnYo=;EntityPath=sales";
	private static final String eventHubName = "sales";
	
	private static final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=njkolbasic;AccountKey=xuN9HtRX9wwV6bxPuDGdYdZDm0SBJNNK0K27s60zGCStDWOvRXrtafCG1cjzFfCpPqXj2EPiHq32MIj6KTmN9w==;EndpointSuffix=core.windows.net";
	private static final String storageContainerName = "checkpointjavacontainer";
	
	public static void main(String[] args) throws IOException {
		
		// Create a blob container client that you use later to build an event processor client to receive and process events
	    BlobContainerAsyncClient blobContainerAsyncClient = new BlobContainerClientBuilder()
	        .connectionString(storageConnectionString)
	        .containerName(storageContainerName)
	        .buildAsyncClient();

	    // Create a builder object that you will use later to build an event processor client to receive and process events and errors.
	    EventProcessorClientBuilder eventProcessorClientBuilder = new EventProcessorClientBuilder()
	        .connectionString(connectionString, eventHubName)
	        .consumerGroup(EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME)
	        .processEvent(PARTITION_PROCESSOR)
	        .processError(ERROR_HANDLER)
	        .checkpointStore(new BlobCheckpointStore(blobContainerAsyncClient));

	    // Use the builder object to create an event processor client
	    EventProcessorClient eventProcessorClient = eventProcessorClientBuilder.buildEventProcessorClient();

	    System.out.println("Starting event processor");
	    eventProcessorClient.start();

	    System.out.println("Press enter to stop.");
	    System.in.read();

	    System.out.println("Stopping event processor");
	    eventProcessorClient.stop();
	    System.out.println("Event processor stopped.");

	    System.out.println("Exiting process");
	}
	
	public static final Consumer<EventContext> PARTITION_PROCESSOR = eventContext -> {
	
		ObjectMapper mapper = new ObjectMapper();
		
		int checkpointEvents = 5;
		PartitionContext partitionContext = eventContext.getPartitionContext();
	    EventData eventData = eventContext.getEventData();

	    System.out.printf("Processing event from partition %s with sequence number %d with body: %s%n",
	        partitionContext.getPartitionId(), eventData.getSequenceNumber(), eventData.getBodyAsString());

	    // Deserialize payload
	    try {
			Sale asale = mapper.readValue(eventData.getBodyAsString(), Sale.class);
		    System.out.println("Seller Id : " + asale.getSeller_id());
		    System.out.println("Product name : " + asale.getProduct());
		    System.out.println("Product quantity : " + asale.getQuantity());
		    System.out.println("Product price : " + asale.getProduct_price());
		    System.out.println("Sale timestamp : " + asale.getSale_ts());
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	    
	    // Every checkpointEvents events received, it will update the checkpoint stored in Azure Blob Storage.
	    if (eventData.getSequenceNumber() % checkpointEvents == 0) {
	        eventContext.updateCheckpoint();
	    }
	};

	public static final Consumer<ErrorContext> ERROR_HANDLER = errorContext -> {
	    System.out.printf("Error occurred in partition processor for partition %s, %s.%n",
	        errorContext.getPartitionContext().getPartitionId(),
	        errorContext.getThrowable());
	};
}
