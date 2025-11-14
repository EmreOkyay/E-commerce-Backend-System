package com.example.Ecommerce;

import com.example.Ecommerce.order.OrderProducer;
import com.example.Ecommerce.redis.CacheUpdatePublisher;
import com.example.Ecommerce.redis.RedisCacheHelper;
import com.example.Ecommerce.registration.user.UserProducer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@Disabled("Skipping contextLoads on CI")
class EcommerceApplicationTests {

	@MockBean
	private RedisCacheHelper redisCacheHelper;

	@MockBean
	private CacheUpdatePublisher cachePublisher;

	@MockBean
	private OrderProducer orderProducer;

	@MockBean
	private UserProducer userProducer;

	@Test
	void contextLoads() {
	}

}
