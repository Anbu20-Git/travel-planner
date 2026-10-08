package com.travelplanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.travelplanner.entity.Review;
import com.travelplanner.repository.ReviewRepository;
import com.travelplanner.service.ReviewService;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;

    @BeforeEach
    void setUp() {
        review = new Review();
        review.setId(1L);
    }

    @Test
    void saveReview_shouldSaveAndReturnReview() {

        when(reviewRepository.save(review))
                .thenReturn(review);

        Review result =
                reviewService.saveReview(review);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(reviewRepository).save(review);
    }

    @Test
    void getAllReviews_shouldReturnAllReviews() {

        Review secondReview = new Review();
        secondReview.setId(2L);

        when(reviewRepository.findAll())
                .thenReturn(List.of(review, secondReview));

        List<Review> result =
                reviewService.getAllReviews();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(reviewRepository).findAll();
    }

    @Test
    void findById_shouldReturnReviewWhenFound() {

        when(reviewRepository.findById(1L))
                .thenReturn(Optional.of(review));

        Review result =
                reviewService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(reviewRepository).findById(1L);
    }

    @Test
    void findById_shouldReturnNullWhenNotFound() {

        when(reviewRepository.findById(99L))
                .thenReturn(Optional.empty());

        Review result =
                reviewService.findById(99L);

        assertNull(result);

        verify(reviewRepository).findById(99L);
    }

    @Test
    void deleteReview_shouldDeleteById() {

        reviewService.deleteReview(1L);

        verify(reviewRepository).deleteById(1L);
    }
}