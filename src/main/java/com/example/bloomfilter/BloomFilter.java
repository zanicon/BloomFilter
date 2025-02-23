package com.example.bloomfilter;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class BloomFilter {
    private final BitSet bitSet;
    private final Integer size;
    private final Integer hashFunctionCount;

    public BloomFilter(int size, int hashFunctionCount) {
        this.size = size;
        this.hashFunctionCount = hashFunctionCount;
        this.bitSet = new BitSet(size);
    }

    public void add(String element) {
        for (int hash : getHashes(element)) {
            bitSet.set(getIndexFromHash(hash));
        }
    }

    public boolean mightContain(String element) {
        for (int hash : getHashes(element)) {
            if (!bitSet.get(getIndexFromHash(hash))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Note:
     * SHA-256 produces a 256-bit (32-byte) hash.
     * If you want to derive multiple independent hash functions you can extract different parts of this hash
     * 256bits/32bytes ( 4bytes ) = 8
     * So, you can extract up to 8 independent hash functions from a single SHA-256 hash.
     * This simple means that the maximum number of hash functions you can derive from a single SHA-256 hash is 8.
     * @param element
     * @return
     */
    private int[] getHashes(String element)  {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(element.getBytes());
        int[] hashes = new int[hashFunctionCount];
        for (int i = 0; i < hashFunctionCount; i++) {
            hashes[i] = ByteBuffer.wrap(hash, i * 4, 4).getInt();;
        }
        return hashes;
    }


    private int getIndexFromHash(int hash) {
        return Math.abs(hash % size);
    }


    @Override
    public String toString() {
        return "BloomFilter{" +
                "size=" + size +
                ", hashFunctionCount=" + hashFunctionCount +
                '}';
    }
}
