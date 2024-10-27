package com.shopbee.reviewservice.api;

import com.shopbee.reviewservice.dto.CreateReviewRequest;
import com.shopbee.reviewservice.service.ReviewService;
import com.shopbee.reviewservice.shared.filter.FilterCriteria;
import com.shopbee.reviewservice.shared.page.PageRequest;
import com.shopbee.reviewservice.shared.sort.SortCriteria;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("reviews")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReviewAPI {

    private final ReviewService reviewService;

    @Inject
    public ReviewAPI(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GET
    public Response getByCriteria(@Valid FilterCriteria filterCriteria,
                                  @Valid PageRequest pageRequest,
                                  @Valid SortCriteria sortCriteria) {
        return Response.ok(reviewService.getPagedReviewByCriteria(filterCriteria, pageRequest, sortCriteria)).build();
    }

    @GET
    @Path("statistic")
    public Response getStatistic(@QueryParam("productSlug") String productSlug) {
        return Response.ok(reviewService.getStatistic(productSlug)).build();
    }

    @POST
    @Authenticated
    public Response create(@Valid CreateReviewRequest createReviewRequest) {
        return Response.status(Response.Status.CREATED).entity(reviewService.create(createReviewRequest)).build();
    }
}
