package com.backend.Ecommerce.redis;

import com.backend.Ecommerce.product.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

@Component
public class RedisCacheHelper {

    private final JedisPool jedisPool;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RedisCacheHelper(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public <T> T getOrLoad(String key, TypeReference<T> typeRef, java.util.function.Supplier<T> dbFallback) {
        try (Jedis jedis = jedisPool.getResource()) {
            String cachedData = jedis.get(key);
            if (cachedData != null) {
                return objectMapper.readValue(cachedData, typeRef);
            }

            T value = dbFallback.get();

            if (value instanceof Optional<?> opt) {
                jedis.set(key, objectMapper.writeValueAsString(opt.orElse(null)));
            } else {
                jedis.set(key, objectMapper.writeValueAsString(value));
            }

            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return dbFallback.get();
        }
    }

    public Optional<Product> getOrLoadOptional(String key, TypeReference<Product> typeReference, Supplier<Optional<Product>> dbFallback) {
        try (Jedis jedis = jedisPool.getResource()) {
            String cachedData = jedis.get(key);

            if (cachedData != null) {
                Product product = objectMapper.readValue(cachedData, typeReference);
                return Optional.ofNullable(product);
            }

            Optional<Product> value = dbFallback.get();

            jedis.set(key, objectMapper.writeValueAsString(value.orElse(null)));

            return value;

        } catch (Exception e) {
            e.printStackTrace();
            return dbFallback.get();
        }
    }

    public void cacheProduct(Product product) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set("product_" + product.getId(), objectMapper.writeValueAsString(product));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAllProductsCache(Product newProduct) {
        try (Jedis jedis = jedisPool.getResource()) {
            String cachedData = jedis.get("all_products");
            ArrayList<Product> products;
            if (cachedData != null) {
                products = objectMapper.readValue(cachedData, new TypeReference<ArrayList<Product>>() {});
                products.add(newProduct);
            } else {
                products = new ArrayList<>();
                products.add(newProduct);
            }
            jedis.set("all_products", objectMapper.writeValueAsString(products));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteKey(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
