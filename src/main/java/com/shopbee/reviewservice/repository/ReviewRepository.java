package com.shopbee.reviewservice.repository;

import com.mongodb.client.model.Filters;
import com.shopbee.reviewservice.entity.Review;
import com.shopbee.reviewservice.shared.filter.FilterCriteria;
import com.shopbee.reviewservice.shared.page.PageRequest;
import com.shopbee.reviewservice.shared.sort.SortCriteria;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

@ApplicationScoped
public class ReviewRepository implements PanacheMongoRepository<Review> {

    public List<Review> findByCriteria(FilterCriteria filterCriteria,
                                       PageRequest pageRequest,
                                       SortCriteria sortCriteria) {
        String filter = buildQuery(filterCriteria).toBsonDocument().toJson();

        Sort sort = sortBy(sortCriteria);

        return find(filter, sort).page(pageRequest.getPage() - 1, pageRequest.getSize()).list();
    }

    public Optional<Review> findByUsernameAndProductSlug(String username, String productSlug) {
        return findByProductSlug(productSlug).stream().filter(review -> review.getUsername().equals(username)).findFirst();
    }

    public List<Review> findByProductSlug(String productSlug) {
        return find("productSlug", productSlug).list();
    }

    public long countByCriteria(FilterCriteria filterCriteria) {
        String filter = buildQuery(filterCriteria).toBsonDocument().toJson();
        return count(filter);
    }

    private Bson buildQuery(FilterCriteria filterCriteria) {
        List<Bson> filters = new ArrayList<>();

        if (StringUtils.isNotBlank(filterCriteria.getProductSlug())) {
            filters.add(Filters.eq("productSlug", filterCriteria.getProductSlug()));
        }

        if (Objects.nonNull(filterCriteria.getRating())) {
            filters.add(Filters.eq("rating", filterCriteria.getRating()));
        }

        return filters.isEmpty() ? Filters.empty() : Filters.and(filters);
    }

    private Sort sortBy(SortCriteria sortCriteria) {
        Sort.Direction direction = sortCriteria.isAscending() ? Sort.Direction.Ascending : Sort.Direction.Descending;
        return Sort.by(sortCriteria.getSortBy().getColumn(), direction);
    }
}
