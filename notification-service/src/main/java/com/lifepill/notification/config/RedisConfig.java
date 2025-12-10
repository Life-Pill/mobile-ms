package com.lifepill.notification.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis configuration for pub/sub messaging across multiple notification service instances.
 * This enables WebSocket scaling with Redis as the message broker.
 */
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    public static final String CHANNEL_PRESCRIPTION_NOTIFICATIONS = "notifications:prescriptions";
    public static final String CHANNEL_USER_NOTIFICATIONS = "notifications:users";
    public static final String CHANNEL_BRANCH_NOTIFICATIONS = "notifications:branches";

    private final ObjectMapper objectMapper;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public ChannelTopic prescriptionNotificationChannel() {
        return new ChannelTopic(CHANNEL_PRESCRIPTION_NOTIFICATIONS);
    }

    @Bean
    public ChannelTopic userNotificationChannel() {
        return new ChannelTopic(CHANNEL_USER_NOTIFICATIONS);
    }

    @Bean
    public ChannelTopic branchNotificationChannel() {
        return new ChannelTopic(CHANNEL_BRANCH_NOTIFICATIONS);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter prescriptionMessageListenerAdapter,
            MessageListenerAdapter userMessageListenerAdapter) {
        
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(prescriptionMessageListenerAdapter, prescriptionNotificationChannel());
        container.addMessageListener(userMessageListenerAdapter, userNotificationChannel());
        return container;
    }

    @Bean
    public MessageListenerAdapter prescriptionMessageListenerAdapter(RedisMessageSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "handlePrescriptionMessage");
    }

    @Bean
    public MessageListenerAdapter userMessageListenerAdapter(RedisMessageSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "handleUserMessage");
    }
}
