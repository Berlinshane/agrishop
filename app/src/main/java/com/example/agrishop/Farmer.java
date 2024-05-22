package com.example.agrishop;

public class Farmer {
    private String name;
    private String phoneNumber;
    private String selectedFertilizer;

    public Farmer(String name, String phoneNumber, String selectedFertilizer) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.selectedFertilizer = selectedFertilizer;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSelectedFertilizer() {
        return selectedFertilizer;
    }
}
