package com.njkol.azure.eventhub;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SalesDataReporter implements Runnable {

	private static final int NUM_MESSAGES = 100;
	private final String TOPIC;
	
	private static final List<String> sellers = Arrays.asList("LNK", "OMA", "KC", "DEN");
	private static final List<Product> products = Arrays.asList(new Product("Toothpaste", 4.99),
			new Product("Toothbrush", 3.99), new Product("Dental Floss", 1.99));
	
	private Producer<Long, String> producer;
	private ObjectMapper mapper = new ObjectMapper();
	
	public SalesDataReporter(final Producer<Long, String> producer, String TOPIC) {
		this.producer = producer;
		this.TOPIC = TOPIC;
	}

	@Override
	public void run() {
		for (int i = 0; i < NUM_MESSAGES; i++) {

			try {
				String jsonStr = mapper.writeValueAsString(makeSaleitem());
				System.out.println("From thread #" + Thread.currentThread().getId() + " : "+jsonStr);
				final ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(TOPIC, null,jsonStr);
				producer.send(record, new Callback() {
					public void onCompletion(RecordMetadata metadata, Exception exception) {
						if (exception != null) {
							System.out.println(exception);
							System.exit(1);
						}
					}
				});
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		System.out.println(
				"Finished sending " + NUM_MESSAGES + " messages from thread #" + Thread.currentThread().getId() + "!");
	}
	
	private Sale makeSaleitem() {
		Random rand = new Random();
		String seller = sellers.get(rand.nextInt(sellers.size()));
		int qty = rand.nextInt(1, 5);
		Product product = products.get(rand.nextInt(products.size()));
		Sale sale = new Sale(seller, product.getProduct(), qty, product.getProduct_price(), System.currentTimeMillis());
		return sale;
	}
}