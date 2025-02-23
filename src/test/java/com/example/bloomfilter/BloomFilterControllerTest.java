package com.example.bloomfilter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.either;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class BloomFilterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        reconfigureBloomFilter(10, 2);
    }

    @Test
    public void shouldAddAndCheckUrl() throws Exception {
        String url = "https://aniefiok.com";
        mockMvcAddAndExpect(url);

        // Check if the URL is in the Bloom filter (should return true)
        mockMvcCheck(url);

        // Check a URL that was never added (should return false or true if false positive occurs)
        mockMvc.perform(MockMvcRequestBuilders.get("/bloomfilter/check")
                        .param("url", "https://nonexistent.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                                either(is("URL might be in the Bloom filter."))
                                        .or(is("URL is definitely not in the Bloom filter."))
                        )
                );
    }

    private void mockMvcCheck(String url) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/bloomfilter/check")
                        .param("url", url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("URL might be in the Bloom filter."));
    }

    private void mockMvcAddAndExpect(String url) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/bloomfilter/add")
                        .param("url", url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(format("URL added to Bloom filter: %s", url)));
    }

    @Test
    public void shouldReconfigureBloomFilter() throws Exception {
        // Reconfigure the Bloom filter with a larger size and more hash functions
        reconfigureBloomFilter(100, 3);

        // Add a URL to the Bloom filter
        mockMvcAddAndExpect("https://aniefiok.com");

        // Check if the URL is in the Bloom filter (should return true)
        mockMvcCheck("https://aniefiok.com");

        // Check a URL that was never added (should return false or true if false positive occurs)
        mockMvc.perform(MockMvcRequestBuilders.get("/bloomfilter/check")
                        .param("url", "http://nonexistent.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        either(is("URL might be in the Bloom filter."))
                                .or(is("URL is definitely not in the Bloom filter."))
                        )
                );
    }

    @Test
    public void shouldAddMultipleUrlsAndCheckForFalsePositive() throws Exception {
        // Add 5 URLs to the Bloom filter

        List<String> urls = List.of(
                "https://example.com",
                "https://example.org",
                "https://example.net",
                "https://example.edu",
                "https://example.gov"
        );

        for (String url : urls) {
            mockMvcAddAndExpect(url);
        }

        // Check a URL that was never added (should false positive occurs because of the small size and hash functions )
        mockMvc.perform(MockMvcRequestBuilders.get("/bloomfilter/check")
                        .param("url", "https://nonexistent.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(is("URL might be in the Bloom filter."))
                );
    }

    private void reconfigureBloomFilter(int size, int hashFunctionCount) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/bloomfilter/reconfigure")
                        .param("size", String.valueOf(size))
                        .param("hashFunctionCount", String.valueOf(hashFunctionCount)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(format("Bloom filter reconfigured to BloomFilter{size=%d, hashFunctionCount=%d}", size, hashFunctionCount)));
    }
}
