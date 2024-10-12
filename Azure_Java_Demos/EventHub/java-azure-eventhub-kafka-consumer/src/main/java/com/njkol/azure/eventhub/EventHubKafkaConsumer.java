package com.njkol.azure.eventhub;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventHubKafkaConsumer {
	
    //Change constant to send messages to the desired topic
    private final static String TOPIC = "salesitem";
    
    private final static int NUM_THREADS = 4;

    public static void main(String... args) throws Exception {

        final ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++){
           // executorService.execute(new TestConsumerThread(TOPIC));
        	 executorService.execute(new TestSalesConsumer(TOPIC));
        }
    }
}