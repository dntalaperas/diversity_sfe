package eu.ubitech.sma.aggregator.main;

import com.mongodb.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;

@Configuration
@EnableMongoRepositories(" eu.ubitech.sma.repository.dao")
public class MongoConfig extends AbstractMongoConfiguration
{
    @Value("${mongodb.name}")
    private String  dbName;

    @Value("${mongodb.host}")
    private String  host;

    @Value("${mongodb.port}")
    private Integer port;

    @Value("${mongodb.username}")
    private String  userName;

    @Value("${mongodb.password}")
    private String  password;


    @Override
    protected String getDatabaseName()
    {
        return this.dbName;
    }

    @Override
    public Mongo mongo() throws Exception
    {
        return new MongoClient(this.host, this.port);
    }


    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {

        // Set credentials
        MongoCredential credential = MongoCredential.createCredential(userName, dbName, password.toCharArray());
        ServerAddress serverAddress = new ServerAddress(host, port);

        // Mongo Client
        MongoClient mongoClient = new MongoClient(serverAddress, Arrays.asList(credential));

        // Mongo DB Factory
        SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(
                mongoClient, dbName);

        return simpleMongoDbFactory;
    }
    @Override
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }

}