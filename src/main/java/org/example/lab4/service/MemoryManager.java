package org.example.lab4.service;

import org.example.lab4.model.Block;
import java.util.*;

public class MemoryManager {
    private final byte[] memory;
    private final TreeMap<Integer, Integer> freeBlocks;
    public final Map<Integer, Block> allocatedBlocks;
    private int blockCounter;

    public MemoryManager(int size) {
        memory = new byte[size];
        freeBlocks = new TreeMap<>();
        allocatedBlocks = new HashMap<>();
        freeBlocks.put(0, size);
        blockCounter = 0;
    }

    public int allocate(int size) {
        for (var entry : freeBlocks.entrySet()) {
            int start = entry.getKey();
            int blockSize = entry.getValue();
            if (blockSize >= size) {
                freeBlocks.remove(start);
                if (blockSize > size) {
                    freeBlocks.put(start + size, blockSize - size);
                }
                int blockId = blockCounter++;
                allocatedBlocks.put(blockId, new Block(start, size));
                return blockId;
            }
        }
        throw new OutOfMemoryError("Недостаточно памяти для выделения " + size + " байт");
    }

    public void free(int blockId) {
        Block block = allocatedBlocks.remove(blockId);
        if (block == null) {
            throw new IllegalArgumentException("Блок с ID " + blockId + " не найден");
        }
        freeBlocks.put(block.getAddress(), block.getSize());
    }

    public void write(int blockId, byte[] data) {
        Block block = allocatedBlocks.get(blockId);
        if (block == null || data.length > block.getSize()) {
            throw new IllegalArgumentException("Недостаточно места для записи");
        }
        System.arraycopy(data, 0, memory, block.getAddress(), data.length);
    }

    public byte[] read(int blockId, int length) {
        Block block = allocatedBlocks.get(blockId);
        if (block == null || length > block.getSize()) {
            throw new IllegalArgumentException("Недостаточно места для чтения");
        }
        byte[] result = new byte[length];
        System.arraycopy(memory, block.getAddress(), result, 0, length);
        return result;
    }

    public void defragment() {
        int currentFreeAddress = 0;
        HashMap<Integer, Block> newAllocatedBlocks = new HashMap<>();
        for (var entry : allocatedBlocks.entrySet()) {
            int blockId = entry.getKey();
            Block block = entry.getValue();
            int oldAddress = block.getAddress();
            int blockSize = block.getSize();
            if (oldAddress == currentFreeAddress) {
                newAllocatedBlocks.put(blockId, new Block(currentFreeAddress, blockSize));
                currentFreeAddress += blockSize;
                continue;
            }
            System.arraycopy(memory, oldAddress, memory, currentFreeAddress, blockSize);
            newAllocatedBlocks.put(blockId, new Block(currentFreeAddress, blockSize));
            currentFreeAddress += blockSize;
        }
        Arrays.fill(memory, currentFreeAddress, memory.length, (byte) 0);
        allocatedBlocks.clear();
        allocatedBlocks.putAll(newAllocatedBlocks);
        freeBlocks.clear();
        freeBlocks.put(currentFreeAddress, memory.length - currentFreeAddress);
    }

    public void printMemoryState() {
        System.out.println("Свободные блоки: " + freeBlocks);
        System.out.println("Занятые блоки: " + allocatedBlocks + "\n");
    }

}
