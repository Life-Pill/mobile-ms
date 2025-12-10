package com.lifepill.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange (same as prescription service)
    public static final String EXCHANGE = "prescription.exchange";
    
    // Queues
    public static final String QUEUE_NOTIFICATION = "prescription.notification.queue";
    public static final String QUEUE_USER_NOTIFICATION = "prescription.user.notification.queue";
    
    // Routing Keys
    public static final String ROUTING_KEY_UPLOADED = "prescription.uploaded";
    public static final String ROUTING_KEY_RESPONSE = "prescription.response";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(QUEUE_NOTIFICATION).build();
    }
    
    @Bean
    public Queue userNotificationQueue() {
        return QueueBuilder.durable(QUEUE_USER_NOTIFICATION).build();
    }

    @Bean
    public Binding prescriptionUploadedBinding(Queue notificationQueue, TopicExchange exchange) {
        return BindingBuilder.bind(notificationQueue).to(exchange).with(ROUTING_KEY_UPLOADED);
    }
    
    @Bean
    public Binding prescriptionResponseBinding(Queue userNotificationQueue, TopicExchange exchange) {
        return BindingBuilder.bind(userNotificationQueue).to(exchange).with(ROUTING_KEY_RESPONSE);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
