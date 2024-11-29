package org.example.lab4.tests;

import org.example.lab4.service.MemoryManager;
import org.junit.Test;

import static org.junit.Assert.*;

public class MemoryManagerTest {

    @Test
    public void testAllocateAndFree() {
        MemoryManager manager = new MemoryManager(1024);
        int addr1 = manager.allocate(100);
        int addr2 = manager.allocate(200);
        assertNotEquals(addr1, addr2);
        manager.free(addr1);
        assertThrows(IllegalArgumentException.class, () -> manager.read(addr1, 100));
        assertThrows(IllegalArgumentException.class, () -> manager.free(addr1));
    }

    @Test
    public void testWriteAndRead() {
        MemoryManager manager = new MemoryManager(1024);
        int addr = manager.allocate(100);
        byte[] data = {1, 2, 3, 4, 5};
        manager.write(addr, data);

        byte[] readData = manager.read(addr, 5);
        assertArrayEquals(data, readData);
    }

    @Test
    public void testDefragment() {
        MemoryManager manager = new MemoryManager(1024);
        int addr1 = manager.allocate(100);
        int addr2 = manager.allocate(200);
        int addr3 = manager.allocate(50);
        int addr4 = manager.allocate(150);
        manager.free(addr1);
        manager.free(addr3);
        assertNotEquals(addr2, addr4);
        manager.defragment();
        manager.allocatedBlocks.get(addr2).getAddress();
        assertEquals(0, manager.allocatedBlocks.get(addr2).getAddress());
        assertEquals(200, manager.allocatedBlocks.get(addr4).getAddress());
        int addr5 = manager.allocate(50);
        assertNotEquals(addr5, addr2);
        assertNotEquals(addr5, addr4);
        manager.free(addr5);
        assertThrows(IllegalArgumentException.class, () -> manager.read(addr5, 50));
    }

}

