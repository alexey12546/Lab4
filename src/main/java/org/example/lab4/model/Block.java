package org.example.lab4.model;

import lombok.Data;

@Data
public class Block {
    private int address;
    private int size;

    public Block(int address, int size) {
        this.address = address;
        this.size = size;
    }
}
