package com.example.bloomfilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BloomFilterConfig {

    @Bean
    @Scope("prototype") // This is important to create a new instance of BloomFilter for each request
    public BloomFilter bloomFilter(BloomFilterProperties properties) {
        return new BloomFilter(properties.getSize(), properties.getHashFunctionCount());
    }
}
