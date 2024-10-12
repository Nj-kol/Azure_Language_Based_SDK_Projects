package com.njkol.azure.eventhub;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestSalesConsumer implements Runnable {

    private final String TOPIC;
	private ObjectMapper mapper = new ObjectMapper();
	
    //Each consumer needs a unique client ID per thread
    private static int id = 0;

    public TestSalesConsumer(final String TOPIC){
        this.TOPIC = TOPIC;
    }

    public void run (){
        final Consumer<Long, String> consumer = createConsumer();
        System.out.println("Polling");

        try {
            while (true) {
                final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);
                for(ConsumerRecord<Long, String> cr : consumerRecords) {
                    System.out.printf("Consumer Record:(%d, %s, %d, %d)\n", cr.key(), cr.value(), cr.partition(), cr.offset());
                    
                    // Deserialize payload
            	    try {
            			Sale asale = mapper.readValue(cr.value(), Sale.class);
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
                }
                consumer.commitAsync();
            }
        } catch (CommitFailedException e) {
            System.out.println("CommitFailedException: " + e);
        } finally {
            consumer.close();
        }
    }
    
    private Consumer<Long, String> createConsumer() {
        try {
            final Properties properties = new Properties();
            synchronized (TestSalesConsumer.class) {
                properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "KafkaExampleConsumer#" + id);
                id++;
            }
            
            
            //properties.put(ConsumerConfig.GROUP_ID_CONFIG, LongDeserializer.class.getName());
            properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
            properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

            //Get remaining properties from config file
            properties.load(new FileReader("src/main/resources/consumer.config"));

            // Create the consumer using properties.
            final Consumer<Long, String> consumer = new KafkaConsumer<>(properties);

            // Subscribe to the topic.
            consumer.subscribe(Collections.singletonList(TOPIC));
            return consumer;
            
        } catch (FileNotFoundException e){
            System.out.println("FileNotFoundException: " + e);
            System.exit(1);
            return null;        //unreachable
        } catch (IOException e){
            System.out.println("IOException: " + e);
            System.exit(1);
            return null;        //unreachable
        }
    }
}