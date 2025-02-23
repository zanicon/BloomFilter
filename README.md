# Bloom Filter Implementation in Java

## Overview
This repository accompanies the following blog post. The application is a simple Spring Boot project that demonstrates the use of a Bloom Filter. It implements a Bloom Filter data structure and allows users to interact with it through various endpoints.

This Spring Boot application provides an implementation of a Bloom Filter for URL storage and lookup. It allows adding URLs to a Bloom Filter, checking if a URL might be in the filter, and reconfiguring the filter dynamically to change its size and number of hash functions.

The Higher the size of the Bloom Filter, the lower the false positive rate. The number of hash functions is also a factor in the false positive rate. The more hash functions, the lower the false positive rate.

## Getting Started

### Prerequisites
- Java 21+
- Gradle
- Spring Boot 3.3+

### Running the Application
1. Clone the repository:
   ```sh
   git clone <repository-url>
   cd bloomfilter
   ```
2. Build the project using Gradle:
   ```sh
   ./gradlew build
   ```
3. Run the Spring Boot application:
   ```sh
   ./gradlew bootRun
   ```

The application will start on `http://localhost:8080`.

## API Endpoints
The application provides the following RESTful endpoints to interact with the Bloom Filter:

| HTTP Method | Endpoint                 | Description                                        | Parameters |
|------------|-------------------------|----------------------------------------------------|------------|
| POST       | `/bloomfilter/add`       | Adds a URL to the Bloom filter                    | `url` (query param, required) |
| GET        | `/bloomfilter/check`     | Checks if a URL might be in the Bloom filter      | `url` (query param, required) |
| POST       | `/bloomfilter/reconfigure` | Reconfigures the Bloom filter with new parameters | `size` (query param, optional), `hashFunctionCount` (query param, optional, max: 8) |




## Running Tests
You can look at the following `BloomFilterControllerTest` to see a set of test to see the BloomFilter in action.
Run the tests using Gradle:
```sh
./gradlew test
```





