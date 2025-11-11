package com.example.Ecommerce.redis;

import com.example.Ecommerce.product.Product;
import com.example.Ecommerce.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class CacheUpdatePublisher {

    private final AmqpTemplate amqpTemplate;

    public CacheUpdatePublisher(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void publishAddProductEvent(Product product) {
        amqpTemplate.convertAndSend(RabbitMQConfig.CACHE_EXCHANGE, RabbitMQConfig.CACHE_ADD_ROUTING_KEY, product);
    }

    public void publishDeleteProductEvent(Long productId) {
        amqpTemplate.convertAndSend(RabbitMQConfig.CACHE_EXCHANGE, RabbitMQConfig.CACHE_DELETE_ROUTING_KEY, productId);
    }

    public void publishBuyProductEvent(Long productId) {
        amqpTemplate.convertAndSend(RabbitMQConfig.CACHE_EXCHANGE, RabbitMQConfig.CACHE_BUY_ROUTING_KEY, productId);
    }
}