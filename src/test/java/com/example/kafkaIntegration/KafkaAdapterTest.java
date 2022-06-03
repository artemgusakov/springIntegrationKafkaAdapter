package com.example.kafkaIntegration;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.dsl.KafkaInboundChannelAdapterSpec;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@EmbeddedKafka(topics = {
        "iso20022.fin.spain"
})
@Slf4j
@ActiveProfiles("test")
public class KafkaAdapterTest {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    @SneakyThrows
    public void kafkaAdapterTest() {
        final ProducerRecord<String, String> sendMessage = kafkaTemplate
                .send("iso20022.fin.spain", "Message from kafka topic")
                .get()
                .getProducerRecord();
        log.info("Sent message '{}' to '{}' kafka topic", "Message from kafka topic", sendMessage.topic());
    }


    @TestConfiguration
    public static class Config {

        @Bean
        public IntegrationFlow integrationFlow(KafkaInboundChannelAdapterSpec<String, String> kafkaAdapter) {
            return IntegrationFlows
                    .from(kafkaAdapter)
                    .handle(message -> {
                        log.info("Got message from kafka {}", message.getPayload());
                        assertThat(message.getPayload())
                                .isEqualTo("Message from kafka topic");
                    })
                    .get();
        }

    }

}
