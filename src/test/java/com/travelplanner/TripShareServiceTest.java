package com.travelplanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.travelplanner.entity.Trip;
import com.travelplanner.entity.TripShare;
import com.travelplanner.repository.TripShareRepository;
import com.travelplanner.service.TripShareService;

@ExtendWith(MockitoExtension.class)
class TripShareServiceTest {

    @Mock
    private TripShareRepository tripShareRepository;

    @InjectMocks
    private TripShareService tripShareService;

    private Trip trip;

    @BeforeEach
    void setUp() {
        trip = new Trip();
        trip.setId(1L);
    }

    @Test
    void createShare_shouldCreateAndSaveShare() {

        TripShare savedShare = new TripShare();

        when(tripShareRepository.save(any(TripShare.class)))
                .thenReturn(savedShare);

        TripShare result =
                tripShareService.createShare(trip);

        assertNotNull(result);

        verify(tripShareRepository).save(any(TripShare.class));
    }

    @Test
    void findByShareCode_shouldReturnShareWhenFound() {

        TripShare tripShare = new TripShare();

        when(tripShareRepository.findByShareCode("ABC123"))
                .thenReturn(Optional.of(tripShare));

        Optional<TripShare> result =
                tripShareService.findByShareCode("ABC123");

        assertTrue(result.isPresent());
        assertSame(tripShare, result.get());

        verify(tripShareRepository)
                .findByShareCode("ABC123");
    }
}