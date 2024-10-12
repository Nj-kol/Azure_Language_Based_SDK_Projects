package com.njkol.azure.eventhub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EventHubProducerDemo {

	private static final String connectionString = "Endpoint=sb://njkolhub.servicebus.windows.net/;SharedAccessKeyName=salesauthorule;SharedAccessKey=JR8ZYBbsH15JU5ZWy3v/Wu6w5tWfDBZXJq9fsElCnYo=;EntityPath=sales";
	private static final String eventHubName = "sales";

	private static final List<String> sellers = Arrays.asList("LNK", "OMA", "KC", "DEN");
	private static final List<Product> products = Arrays.asList(new Product("Toothpaste", 4.99),
			new Product("Toothbrush", 3.99), new Product("Dental Floss", 1.99));

	public static void main(String[] args) throws JsonProcessingException, InterruptedException {
        
		ObjectMapper mapper = new ObjectMapper();
		int noOfMessagestoProduce = 5;
		long messageProdutionInterval = 5000;
		
		while(true) {
			List<EventData> allEvents = new ArrayList<>();
			for (int i = 0; i <= noOfMessagestoProduce ; i++) {
				String jsonStr = mapper.writeValueAsString(makeSaleitem());
				System.out.println(jsonStr);
				allEvents.add(new EventData(jsonStr));
			}
			publishEvents(allEvents);
			System.out.println("Published events batch successfully!");
			allEvents.clear();
			Thread.sleep(messageProdutionInterval);
		}
	}

	public static Sale makeSaleitem() {
		Random rand = new Random();
		String seller = sellers.get(rand.nextInt(sellers.size()));
		int qty = rand.nextInt(1, 5);
		Product product = products.get(rand.nextInt(products.size()));
		Sale sale = new Sale(seller, product.getProduct(), qty, product.getProduct_price(), System.currentTimeMillis());
		return sale;
	}

	/**
	 * Code sample for publishing events.
	 * 
	 * @throws IllegalArgumentException if the EventData is bigger than the max
	 *                                  batch size.
	 */
	public static void publishEvents(List<EventData> allEvents) {

		// create a producer client
		EventHubProducerClient producer = new EventHubClientBuilder().connectionString(connectionString, eventHubName)
				.buildProducerClient();

		// create a batch
		EventDataBatch eventDataBatch = producer.createBatch();

		for (EventData eventData : allEvents) {
			// try to add the event from the array to the batch
			if (!eventDataBatch.tryAdd(eventData)) {
				// if the batch is full, send it and then create a new batch
				producer.send(eventDataBatch);
				eventDataBatch = producer.createBatch();

				// Try to add that event that couldn't fit before.
				if (!eventDataBatch.tryAdd(eventData)) {
					throw new IllegalArgumentException(
							"Event is too large for an empty batch. Max size: " + eventDataBatch.getMaxSizeInBytes());
				}
			}
		}

		// send the last batch of remaining events
		if (eventDataBatch.getCount() > 0) {
			producer.send(eventDataBatch);
		}
		producer.close();
	}
}
