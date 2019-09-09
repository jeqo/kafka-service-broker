package io.confluent.examples.pcf.servicebroker;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Slf4j
class ServiceInstanceRepositoryTest {

    @Value("${service.plan.standard.uuid}")
    private UUID servicePlanId;

    @Autowired
    private ServiceInstanceRepository serviceInstanceRepository;

    @BeforeClass
    static void before() throws ExecutionException, InterruptedException {
        TestUtils.recreateServiceInstancesTopic();
    }

    @Test
    public void testSave() throws InterruptedException, ExecutionException, JsonProcessingException {
        TopicServiceInstance topicServiceInstance = TopicServiceInstance.builder().topicName(UUID.randomUUID().toString()).uuid(UUID.randomUUID()).created(new Date()).build();
        serviceInstanceRepository.save(topicServiceInstance);
    }

    @Test
    public void testSaveAndList() throws InterruptedException, ExecutionException, JsonProcessingException {
        UUID uuid = UUID.randomUUID();
        TopicServiceInstance topicServiceInstance = TopicServiceInstance.builder().topicName(UUID.randomUUID().toString()).uuid(uuid).created(new Date()).build();
        serviceInstanceRepository.save(topicServiceInstance);
        Thread.sleep(100);
        TopicServiceInstance stored = serviceInstanceRepository.get(uuid);
        assertNotNull(stored);
    }

    @Test
    public void testSaveAndDelete() throws InterruptedException, ExecutionException, JsonProcessingException {
        UUID uuid = UUID.randomUUID();
        TopicServiceInstance topicServiceInstance = TopicServiceInstance.builder()
                .topicName(UUID.randomUUID().toString())
                .uuid(uuid)
                .created(new Date())
                .planId(servicePlanId)
                .build();

        serviceInstanceRepository.save(topicServiceInstance);
        Thread.sleep(50);
        assertNotNull(serviceInstanceRepository.get(uuid));
        serviceInstanceRepository.delete(uuid);
        Thread.sleep(50);
        assertNull(serviceInstanceRepository.get(uuid));
        serviceInstanceRepository.delete(uuid);
    }

}

