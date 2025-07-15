package controller;

import details.DetailsEntry;
import product.Product;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Bot;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductController {
    @FXML private TableView<List<String>> productTable;
    @FXML private TableColumn<List<String>, String> featureColumn;
    @FXML private TableColumn<List<String>, String> valueColumn;
    @FXML private Label productNameLabel;
    @FXML private Label costLabel;
    @FXML private Label contentLabel;
    @FXML private Label countLabel;
    @FXML private Button myButton;
    @FXML private HBox starBox;
    @FXML private VBox commentVBox;
    @FXML private AnchorPane commentPane;
    @FXML private ScrollPane productScrollPane;
    @FXML private AnchorPane showContentPane;
    @FXML private Button blinkButton;
    @FXML private AnchorPane scrollContent;
    @FXML private ImageView backgroundImage;

    private Product<?> product;

    public void setProduct(Product<?> product) {
        this.product = product;
        updateUI();
        if (!this.product.getComments().isEmpty()) {
            for (int i = 0; i < product.getComments().size(); i++) {
                showComments(i,
                        product.getComments().get(i).getCommentator(),
                        product.getComments().get(i).getContent()
                );
            }
        }
    }

    @FXML
    public void initialize() {
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
        blinkButton.setOnMouseEntered(e -> {
            blinkButton.setScaleX(1.2);
            blinkButton.setScaleY(1.2);
        });
        blinkButton.setOnMouseExited(e -> {
            blinkButton.setScaleX(1);
            blinkButton.setScaleY(1);
        });
        blink.setCycleCount(Timeline.INDEFINITE);
        blink.play();
        URL bgUrl = getClass().getResource("/com/example/screen/background.jpg");
        if (bgUrl != null) {
            backgroundImage.setImage(new Image(bgUrl.toExternalForm(), false));
        } else {
            System.out.println("Không tìm thấy background.jpg");
        }
    }

    private void updateUI() {
        initialize();
        productNameLabel.setText(product.getName() != null ? product.getName() : "Unknown Product");
        costLabel.setText(!"0đ".equals(product.getPrice()) ? standardization(product.getPrice()) : "N/A");
        contentLabel.setText(product.getMain_content() != null ? product.getMain_content() : "N/A");
        countLabel.setText(product.getTotal_count() != 0 ? String.valueOf(product.getTotal_count()) : "N/A");
        showStarRating();
        List<DetailsEntry> allDetails = product.allDetails();
        List<DetailsEntry> specificDetails = allDetails.size() > 2 ? allDetails.subList(2, allDetails.size()) : new ArrayList<>();
        List<List<String>> tableData = new ArrayList<>();
        for (DetailsEntry entry : specificDetails) {
            tableData.add(Arrays.asList(entry.getLabel(), entry.getValue()));
        }
        ObservableList<List<String>> observableTableData = FXCollections.observableArrayList(tableData);
        featureColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        valueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        productTable.setItems(observableTableData);
        productTable.setFixedCellSize(30);
        productTable.setPrefHeight(30 * observableTableData.size());
        productTable.setMaxHeight(Region.USE_PREF_SIZE);
        loadImageToButton(product.getPicture_url());
    }

    private void showStarRating() {
        starBox.getChildren().clear();
        double rating;
        try {
            rating = product.getAverage_rating();
        } catch (Exception e) {
            rating = 0;
        }
        int fullStars = (int) rating;
        boolean halfStar = (rating - fullStars) >= 0.25 && (rating - fullStars) < 0.75;
        int totalStars = 5;
        for (int i = 0; i < fullStars; i++) {
            starBox.getChildren().add(createStar("★"));
        }
        if (halfStar) {
            starBox.getChildren().add(createStar("⯨"));
        }
        int remaining = totalStars - fullStars - (halfStar ? 1 : 0);
        for (int i = 0; i < remaining; i++) {
            starBox.getChildren().add(createStar("☆"));
        }
    }

    private Label createStar(String symbol) {
        Label star = new Label(symbol);
        star.setStyle("-fx-font-size: 24px; -fx-text-fill: gold;");
        return star;
    }

    private void loadImageToButton(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                Image image = new Image(imageUrl, true);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(320);
                imageView.setFitHeight(320);
                imageView.setPreserveRatio(true);
                myButton.setGraphic(imageView);
            } catch (Exception e) {
                System.out.println("Lỗi tải ảnh: " + e.getMessage());
            }
        } else {
            System.out.println("URL ảnh không hợp lệ!");
        }
    }

    private int check = 0;
    private int replySize = 0;

    private void showComments(int i, String commentator, String content) {
        Label commentatorLabel = new Label();
        Label contentLabel = new Label();
        contentLabel.setWrapText(true);
        contentLabel.setMinWidth(700);
        commentatorLabel.setText(commentator);
        if (content != null) {
            content = content.replaceAll("Trả lời", "");
            contentLabel.setText(content.replaceAll("\n", System.lineSeparator()));
        }
        contentLabel.setPadding(new Insets(0, 0, 0, 20));
        VBox commentBox = new VBox(5);
        commentBox.setMinHeight(Region.USE_PREF_SIZE);
        commentBox.setMaxHeight(Region.USE_PREF_SIZE);
        if (check == 1) {
            VBox.setMargin(commentBox, new Insets(0, 0, 0, 40));
        } else {
            replySize = product.getComments().get(i).getReplies().size();
            System.out.println(replySize);
        }
        commentBox.getChildren().addAll(commentatorLabel, contentLabel);
        commentBox.setStyle(
                "-fx-border-color: #ccc;" +
                        "-fx-background-color: beige;" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 1);"
        );
        commentVBox.getChildren().add(commentBox);
        commentVBox.setMaxWidth(1204);
        commentVBox.setSpacing(5);
        if (replySize != 0 && check == 0) {
            for (int j = 0; j < replySize; j++) {
                check = 1;
                showComments(j,
                        product.getComments().get(i).getReplies().get(j).getReplier(),
                        product.getComments().get(i).getReplies().get(j).getReplyContent()
                );
                check = 0;
            }
        }
    }

    public String standardization(String price) {
        if (price == null || price.isEmpty()) {
            return "N/A";
        }
        try {
            String currency = "";
            String numberPart = price;
            if (!Character.isDigit(price.charAt(price.length() - 1))) {
                currency = " " + price.charAt(price.length() - 1);
                numberPart = price.substring(0, price.length() - 1);
            }
            long number = Long.parseLong(numberPart.replaceAll("[^\\d]", ""));
            return String.format("%,d", number) + currency;
        } catch (NumberFormatException e) {
            System.err.println("Could not parse price: " + price);
            return price;
        }
    }

    @FXML
    public void openChatbot() {
        Bot chatbot = new Bot();
        Stage chatStage = new Stage();
        chatbot.createChatUI(chatStage);
        chatStage.show();
    }
}
