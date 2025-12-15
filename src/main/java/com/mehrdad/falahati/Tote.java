package com.mehrdad.falahati;

public record Tote(char type, int weight) {

    public Tote(String input) {
        this(
                input.charAt(0),
                Integer.parseInt(input.substring(1))
        );
    }

    public int getTemperature() {
        return switch (type) {
            case 'A' -> 3;
            case 'C' -> 2;
            case 'F' -> 1;
            default -> 0;
        };
    }
}
