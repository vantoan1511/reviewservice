package com.shopbee.reviewservice.service;

import com.shopbee.reviewservice.converter.ReviewConverter;
import com.shopbee.reviewservice.dto.CreateReviewRequest;
import com.shopbee.reviewservice.dto.ReviewStatistic;
import com.shopbee.reviewservice.entity.Review;
import com.shopbee.reviewservice.external.order.OrderServiceClient;
import com.shopbee.reviewservice.external.order.dto.Order;
import com.shopbee.reviewservice.external.order.dto.OrderStatus;
import com.shopbee.reviewservice.external.order.filter.FilterCriteria;
import com.shopbee.reviewservice.repository.ReviewRepository;
import com.shopbee.reviewservice.shared.exception.ReviewServiceException;
import com.shopbee.reviewservice.shared.page.PageRequest;
import com.shopbee.reviewservice.shared.page.PagedResponse;
import com.shopbee.reviewservice.shared.sort.SortCriteria;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@ApplicationScoped
public class ReviewService {

    private final OrderServiceClient orderServiceClient;
    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;
    private final SecurityIdentity identity;

    @Inject
    public ReviewService(@RestClient OrderServiceClient orderServiceClient,
                         ReviewRepository reviewRepository,
                         ReviewConverter reviewConverter,
                         SecurityIdentity identity) {
        this.orderServiceClient = orderServiceClient;
        this.reviewRepository = reviewRepository;
        this.reviewConverter = reviewConverter;
        this.identity = identity;
    }

    public PagedResponse<Review> getPagedReviewByCriteria(com.shopbee.reviewservice.shared.filter.FilterCriteria filterCriteria,
                                                          PageRequest pageRequest,
                                                          SortCriteria sortCriteria) {
        List<Review> reviews = reviewRepository.findByCriteria(filterCriteria, pageRequest, sortCriteria);
        long totalItems = reviewRepository.countByCriteria(filterCriteria);
        return PagedResponse.of(totalItems, pageRequest, reviews);
    }

    public ReviewStatistic getStatistic(String productSlug) {
        List<Review> reviews = reviewRepository.findByProductSlug(productSlug);
        double averageRating = calculateAverageRating(reviews);
        long totalReviews = reviews.size();
        long totalOneStars = reviews.stream().filter(review -> review.getRating().equals(1)).count();
        long totalTwoStars = reviews.stream().filter(review -> review.getRating().equals(2)).count();
        long totalThreeStars = reviews.stream().filter(review -> review.getRating().equals(3)).count();
        long totalFourStars = reviews.stream().filter(review -> review.getRating().equals(4)).count();
        long totalFiveStars = reviews.stream().filter(review -> review.getRating().equals(5)).count();

        ReviewStatistic statistic = new ReviewStatistic();
        statistic.setAverageRating(averageRating);
        statistic.setTotalReviews(totalReviews);
        statistic.setTotalOneStars(totalOneStars);
        statistic.setTotalTwoStars(totalTwoStars);
        statistic.setTotalThreeStars(totalThreeStars);
        statistic.setTotalFourStars(totalFourStars);
        statistic.setTotalFiveStars(totalFiveStars);
        return statistic;
    }

    public Review create(CreateReviewRequest createReviewRequest) {
        if (hasBoughtProduct(createReviewRequest.getProductSlug())) {
            throw new ReviewServiceException("Must buy product first", Response.Status.METHOD_NOT_ALLOWED);
        }

        if (hasAlreadyReviewed(createReviewRequest.getProductSlug())) {
            throw new ReviewServiceException("You have already reviewed this product", Response.Status.CONFLICT);
        }

        Review review = reviewConverter.convert(createReviewRequest);
        review.setUsername(getCurrentUsername());
        reviewRepository.persist(review);

        return review;
    }

    private double calculateAverageRating(List<Review> reviews) {
        double totalRating = calculateTotalRating(reviews);
        double averageRating = totalRating / reviews.size();
        return roundHalfUp(averageRating);
    }

    private double calculateTotalRating(List<Review> reviews) {
        return reviews.stream().map(Review::getRating).reduce(0, Integer::sum);
    }

    private double roundHalfUp(double raw) {
        return BigDecimal.valueOf(raw).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    private boolean hasAlreadyReviewed(String productSlug) {
        return reviewRepository.findByUsernameAndProductSlug(getCurrentUsername(), productSlug).isPresent();
    }

    private boolean hasBoughtProduct(String productSlug) {
        FilterCriteria filterCriteria = new FilterCriteria();

        filterCriteria.setProductSlug(productSlug);
        filterCriteria.setStatus(OrderStatus.COMPLETED);
        PagedResponse<Order> response = orderServiceClient.getOrders(filterCriteria, null, null);

        return response.getItems().isEmpty();
    }

    private String getCurrentUsername() {
        return identity.getPrincipal().getName();
    }
}
