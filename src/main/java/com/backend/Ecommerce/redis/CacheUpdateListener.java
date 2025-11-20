package com.backend.Ecommerce.redis;

import com.backend.Ecommerce.product.Product;
import com.backend.Ecommerce.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CacheUpdateListener {

    private final RedisCacheHelper redisCacheHelper;

    public CacheUpdateListener(RedisCacheHelper redisCacheHelper) {
        this.redisCacheHelper = redisCacheHelper;
    }

    @RabbitListener(queues = RabbitMQConfig.CACHE_ADD_QUEUE)
    public void handleAddProduct(Product product) {
        redisCacheHelper.updateAllProductsCache(product);
        redisCacheHelper.cacheProduct(product);
    }

    @RabbitListener(queues = RabbitMQConfig.CACHE_DELETE_QUEUE)
    public void handleDeleteProduct(Long productId) {
        redisCacheHelper.deleteKey("all_products");
        redisCacheHelper.deleteKey("product_" + productId);
    }

    @RabbitListener(queues = RabbitMQConfig.CACHE_BUY_QUEUE)
    public void handleBuyProduct(Long productId) {
        redisCacheHelper.deleteKey("all_products");
        redisCacheHelper.deleteKey("product_" + productId);
    }
}
