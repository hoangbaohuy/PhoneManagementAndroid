package com.example.phonemanagement.Models;

public class Phone {
    private int phoneId;
    private String description;
    private double price;
    private int stockQuantity;
    private String image;
    private String chipset;
    private String gpu;
    private String color;
    private int warrantyPeriod;
    private String modelName;
    private String releaseDate;
    private String operatingSystem;
    private int ram;
    private int storage;
    private String brandName;

    public Phone() {
    }

    public Phone(int phoneId, String description, double price, int stockQuantity, String image, String chipset, String gpu, String color, int warrantyPeriod, String modelName, String releaseDate, String operatingSystem, int ram, int storage, String brandName) {
        this.phoneId = phoneId;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.image = image;
        this.chipset = chipset;
        this.gpu = gpu;
        this.color = color;
        this.warrantyPeriod = warrantyPeriod;
        this.modelName = modelName;
        this.releaseDate = releaseDate;
        this.operatingSystem = operatingSystem;
        this.ram = ram;
        this.storage = storage;
        this.brandName = brandName;
    }

    public int getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(int phoneId) {
        this.phoneId = phoneId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getChipset() {
        return chipset;
    }

    public void setChipset(String chipset) {
        this.chipset = chipset;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
