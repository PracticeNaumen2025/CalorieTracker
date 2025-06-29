package ru.naumen.calorietracker;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.s3.S3Client;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public abstract class IntegrationTestBase {

    @MockBean
    private S3Client s3Client;

    @MockBean
    private RedisConnectionFactory redisConnectionFactory;

    @Container
    private static final PostgreSQLContainer<?> postgresqlContainer = 
        new PostgreSQLContainer<>("postgres:15-alpine");

    @Container
    private static final ElasticsearchContainer elasticsearchContainer = 
        new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:8.11.3");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);

        registry.add("spring.elasticsearch.uris", elasticsearchContainer::getHttpHostAddress);
    }
}

