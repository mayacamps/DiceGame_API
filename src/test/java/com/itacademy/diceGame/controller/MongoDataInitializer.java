package com.itacademy.diceGame.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MongoDataInitializer {
    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void initializeTestData() throws IOException {
        String testCollectionName = "game_histories";
        if (mongoTemplate.collectionExists(testCollectionName)) {
            mongoTemplate.dropCollection(testCollectionName);
            System.out.println("Dropped existing MongoDB test collection.");
        }
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test_data.json");
        List<Document> testData = new ObjectMapper().readValue(inputStream, new TypeReference<List<Document>>() {});
        testData.forEach(document -> mongoTemplate.insert(document, testCollectionName));
        System.out.println("Test data successfully initialized in MongoDB collection.");
    }
}
