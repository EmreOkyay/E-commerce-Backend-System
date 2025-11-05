package com.example.Ecommerce.security.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Order Queue
    public static final String ORDER_QUEUE = "orderQueue";
    public static final String ORDER_EXCHANGE = "orderExchange";
    public static final String ORDER_ROUTING_KEY = "orderRoutingKey";

    // User Queue
    public static final String USER_QUEUE = "userQueue";
    public static final String USER_EXCHANGE = "userExchange";
    public static final String USER_ROUTING_KEY = "userRoutingKey";

    // ------------------ Order Queue ------------------ //

    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, false);
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(ORDER_ROUTING_KEY);
    }

    // ------------------ User Queue ------------------ //

    @Bean
    public Queue userQueue() {
        return new Queue(USER_QUEUE, false);
    }

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    @Bean
    public Binding userBinding(Queue userQueue, TopicExchange userExchange) {
        return BindingBuilder.bind(userQueue).to(userExchange).with(USER_ROUTING_KEY);
    }
}
