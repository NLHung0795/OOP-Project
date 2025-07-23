package view;

import controller.ProductController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import product.Product;

public class ProductButton extends VBox{
    private final Product<?> product;
    private final int maxPerRow = 5;
    private GridPane gridProductPane;
    public ProductButton(double v, Product<?> product, GridPane gridProductPane){
        super(v);
        this.product = product;
        this.gridProductPane = gridProductPane;
    }

    protected void createProductItem(Product<?> product) {

        this.setPrefWidth(180); this.setMinHeight(360); this.setMaxHeight(360);
        this.setAlignment(Pos.TOP_CENTER); this.setPadding(new Insets(10));
        this.setStyle("-fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-background-color: white;");

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

        this.getChildren().addAll(imageView, nameLabel, priceLabel, ratingBox);
    }

    public void createProductButton(int totalOrder, int cnt, int j, int i){
        int totalRows = (totalOrder + maxPerRow - 1) / maxPerRow;
        Button button = new Button("" + cnt);
        button.setOnAction(event -> {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(100);
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/screen/ShowProduct.fxml"));
                            Parent root = loader.load();
                            ProductController control = loader.getController();
                            control.setProduct(product); 
                            Scene scene = new Scene(root); Stage stage = new Stage();
                            stage.setScene(scene); stage.setTitle(product.getName());
                            stage.show();
                        } catch (Exception e) { e.printStackTrace(); }
                    });
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); e.printStackTrace(); }
            });
            thread.setDaemon(true); thread.start();
        });

        this.createProductItem(product); // Renamed
        AnchorPane pane2 = new AnchorPane(button);
        AnchorPane.setTopAnchor(button, 0.0); AnchorPane.setBottomAnchor(button, 0.0);
        AnchorPane.setLeftAnchor(button, 0.0); AnchorPane.setRightAnchor(button, 0.0);
        button.setOpacity(0);
        gridProductPane.setPrefHeight(380 * totalRows);
        gridProductPane.setGridLinesVisible(false);

        StackPane stack = new StackPane();
        stack.setMinSize(180, 360); stack.getChildren().addAll(this,pane2);
        GridPane.setHgrow(stack, Priority.ALWAYS); GridPane.setVgrow(stack, Priority.ALWAYS);
        gridProductPane.add(stack, j, i);
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
