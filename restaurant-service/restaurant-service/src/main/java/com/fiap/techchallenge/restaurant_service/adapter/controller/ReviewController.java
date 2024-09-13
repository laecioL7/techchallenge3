package com.fiap.techchallenge.restaurant_service.adapter.controller;

import com.fiap.techchallenge.restaurant_service.adapter.dto.ReviewDto;
import com.fiap.techchallenge.restaurant_service.adapter.mapper.ReviewMapper;
import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.entity.Review;
import com.fiap.techchallenge.restaurant_service.domain.usecase.SubmitReviewUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewMapper reviewMapper;
    private final SubmitReviewUseCase submitReviewUseCase;

    public ReviewController(ReviewMapper reviewMapper, SubmitReviewUseCase submitReviewUseCase) {
        this.reviewMapper = reviewMapper;
        this.submitReviewUseCase = submitReviewUseCase;
    }

    @PostMapping
    public ResponseEntity<String> submitReview(@RequestBody ReviewDto reviewDto) {
        Review review = reviewMapper.toEntity(reviewDto);
        submitReviewUseCase.submitReview(review);
        return ResponseEntity.ok().body("Avaliação realizada com sucesso");
    }
}
