package br.com.letscode.dynamo.config;

import br.com.letscode.dynamo.model.Tarefa;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@EnableDynamoDBRepositories("br.com.letscode.dynamo.repository")
public class DynamoDBConfig {
    @Value("${aws.access.key.id:fakeId}")
    private String awsAccessKeyId;
    @Value("${aws.access.key.secret:fakeSecret}")
    private String awsAccessKeySecret;
    @Value("${dynamodb.service.endpoint:http://localhost:8000/}")
    private String dynamoDBServiceEndPoint;
    @Value("${dynamodb.service.region:sa-east-1}")
    private String dynamoDBRegion;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(endpointConfiguration())
                .withCredentials(credentialsProvider())
                .build();
    }

    private AWSCredentialsProvider credentialsProvider() {
        return new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(awsAccessKeyId, awsAccessKeySecret)
        );
    }

    private AwsClientBuilder.EndpointConfiguration endpointConfiguration() {
        return new AwsClientBuilder.EndpointConfiguration(dynamoDBServiceEndPoint, dynamoDBRegion);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void setupBD(ApplicationReadyEvent event) {
        AmazonDynamoDB amazonDynamoDB = event.getApplicationContext().getBean(AmazonDynamoDB.class);
        DynamoDBMapper dynamoDBMapper = event.getApplicationContext().getBean(DynamoDBMapper.class);

        CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(Tarefa.class);

        if (!amazonDynamoDB.listTables().getTableNames().contains(createTableRequest.getTableName())) {
            createTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            amazonDynamoDB.createTable(createTableRequest);
        }
    }
}
