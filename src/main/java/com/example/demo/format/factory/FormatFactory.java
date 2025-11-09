package com.example.demo.format.factory;

import com.example.demo.format.Format1v1;
import com.example.demo.format.Format3v3;
import com.example.demo.format.Format5v5;
import com.example.demo.format.strategy.Format;

public class FormatFactory {

    private FormatFactory() {}

    public static Format fromString(String formatType) {
        if (formatType == null) {
            throw new IllegalArgumentException("Format type cannot be null");
        }

        return switch (formatType.toLowerCase()) {
            case "1v1" -> new Format1v1();
            case "3v3" -> new Format3v3();
            case "5v5" -> new Format5v5();
            default -> throw new IllegalArgumentException("Unsupported format type: " + formatType);
        };
    }

    public static String toString(Format format) {
        int maxPlayers = format.getMaxPlayersPerTeam();
        return maxPlayers + "v" + maxPlayers;
    }
}

