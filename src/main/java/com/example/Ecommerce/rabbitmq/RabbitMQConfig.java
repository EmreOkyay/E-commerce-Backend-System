package com.example.Ecommerce.rabbitmq;

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

    // ------------------ Cache Queue ------------------ //

    public static final String CACHE_ADD_QUEUE = "cacheQueue.add";
    public static final String CACHE_DELETE_QUEUE = "cacheQueue.delete";
    public static final String CACHE_BUY_QUEUE = "cacheQueue.buy";

    public static final String CACHE_EXCHANGE = "cacheExchange";

    public static final String CACHE_ADD_ROUTING_KEY = "product.add";
    public static final String CACHE_DELETE_ROUTING_KEY = "product.delete";
    public static final String CACHE_BUY_ROUTING_KEY = "product.buy";

    @Bean
    public Queue cacheAddQueue() {
        return new Queue(CACHE_ADD_QUEUE, false);
    }

    @Bean
    public Queue cacheDeleteQueue() {
        return new Queue(CACHE_DELETE_QUEUE, false);
    }

    @Bean
    public Queue cacheBuyQueue() {
        return new Queue(CACHE_BUY_QUEUE, false);
    }

    @Bean
    public TopicExchange cacheExchange() {
        return new TopicExchange(CACHE_EXCHANGE);
    }

    @Bean
    public Binding cacheAddBinding(Queue cacheAddQueue, TopicExchange cacheExchange) {
        return BindingBuilder.bind(cacheAddQueue).to(cacheExchange).with(CACHE_ADD_ROUTING_KEY);
    }

    @Bean
    public Binding cacheDeleteBinding(Queue cacheDeleteQueue, TopicExchange cacheExchange) {
        return BindingBuilder.bind(cacheDeleteQueue).to(cacheExchange).with(CACHE_DELETE_ROUTING_KEY);
    }

    @Bean
    public Binding cacheBuyBinding(Queue cacheBuyQueue, TopicExchange cacheExchange) {
        return BindingBuilder.bind(cacheBuyQueue).to(cacheExchange).with(CACHE_BUY_ROUTING_KEY);
    }
}
