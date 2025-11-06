package com.example.demo.format;

import com.example.demo.format.strategy.Format;

public class Format5v5 implements Format {
    @Override
    public int getMaxPlayersPerTeam() {
        return 5;
    }
}
