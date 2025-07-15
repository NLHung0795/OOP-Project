package details;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LaptopDetails implements IDetails {
    private String battery;
    private String cpu;
    private String ram;
    private String storage;
    private String gpu;
    private String bluetooth;
    private String display_size;
    private String display_resolution;
    private String operating_system;
    private String special_feature;
    private String product_weight;
    private String dimensions;

    public LaptopDetails(){}

    public LaptopDetails(String battery, String cpu, String ram,
                         String storage, String gpu, String bluetooth,
                         String displaySize, String display_resolution,
                         String operating_system, String specialFeature,
                         String weight, String dimensions) {
        this.battery = battery;
        this.cpu = cpu;
        this.ram = ram;
        this.storage = storage;
        this.gpu = gpu;
        this.bluetooth = bluetooth;
        this.display_size = displaySize;
        this.display_resolution = display_resolution;
        this.operating_system = operating_system;
        this.special_feature = specialFeature;
        this.product_weight = weight;
        this.dimensions = dimensions;
    }

    @Override
    public List<DetailsEntry> details() {
        List<DetailsEntry> list = new ArrayList<>();
        list.add(new DetailsEntry("Battery", battery));
        list.add(new DetailsEntry("CPU", cpu));
        list.add(new DetailsEntry("RAM", ram));
        list.add(new DetailsEntry("Storage", storage));
        list.add(new DetailsEntry("GPU", gpu));
        list.add(new DetailsEntry("Bluetooth", bluetooth));
        list.add(new DetailsEntry("Display Size", display_size));
        list.add(new DetailsEntry("Display Resolution", display_resolution));
        list.add(new DetailsEntry("Operating System", operating_system));
        list.add(new DetailsEntry("Special Feature", special_feature));
        list.add(new DetailsEntry("Weight", product_weight));
        list.add(new DetailsEntry("Dimensions", dimensions));
        return Collections.unmodifiableList(list);
    }

    public void setProduct_weight(String product_weight) {
        this.product_weight = product_weight;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public void setBluetooth(String bluetooth) {
        this.bluetooth = bluetooth;
    }

    public void setDisplay_size(String display_size) {
        this.display_size = display_size;
    }

    public void setDisplay_resolution(String display_resolution) {
        this.display_resolution = display_resolution;
    }

    public void setOperating_system(String operating_system) {
        this.operating_system = operating_system;
    }

    public void setSpecial_feature(String special_feature) {
        this.special_feature = special_feature;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }
}