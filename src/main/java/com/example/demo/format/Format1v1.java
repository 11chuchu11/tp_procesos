package com.example.demo.format;

import com.example.demo.format.strategy.Format;

public class Format1v1 implements Format {
    @Override
    public int getMaxPlayersPerTeam() {
        return 1;
    }
}
