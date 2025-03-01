package com.example.bloomfilter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BloomFilterTest {

    @Test
    void shouldAddToBloomFilterAndCheckIfItemMightBeContained() {
        // Given
        BloomFilter bloomFilter = new BloomFilter(100, 4);

        // When
        bloomFilter.add("https://www.aniefiok.com");
        bloomFilter.add("https://www.codeninja.com");

        // Then
        assertThat(bloomFilter.mightContain("https://www.aniefiok.com")).isTrue(); // true
        assertThat(bloomFilter.mightContain("https://www.codeninja.com")).isTrue(); // true

        assertThat(bloomFilter.mightContain("https://www.google.com")).isFalse(); // false (most likely)
    }
}