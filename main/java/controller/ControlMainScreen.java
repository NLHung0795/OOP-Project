package controller;

import datahandler.JsonReader;
import main.Bot;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import product.Laptop;
import product.Phone;
import product.Product;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.ProductButton;
import view.CompareTable;
import structure.TreeProduct;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class ControlMainScreen implements Initializable {

    @FXML private Label addLabel;
    @FXML private AnchorPane addPane1;
    @FXML private AnchorPane OptionPane1;
    @FXML private AnchorPane OptionPane2;
    @FXML private HBox myBrandHBox;
    @FXML private HBox myProductHBox;
    @FXML private AnchorPane myPane1;
    @FXML private AnchorPane myPane2;
    @FXML private TextField searchTextField;
    @FXML private AnchorPane searchPane;
    @FXML private GridPane gridProductPane;
    @FXML private ScrollPane scrollProductPane;
    @FXML private StackPane addStackPane;
    @FXML private ImageView addImage1;
    @FXML private TableView<List<String>> compareTable;
    @FXML private TableColumn<List<String>, String> detailsColumn;
    @FXML private TableColumn<List<String>, String> firstProductColumn;
    @FXML private TableColumn<List<String>, String> secondProductColumn;
    @FXML private AnchorPane sortPane;
    @FXML private Button blinkButton;

    // Class variables
    private static Product<?> productAddedFirst;
    private static ArrayList<Product<?>> productList;
    private static final TreeProduct productTree = new TreeProduct("product");;
    private static final TreeSet<String> productBrand = new TreeSet<>();
    private static final TreeSet<String> productType = new TreeSet<>();
    private static JsonReader reader = new JsonReader();
    private final int maxPerRow = 5;
    private int stt = 0;
    private static int numberProductAdded = 1;

    // Lists to manage dynamic buttons
    private final List<Button> dynamicTypeButtons = new ArrayList<>();
    private final List<Button> dynamicBrandButtons = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productList = new ArrayList<>();
        reader.getContainer(productList);
        reader.read();

        productTree.initData(productList,productBrand,productType);
        productTree.createTree();

        initialize_button();
        createSearchButton();
        showAllProduct(productList);
        searchBySearchBox();
        showOptionPane1();

        myProductHBox.setSpacing(20);
        myProductHBox.setAlignment(Pos.CENTER);
        myBrandHBox.setSpacing(20);
        myBrandHBox.setAlignment(Pos.CENTER);

        scrollProductPane.setFitToWidth(true);

        addStackPane.setVisible(false);
        myPane2.setVisible(false);
    }

    @FXML
    public void openChatbot() {
        Bot chatbot = new Bot();
        Stage chatStage = new Stage();
        chatbot.createChatUI(chatStage);
        chatStage.show();
    }

    @FXML
    private void showOptionPane1(){
        OptionPane1.setVisible(true);
        OptionPane2.setVisible(false);
        myBrandHBox.getChildren().removeAll(dynamicBrandButtons);
        dynamicBrandButtons.clear();
    }

    @FXML
    private void showOptionPane2(){
        OptionPane1.setVisible(false);
        OptionPane2.setVisible(true);
    }

    @FXML
    private void switchPane1(ActionEvent e){
        addStackPane.setVisible(true);
        addPane1.setVisible(true);
        myPane2.setVisible(false);
        myPane1.setVisible(false);
        sortPane.setVisible(false);
        searchPane.setVisible(false);
        scrollProductPane.setVisible(false);
        myProductHBox.getParent().getParent().setVisible(false);

    }
    @FXML
    private void switchPane2(ActionEvent e){
        myPane1.setVisible(true);
        sortPane.setVisible(true);
        myPane2.setVisible(false);
        addPane1.setVisible(false);
        addStackPane.setVisible(false);
        searchPane.setVisible(true);
        scrollProductPane.setVisible(true);
        myProductHBox.getParent().getParent().setVisible(true);
        showOptionPane1();
    }

    @FXML
    private void switchSearchInCompare(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/screen/ShowSearchInCompare.fxml"));
            Parent root = loader.load();

            SearchCompareController searchController = loader.getController();
            searchController.initData(this, productList);

            Stage searchStage = new Stage();
            searchStage.setTitle("Chọn sản phẩm để so sánh");
            searchStage.setScene(new Scene(root));
            searchStage.initModality(Modality.APPLICATION_MODAL); //chặn cửa sổ chính cho đến khi cửa sổ này đóng lại.
            searchStage.showAndWait(); //Hiển thị cửa sổ và chờ người dùng tương tác xong (đóng lại) rồi mới chạy tiếp phần sau của chương trình.

        } catch (IOException ex) {
            System.err.println("Không thể mở cửa sổ tìm kiếm!");
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Không thể mở cửa sổ tìm kiếm.");
            alert.setContentText("Đã xảy ra lỗi khi tải giao diện.");
            alert.showAndWait();
        }
    }

    @FXML
    public void gainProductFromAdd(int order){
        CompareTable table = new CompareTable();
        table.setData(compareTable,detailsColumn, firstProductColumn,secondProductColumn);
        if (order < 0 || order >= productList.size()) return;

        Product<?> selectedProduct = productList.get(order);

        if (numberProductAdded % 2 == 1) {
            addLabel.setText("Đã có 1 sản phẩm được thêm: " + selectedProduct.getName());
            addLabel.setVisible(true);
            productAddedFirst = selectedProduct;
            addImage1.setImage(new Image(selectedProduct.getPicture_url(), true));
            numberProductAdded++;
        } else {
            if (productAddedFirst != null && productAddedFirst.getType().equals(selectedProduct.getType())) {
                addPane1.setVisible(false);
                myPane2.setVisible(true);
                addStackPane.setVisible(true);
                myPane1.setVisible(false);
                table.customCompareTable(productAddedFirst, selectedProduct);
            } else {
                productAddedFirst = selectedProduct;
                numberProductAdded = 2;
                addLabel.setText("Đã có 1 sản phẩm được thêm: " + selectedProduct.getName());
                addImage1.setImage(new Image(selectedProduct.getPicture_url(), true));
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Loại sản phẩm không khớp.");
                alert.setContentText("Đã chọn sản phẩm mới. Vui lòng chọn sản phẩm thứ hai cùng loại.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void clearAddPane(){
        compareTable.getColumns().clear();
        compareTable.getItems().clear();
        productAddedFirst = null;
        numberProductAdded = 1;
        addLabel.setVisible(false);
        addImage1.setImage(null);
        addPane1.setVisible(true);
        myPane2.setVisible(false);
    }

    @FXML
    public void searchByButton(ActionEvent e){
        showAllProduct(productList);
        showOptionPane1();
    }

    @FXML
    public void searchBySearchBox(){
        searchTextField.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().equals(KeyCode.ENTER)){
                String searchBox = searchTextField.getText().toLowerCase().trim();
                if (searchBox.isEmpty()) {
                    showAllProduct(productList);
                    return;
                }
                ArrayList<Product<?>> searchResult = new ArrayList<>();
                for(Product<?> product : productList){
                    if(product.getName().toLowerCase().contains(searchBox)){
                        searchResult.add(product);
                    }
                }
                if(!searchResult.isEmpty()){ showAllProduct(searchResult); } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText("Không tìm thấy sản phẩm.");
                    alert.setContentText("Không có sản phẩm nào khớp với: '" + searchTextField.getText() + "'");
                    alert.showAndWait();
                }
            }
        });
    }


    @FXML
    public void sortProductPrice(Event e){
        MenuItem btn = (MenuItem) e.getSource();
        String sortType = btn.getText();

        productList.sort((o1, o2) -> {
            try {
                long price1 = Long.parseLong(o1.getPrice().replaceAll("[^\\d]", ""));
                long price2 = Long.parseLong(o2.getPrice().replaceAll("[^\\d]", ""));
                if(sortType.charAt(0) == 'C')
                    return Long.compare(price2, price1);
                else
                    return Long.compare(price1, price2);
            } catch (Exception ex) {
                System.err.println("Error parsing price: " + o1.getPrice() + " or " + o2.getPrice());
                return 0;
            }
        });

        productTree.initData(productList,productBrand,productType);
        productTree.createTree();
        createSearchButton();
        showAllProduct(productList);
    }

    private void showAllProduct(ArrayList<Product<?>> productsToShow) {
        gridProductPane.getChildren().clear();
        int numberOfProduct = productsToShow.size();
        int col = 0;
        int row = 0;
        for (int i = 0; i < numberOfProduct; i++) {
            int originalIndex = productList.indexOf(productsToShow.get(i));
            if (originalIndex != -1) {
                ProductButton button = new ProductButton(10, productList.get(originalIndex), gridProductPane);
                button.createProductButton(numberOfProduct, originalIndex, col, row);
                col++;
                if (col >= maxPerRow) { col = 0; row++; }
            }
        }
    }

    private void showProductsFromTreeItems(ObservableList<TreeItem<String>> items) {
        gridProductPane.getChildren().clear();
        ArrayList<Product<?>> products = new ArrayList<>();
        for(TreeItem<String> item : items){
            int i = Integer.parseInt(item.getValue());
            products.add(productList.get(i));
        }
        showAllProduct(products);
    }

    private void createSearchButton() {
        myProductHBox.getChildren().removeAll(dynamicTypeButtons);
        dynamicTypeButtons.clear();
        myBrandHBox.getChildren().removeAll(dynamicBrandButtons);
        dynamicBrandButtons.clear();

        ObservableList<TreeItem<String>> types = productTree.getChildren();

        for(TreeItem<String> type : types){
            Button btn = new Button(type.getValue());
            btn.setOnAction(e -> {
                showOptionPane2();
                myBrandHBox.getChildren().removeAll(dynamicBrandButtons);
                dynamicBrandButtons.clear();

                ObservableList<TreeItem<String>> items = productTree.bfs(type.getValue());
                if (items != null) {
                    showProductsFromTreeItems(items);
                }

                Button returnButton = new Button("Return");
                returnButton.setOnAction(actionEvent -> {
                    showAllProduct(productList);
                    showOptionPane1();
                });
                returnButton.setPrefHeight(27);
                myBrandHBox.getChildren().add(returnButton);
                dynamicBrandButtons.add(returnButton);

                for(TreeItem<String> brand : type.getChildren()){
                    Button btn1 = new Button(brand.getValue());
                    btn1.setOnAction(e1 -> {
                        Button press = (Button) e1.getSource();
                        ObservableList<TreeItem<String>> brandItems = productTree.bfs(press.getText());
                        if (brandItems != null) {
                            for(TreeItem<String> item : brandItems)
                                System.out.println(item.getValue());
                            showProductsFromTreeItems(brandItems);
                        }
                    });
                    btn1.setPrefHeight(27);
                    myBrandHBox.getChildren().add(btn1);
                    dynamicBrandButtons.add(btn1);
                }
            });
            btn.setPrefHeight(27);
            myProductHBox.getChildren().add(btn);
            dynamicTypeButtons.add(btn);
        }
    }



    public void initialize_button() {
        Timeline blink = new Timeline(
                new KeyFrame(Duration.seconds(0.5), e -> blinkButton.setStyle(
                        "-fx-background-color: #64b5f6;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 20;"
                )),
                new KeyFrame(Duration.seconds(1.0), e -> blinkButton.setStyle(
                        "-fx-background-color: #C0C0C0;" +
                                "-fx-text-fill: black;" +
                                "-fx-background-radius: 20;" +
                                "-fx-border-radius: 20;"
                ))
        );
        blinkButton.setOnMouseEntered(e -> { blinkButton.setScaleX(1.2); blinkButton.setScaleY(1.2); });
        blinkButton.setOnMouseExited(e -> { blinkButton.setScaleX(1); blinkButton.setScaleY(1); });
        blink.setCycleCount(Timeline.INDEFINITE);
        blink.play();
    }

}