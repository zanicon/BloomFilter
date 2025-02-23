package com.example.bloomfilter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bloomfilter")
public class BloomFilterController {

    private BloomFilter bloomFilter;
    private final BloomFilterProperties properties;

    @Autowired
    public BloomFilterController(BloomFilter bloomFilter, BloomFilterProperties properties) {
        this.bloomFilter = bloomFilter;
        this.properties = properties;
    }

    @PostMapping("/add")
    public String addUrl(@RequestParam String url) {
        bloomFilter.add(url);
        return "URL added to Bloom filter: " + url;
    }

    @GetMapping("/check")
    public String checkUrl(@RequestParam String url) {
        boolean mightContain = bloomFilter.mightContain(url);
        return mightContain ? "URL might be in the Bloom filter." : "URL is definitely not in the Bloom filter.";
    }

    @PostMapping("/reconfigure")
    public String reconfigureBloomFilter(
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Integer hashFunctionCount) {

        // Update properties if provided
        if (size != null) {
            properties.setSize(size);
        }
        if (hashFunctionCount != null) {
            if(hashFunctionCount > 8) {
                return "The maximum number of hash functions you can derive from a single SHA-256 hash is 8.";
            }
            properties.setHashFunctionCount(hashFunctionCount);
        }

        // Create a new BloomFilter instance with the updated properties
        bloomFilter = new BloomFilter(properties.getSize(), properties.getHashFunctionCount());

        return String.format("Bloom filter reconfigured to %s", bloomFilter);
    }
}
