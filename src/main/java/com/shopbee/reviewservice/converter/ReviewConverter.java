package com.shopbee.reviewservice.converter;

import com.shopbee.reviewservice.dto.CreateReviewRequest;
import com.shopbee.reviewservice.entity.Review;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReviewConverter implements IConverter<CreateReviewRequest, Review> {

    @Override
    public Review convert(CreateReviewRequest createReviewRequest) {
        if (createReviewRequest == null) {
            return null;
        }

        Review review = new Review();
        review.setText(createReviewRequest.getText());
        review.setTitle(createReviewRequest.getTitle());
        review.setRating(createReviewRequest.getRating());
        review.setProductSlug(createReviewRequest.getProductSlug());
        return review;
    }

    @Override
    public CreateReviewRequest reverse(Review review) {
        return null;
    }
}
