package com.travelplanner.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DestinationTest {

    @Test
    void defaultConstructor_shouldCreateDestination() {

        Destination destination =
                new Destination();

        assertEquals(
                null,
                destination.getId()
        );

        assertEquals(
                null,
                destination.getName()
        );

        assertEquals(
                null,
                destination.getCountry()
        );

        assertEquals(
                null,
                destination.getDescription()
        );

        assertEquals(
                null,
                destination.getImageUrl()
        );

        assertEquals(
                null,
                destination.getRating()
        );
    }

    @Test
    void parameterizedConstructor_shouldSetAllFields() {

        Destination destination =
                new Destination(
                        "Chennai",
                        "India",
                        "A beautiful city",
                        "chennai.jpg",
                        4.5
                );

        assertEquals(
                "Chennai",
                destination.getName()
        );

        assertEquals(
                "India",
                destination.getCountry()
        );

        assertEquals(
                "A beautiful city",
                destination.getDescription()
        );

        assertEquals(
                "chennai.jpg",
                destination.getImageUrl()
        );

        assertEquals(
                4.5,
                destination.getRating()
        );
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {

        Destination destination =
                new Destination();

        destination.setId(1L);
        destination.setName("Paris");
        destination.setCountry("France");
        destination.setDescription("City of lights");
        destination.setImageUrl("paris.jpg");
        destination.setRating(4.8);

        assertEquals(
                1L,
                destination.getId()
        );

        assertEquals(
                "Paris",
                destination.getName()
        );

        assertEquals(
                "France",
                destination.getCountry()
        );

        assertEquals(
                "City of lights",
                destination.getDescription()
        );

        assertEquals(
                "paris.jpg",
                destination.getImageUrl()
        );

        assertEquals(
                4.8,
                destination.getRating()
        );
    }
}