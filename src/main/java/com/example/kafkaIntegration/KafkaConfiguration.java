package com.example.kafkaIntegration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.kafka.dsl.KafkaInboundChannelAdapterSpec;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConsumerProperties;

@Configuration
@Slf4j
public class KafkaConfiguration {

    @Bean
    public KafkaInboundChannelAdapterSpec<String, String> kafkaAdapter(ConsumerFactory<String, String> cf) {
        log.info("Create kafka adapter");
        final ConsumerProperties consumerProperties = new ConsumerProperties("iso20022.fin.spain");
        consumerProperties.setGroupId("iso20022integration");
        return Kafka.inboundChannelAdapter(
                cf,
                consumerProperties
        );
    }

}
