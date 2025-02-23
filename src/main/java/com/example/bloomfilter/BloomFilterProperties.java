package com.example.bloomfilter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bloomfilter")
public class BloomFilterProperties {
    private Integer size = 1000;
    private Integer hashFunctionCount = 3;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getHashFunctionCount() {
        return hashFunctionCount;
    }

    public void setHashFunctionCount(Integer hashFunctionCount) {
        this.hashFunctionCount = hashFunctionCount;
    }
}
