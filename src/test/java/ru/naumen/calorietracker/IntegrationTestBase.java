package ru.naumen.calorietracker;

import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.s3.S3Client;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@Testcontainers
@TestPropertySource(properties = {
    "CAL_MINIO_ACCESS_KEY=test-access-key",
    "CAL_MINIO_SECRET_KEY=test-secret-key",
    "CAL_MINIO_URL=http://localhost:9000",
    "CAL_MINIO_EXTERNAL_URL=http://localhost:9000"
})
public abstract class IntegrationTestBase {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public S3Client s3Client() {
            return Mockito.mock(S3Client.class);
        }

        @Bean
        public RedisConnectionFactory redisConnectionFactory() {
            return Mockito.mock(RedisConnectionFactory.class);
        }
    }

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgresqlContainer =
            new PostgreSQLContainer<>("postgres:17");

    @Container
    @ServiceConnection
    private static final ElasticsearchContainer elasticsearchContainer =
            new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:8.17.7")
                    .withEnv("xpack.security.enabled", "false");
}
