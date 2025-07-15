package controller;

import product.Product;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class SearchCompareController implements Initializable {

    @FXML private GridPane addGridPane;
    @FXML private TextField searchTextField1;

    private ArrayList<Product<?>> productList;
    private ControlMainScreen mainController;

    private final int maxPerRow = 3;

    public void initData(ControlMainScreen mainController, ArrayList<Product<?>> products) {
        this.mainController = mainController;
        this.productList = products;
        displayProducts(this.productList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupSearchListener();
    }

    private void displayProducts(List<Product<?>> productsToDisplay) {
        addGridPane.getChildren().clear();
        if (productsToDisplay == null) return;

        int total = productsToDisplay.size();
        int row = 0;
        int col = 0;

        for (Product<?> product : productsToDisplay) {
            int originalIndex = this.productList.indexOf(product);
            if (originalIndex != -1) {
                addProductToSearch(total, originalIndex, col, row);
                col++;
                if (col >= maxPerRow) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    private void addProductToSearch(int order, int productIndex, int j, int i) {
        int totalRows = (order + maxPerRow - 1) / maxPerRow;
        Button button = new Button("" + productIndex);

        button.setOnAction(event -> {
            Button numberPressed = (Button) event.getSource();
            int number = Integer.parseInt(numberPressed.getText());

            if (mainController != null) {
                Platform.runLater(() -> mainController.gainProductFromAdd(number));
            }

            Stage currentStage = (Stage) addGridPane.getScene().getWindow();
            currentStage.close();
        });

        VBox productBox = createProductItem(productList.get(productIndex));
        AnchorPane pane2 = new AnchorPane(button);
        AnchorPane.setTopAnchor(button, 0.0);
        AnchorPane.setBottomAnchor(button, 0.0);
        AnchorPane.setLeftAnchor(button, 0.0);
        AnchorPane.setRightAnchor(button, 0.0);

        button.setOpacity(0);
        addGridPane.setPrefHeight(380 * totalRows);
        addGridPane.setGridLinesVisible(false);

        StackPane stack = new StackPane();
        stack.setMinSize(180, 360);
        stack.getChildren().addAll(productBox, pane2);
        GridPane.setHgrow(stack, Priority.ALWAYS);
        GridPane.setVgrow(stack, Priority.ALWAYS);
        addGridPane.add(stack, j, i);
    }

    @FXML
    public void searchBySearchBox() {
        searchTextField1.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                String searchBox = searchTextField1.getText().toLowerCase().trim();
                ArrayList<Product<?>> searchResult = new ArrayList<>();

                if (searchBox.isEmpty()) {
                    displayProducts(productList);
                    return;
                }

                for (Product<?> product : productList) {
                    if (product.getName().toLowerCase().contains(searchBox)) {
                        searchResult.add(product);
                    }
                }

                if (!searchResult.isEmpty()) {
                    displayProducts(searchResult);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Cảnh báo");
                    alert.setHeaderText("Không tồn tại sản phẩm.");
                    alert.setContentText("Vui lòng kiểm tra lại thông tin.");
                    alert.showAndWait();
                }
            }
        });
    }

    private void setupSearchListener() {
        searchTextField1.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                searchBySearchBoxInternal();
            }
        });
        //searchTextField1.setOnAction(event -> searchBySearchBoxInternal());
    }

    private void searchBySearchBoxInternal() {
        String searchBox = searchTextField1.getText().toLowerCase().trim();
        ArrayList<Product<?>> searchResult = new ArrayList<>();

        if (searchBox.isEmpty()) {
            displayProducts(productList);
            return;
        }

        for (Product<?> product : productList) {
            if (product.getName().toLowerCase().contains(searchBox)) {
                searchResult.add(product);
            }
        }

        if (!searchResult.isEmpty()) {
            displayProducts(searchResult);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText("Không tồn tại sản phẩm.");
            alert.setContentText("Vui lòng kiểm tra lại thông tin.");
            alert.showAndWait();
        }
    }

    protected VBox createProductItem(Product<?> product) {
        VBox vBox = new VBox(10);
        vBox.setPrefWidth(180); vBox.setMinHeight(360); vBox.setMaxHeight(360);
        vBox.setAlignment(Pos.TOP_CENTER); vBox.setPadding(new Insets(10));
        vBox.setStyle("-fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-background-color: white;");

        ImageView imageView = new ImageView();
        try {
            imageView.setImage(new Image(product.getPicture_url(), true));
            imageView.setFitWidth(160); imageView.setFitHeight(160);
            imageView.setPreserveRatio(true);
        } catch (Exception e) { System.err.println("Lỗi tải ảnh: " + product.getPicture_url()); }
        VBox.setVgrow(imageView, Priority.NEVER);

        Label nameLabel = new Label(product.getName());
        nameLabel.setWrapText(true); nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        nameLabel.setAlignment(Pos.CENTER); nameLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        nameLabel.setMinHeight(60); nameLabel.setMaxWidth(160);
        VBox.setVgrow(nameLabel, Priority.ALWAYS);

        Label priceLabel = new Label(standardization(product.getPrice()));
        priceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #e53935;");
        priceLabel.setAlignment(Pos.CENTER); VBox.setVgrow(priceLabel, Priority.NEVER);

        HBox ratingBox = new HBox(5);
        ratingBox.setAlignment(Pos.CENTER);
        Label ratingLabel = new Label(String.format("%.1f", product.getAverage_rating()));
        ratingLabel.setStyle("-fx-font-size: 12px;");
        Label starLabel = new Label("⭐");
        starLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #fdd835;");
        Label countLabel = new Label("(" + product.getTotal_count() + ")");
        countLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: grey;");
        ratingBox.getChildren().addAll(ratingLabel, starLabel, countLabel);
        VBox.setVgrow(ratingBox, Priority.NEVER);

        vBox.getChildren().addAll(imageView, nameLabel, priceLabel, ratingBox);
        return vBox;
    }

    public String standardization(String price){
        if (price == null || price.isEmpty()) return "N/A";
        try {
            String currency = ""; String numberPart = price;
            if (!Character.isDigit(price.charAt(price.length() - 1))) {
                currency = " " + price.charAt(price.length() - 1);
                numberPart = price.substring(0, price.length() - 1);
            }
            long number = Long.parseLong(numberPart.replaceAll("[^\\d]", ""));
            return String.format("%,d", number) + currency;
        } catch (Exception e) { return price; }
    }
}
