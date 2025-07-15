package main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.apache.commons.lang3.StringEscapeUtils.escapeJson;

public class Bot extends Application {
    private static final String DATA_FILE_PATH = "D:\\New folder\\jetbrain\\code\\ProjectOOP_final2\\ProjectOOP_final2\\src\\main\\resources\\data\\data_bot.json";
    private static final String API_KEY = "AIzaSyD6ABHdkv9kTHF5c1z-WT5Foc1LwEfpwIs";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/"
                + "gemini-1.5-flash:generateContent";

    private TextArea chatArea;
    private TextField userInput;
    private ImageView backgroundImageView;

    @Override
    public void start(Stage primaryStage) {
        createChatUI(primaryStage);
        primaryStage.setTitle("Chatbot");
        primaryStage.show();
    }

    public void createChatUI(Stage chatStage) {
        chatStage.setTitle("Chatbot");

        // Khung hiển thị cuộc trò chuyện
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setStyle("-fx-control-inner-background: #FFE6F0; -fx-font-size: 14px; -fx-font-family: 'Comic Sans MS';");

         //Nền GIF
        Image gifImage = new Image("https://media3.giphy.com/media/v1.Y2lkPTc5MGI3NjExY2V4Z2tkM3Bla2c0c3d6YXVtaGJkeDlxdzlsc2h5bmplN3k3MzBuOCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/h0VzgrFX9AKXK/giphy.gif");
        backgroundImageView = new ImageView(gifImage);
        backgroundImageView.setFitWidth(900);
        backgroundImageView.setFitHeight(600);
        backgroundImageView.setOpacity(1);

        // Ô nhập văn bản
        userInput = new TextField();
        userInput.setPromptText("Nhập câu hỏi của bạn...");
        userInput.setStyle("-fx-background-color: #FFF0F5; -fx-font-family: 'Comic Sans MS';");

        // Nút gửi tin nhắn
        Button sendButton = new Button("Gửi");
        sendButton.setStyle("-fx-background-color: #FFB6C1; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-background-radius: 15; -fx-font-family: 'Comic Sans MS';");
        sendButton.setOnAction(e -> sendMessage());

        // Nhấn Enter gửi tin nhắn và hiện GIF
        userInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessage();
            }
        });

        StackPane root = new StackPane();
        //root.getChildren().addAll(backgroundImageView);

        VBox chatLayout = new VBox(15, chatArea, userInput, sendButton);
        chatLayout.setPadding(new Insets(20));
        chatLayout.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);");

        root.getChildren().add(chatLayout);


        Scene scene = new Scene(root, 900, 600);
        chatStage.setScene(scene);
    }

    private void sendMessage() {
        String question = userInput.getText().trim();
        if (!question.isEmpty()) {
            chatArea.appendText("You: " + question + "\n");
            userInput.clear();
            new Thread(() -> {
                String response = callGeminiAPI(question);
                javafx.application.Platform.runLater(() -> chatArea.appendText("Bot: " + response + "\n\n"));
            }).start();
        }
    }

    private String callGeminiAPI(String question) {
        HttpClient client = HttpClient.newHttpClient();

        String fileContent;
        try {
            fileContent = Files.readString(Paths.get(DATA_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
            return "Không thể đọc file data.json!";
        }

        String requestBody = "{ \"contents\": " +
                "[{ \"parts\": " +
                "[" +
                "{ \"text\": \"Read this file.: " + escapeJson(fileContent) + "\" }," +
                "{ \"text\": \"Question: " + escapeJson(question) + "\" }," +
                "{ \"text\": \"Answer naturally and vividly, avoid using phrases like data-based, and answer phone-related questions based on the information above..\" }," +
                "{ \"text\": \"Tra loi bang tieng Viet\" }" +
                "] }] }";

        String fullUrl = API_URL + "?key=" + API_KEY;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            return parseResponse(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Lỗi khi kết nối đến API!";
        }
    }

    private String parseResponse(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);

            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi xử lý phản hồi!";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}