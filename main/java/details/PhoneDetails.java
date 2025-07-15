package details;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhoneDetails implements IDetails {
    private String battery;
    private String chipset;
    private String cpu;
    private String display_size;
    private String display_resolution;
    private String camera_primary;
    private String camera_secondary;
    private String memory_internal;
    private String storage;
    private String dimensions;
    private String sim;
    private String gpu;
    private String mobile_fingerprint_sensor;
    private String mobile_frame_material;
    private String mobile_using;
    private String product_weight;

    public PhoneDetails(){}

    public PhoneDetails(String battery, String chipset, String cpu,
                        String display_size, String displayResolution,
                        String cameraPrimary, String cameraSecondary,
                        String memoryInternal, String storage,
                        String dimensions, String sim, String gpu,
                        String fingerprintSensor, String frameMaterial,
                        String usage, String weight) {
        this.battery = battery;
        this.chipset = chipset;
        this.cpu = cpu;
        this.display_size = display_size;
        this.display_resolution = displayResolution;
        this.camera_primary = cameraPrimary;
        this.camera_secondary = cameraSecondary;
        this.memory_internal = memoryInternal;
        this.storage = storage;
        this.dimensions = dimensions;
        this.sim = sim;
        this.gpu = gpu;
        this.mobile_fingerprint_sensor = fingerprintSensor;
        this.mobile_frame_material = frameMaterial;
        this.mobile_using = usage;
        this.product_weight = weight;
    }

    @Override
    public List<DetailsEntry> details() {
        List<DetailsEntry> list = new ArrayList<>();
        list.add(new DetailsEntry("Battery", battery));
        list.add(new DetailsEntry("Chipset", chipset));
        list.add(new DetailsEntry("CPU", cpu));
        list.add(new DetailsEntry("Display Size", display_size));
        list.add(new DetailsEntry("Display Resolution", display_resolution));
        list.add(new DetailsEntry("Primary Camera", camera_primary));
        list.add(new DetailsEntry("Secondary Camera", camera_secondary));
        list.add(new DetailsEntry("Internal Memory", memory_internal));
        list.add(new DetailsEntry("Storage", storage));
        list.add(new DetailsEntry("Dimensions", dimensions));
        list.add(new DetailsEntry("SIM", sim));
        list.add(new DetailsEntry("GPU", gpu));
        list.add(new DetailsEntry("Fingerprint Sensor", mobile_fingerprint_sensor));
        list.add(new DetailsEntry("Frame Material", mobile_frame_material));
        list.add(new DetailsEntry("Usage", mobile_using));
        list.add(new DetailsEntry("Weight", product_weight));
        return Collections.unmodifiableList(list);
    }

    public void setBattery(String battery) { this.battery = battery;}

    public void setChipset(String chipset) {this.chipset = chipset;}

    public void setCpu(String cpu) {this.cpu = cpu;}

    public void setDisplay_size(String display_size) {this.display_size = display_size;}

    public void setDisplay_resolution(String display_resolution) {this.display_resolution = display_resolution;}

    public void setCamera_primary(String camera_primary) {this.camera_primary = camera_primary;}

    public void setCamera_secondary(String camera_secondary) {this.camera_secondary = camera_secondary;}

    public void setMemory_internal(String memory_internal) {this.memory_internal = memory_internal;}

    public void setStorage(String storage) {this.storage = storage;}

    public void setDimensions(String dimensions) {this.dimensions = dimensions;}

    public void setSim(String sim) {this.sim = sim;}

    public void setGpu(String gpu) {this.gpu = gpu;}

    public void setMobile_fingerprint_sensor(String mobile_fingerprint_sensor) {this.mobile_fingerprint_sensor = mobile_fingerprint_sensor;}

    public void setMobile_frame_material(String mobile_frame_material) {this.mobile_frame_material = mobile_frame_material;}

    public void setMobile_using(String mobile_using) {this.mobile_using = mobile_using;}

    public void setProduct_weight(String product_weight) {this.product_weight = product_weight;}
}
