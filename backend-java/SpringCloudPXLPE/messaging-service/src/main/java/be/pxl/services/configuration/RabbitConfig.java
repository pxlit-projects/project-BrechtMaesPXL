package be.pxl.services.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String QUEUE_NAME = "messaging-queue";
    public static final String REJECTER = "reject-queue";

    public static final String EXCHANGE_NAME = "messaging-exchange";
    public static final String ROUTING_KEY = "messaging.routing.key";

    @Bean
    public Queue queueName() {
        return new Queue(QUEUE_NAME, true);
    }
    @Bean
    public Queue rejected() {
        return new Queue(REJECTER, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queueName, DirectExchange exchange) {
        return BindingBuilder.bind(queueName).to(exchange).with(ROUTING_KEY);
    }
}