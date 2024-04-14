package com.example.lawnMower.entity;

import lombok.Data;

@Data
public class LawnMower {
    private int x;
    private int y;
    private char orientation;
    private String instructions;
}
