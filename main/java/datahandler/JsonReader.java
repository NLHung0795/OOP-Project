package datahandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import product.Laptop;
import product.Phone;
import product.Product;

import java.io.InputStream;
import java.util.ArrayList;

public class JsonReader {

    private ArrayList<Phone> phoneList;
    private ArrayList<Laptop> laptopList;
    private ArrayList<Product<?>> productList;

    public void getContainer(ArrayList<Product<?>> productList){
        this.productList = productList;
    }

    public void read(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            InputStream inputStreamPhones = getClass().getResourceAsStream("/data/phones_filtered.json");
            if (inputStreamPhones != null) {
                phoneList = objectMapper.readValue(inputStreamPhones, new TypeReference<>() {});
                productList.addAll(phoneList);
            } else { System.err.println("Cannot load phones_filtered.json"); }

            InputStream inputStreamLaptops = getClass().getResourceAsStream("/data/laptop_filtered.json");
            if (inputStreamLaptops != null) {
                laptopList = objectMapper.readValue(inputStreamLaptops, new TypeReference<>() {});
                productList.addAll(laptopList);
            } else { System.err.println("Cannot load laptop_filtered.json"); }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Không thể tải dữ liệu sản phẩm.");
            alert.setContentText("Ứng dụng có thể không hoạt động đúng.");
            alert.showAndWait();
        }
    }
}
