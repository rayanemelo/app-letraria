package com.example.letraria.enums;

public enum BookStatus {
    QUERO_LER(0, "Quero Ler"),
    LENDO(1, "Lendo"),
    LIDO(2, "Lido");

    private final int value;
    private final String label;

    BookStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static BookStatus fromValue(int value) {
        for (BookStatus status : values()) {
            if (status.getValue() == value) return status;
        }
        return null;
    }

    public static BookStatus fromLabel(String label) {
        for (BookStatus status : values()) {
            if (status.getLabel().equalsIgnoreCase(label)) return status;
        }
        return null;
    }
}
