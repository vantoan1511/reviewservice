package com.shopbee.reviewservice.entity;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MongoEntity(collection = "Review")
public class Review extends PanacheMongoEntity {
    private Integer rating;

    private String title;

    private String text;

    private Integer helpfulCount = 0;

    private Instant createdAt = Instant.now();

    private String username;

    private String productSlug;
}
