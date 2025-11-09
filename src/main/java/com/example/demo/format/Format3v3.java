package com.example.demo.format;

import com.example.demo.format.strategy.Format;

public class Format3v3 implements Format {
    @Override
    public int getMaxPlayersPerTeam() {
        return 3;
    }
}
