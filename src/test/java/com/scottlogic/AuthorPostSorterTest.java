package com.scottlogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthorPostSorterTest {

    private final OffsetDateTime dateTimeA = OffsetDateTime.of(
            2022, 1, 20, 12, 15, 11, 0, ZoneOffset.UTC);
    private final OffsetDateTime dateTimeB = OffsetDateTime.of(
            2022, 1, 21, 13, 11, 11, 0, ZoneOffset.UTC);
    private final OffsetDateTime dateTimeC = OffsetDateTime.of(
            2022, 1, 22, 14, 17, 11, 0, ZoneOffset.UTC);

    private final UserPost userA = new UserPost("Gene", dateTimeA, "", 0);
    private final UserPost userB = new UserPost("Louise", dateTimeB, "", 0);
    private final UserPost userC = new UserPost("Tina", dateTimeC, "", 0);

    private final List<UserPost> sortedList = Arrays.asList(userA, userB, userC);
    private final List<UserPost> unsortedList = Arrays.asList(userB, userA, userC);

    private PostSorter postSorter;

    @BeforeEach
    void arrange() {
        postSorter = new AuthorPostSorter();
    }

    @Test
    void sort_sortedList_returnsSortedList() {
        List<UserPost> actualList = postSorter.sort(sortedList);

        assertEquals(sortedList, actualList);
    }

    @Test
    void sort_unsortedList_returnsSortedList() {
        List<UserPost> actualList = postSorter.sort(unsortedList);

        assertEquals(sortedList, actualList);
    }

}