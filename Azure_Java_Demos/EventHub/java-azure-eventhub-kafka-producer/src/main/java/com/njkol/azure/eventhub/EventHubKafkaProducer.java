package com.njkol.azure.eventhub;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;
import java.io.FileReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventHubKafkaProducer {

	private final static String TOPIC = "salesitem";

	private final static int NUM_THREADS = 4;

	public static void main(String... args) throws Exception {

		// Create Kafka Producer
		final Producer<Long, String> producer = createProducer();

		Thread.sleep(2000);

		final ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

		// Run NUM_THREADS TestDataReporters
		for (int i = 0; i < NUM_THREADS; i++)
			// executorService.execute(new TestDataReporter(producer, TOPIC));
			executorService.execute(new SalesDataReporter(producer, TOPIC));
	}

	private static Producer<Long, String> createProducer() {
		try {
			Properties properties = new Properties();
			properties.load(new FileReader("src/main/resources/producer.config"));
			properties.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
			properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
			properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
			return new KafkaProducer<>(properties);
		} catch (Exception e) {
			System.out.println("Failed to create producer with exception: " + e);
			System.exit(0);
			return null; // unreachable
		}
	}
}