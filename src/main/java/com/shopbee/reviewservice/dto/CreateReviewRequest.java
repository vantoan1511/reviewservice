package com.shopbee.reviewservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {

    @Range(min = 1, max = 5, message = "Rating must be in range 1-5")
    private Integer rating;

    @NotBlank(message = "Title must not be blank")
    @Length(max = 255, message = "Title length exceeds the limit of 255 characters")
    private String title;

    @NotBlank(message = "Review text must not be blank")
    @Length(max = 1000, message = "Review content length exceeds the limit of 1000 characters")
    private String text;

    @NotBlank(message = "Product slug must not be blank")
    private String productSlug;
}
