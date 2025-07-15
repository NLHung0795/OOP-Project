package view;

import details.DetailsEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import product.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompareTable {
    private TableView<List<String>> compareTable;
    private TableColumn<List<String>, String> detailsColumn;
    private TableColumn<List<String>, String> firstProductColumn;
    private TableColumn<List<String>, String> secondProductColumn;

    public void setData(TableView<List<String>> compareTable,TableColumn<List<String>, String> detailsColumn, TableColumn<List<String>, String> firstProductColumn, TableColumn<List<String>, String> secondProductColumn){
        this.compareTable= compareTable;
        this.detailsColumn = detailsColumn;
        this.secondProductColumn = secondProductColumn;
        this.firstProductColumn = firstProductColumn;
    }

    public void customCompareTable(Product<?> product1, Product<?> product2) {
        compareTable.getColumns().clear();
        compareTable.getItems().clear();
        compareTable.setFixedCellSize(Region.USE_COMPUTED_SIZE);

        List<DetailsEntry> d1 = product1.allDetails();
        List<DetailsEntry> d2 = product2.allDetails();

        ArrayList<List<String>> arr = new ArrayList<>();
        arr.add(Arrays.asList("", product1.getPicture_url(), product2.getPicture_url()));

        int maxSize = Math.max(d1.size(), d2.size());
        for (int i = 0; i < maxSize; i++) {
            String label = (i < d1.size()) ? d1.get(i).getLabel() : (i < d2.size() ? d2.get(i).getLabel() : "N/A");
            String val1 = (i < d1.size()) ? d1.get(i).getValue() : "N/A";
            String val2 = (i < d2.size()) ? d2.get(i).getValue() : "N/A";
            arr.add(Arrays.asList(label, val1, val2));
        }

        detailsColumn.setText("Thông số");
        firstProductColumn.setText(product1.getName());
        secondProductColumn.setText(product2.getName());

        setColumn(detailsColumn, arr, 0);
        setColumn(firstProductColumn, arr, 1);
        setColumn(secondProductColumn, arr, 2);

        compareTable.getColumns().addAll(detailsColumn, firstProductColumn, secondProductColumn);

        ObservableList<List<String>> data = FXCollections.observableArrayList(arr);
        compareTable.setItems(data);
    }

    private void setColumn(TableColumn<List<String>, String> col, ArrayList<List<String>> arr, int index) {
        col.setCellValueFactory(cellData -> {
            List<String> rowValue = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                    (rowValue != null && index < rowValue.size()) ? rowValue.get(index) : null
            );
        });

        col.setCellFactory(column -> new TableCell<List<String>, String>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                int currentIndex = getIndex();
                if (empty || currentIndex < 0 || currentIndex >= arr.size()) {
                    setText(null); setGraphic(null); setStyle("");
                } else {
                    String currentItem = arr.get(currentIndex).get(index);
                    if (currentItem == null) currentItem = "";

                    if (currentIndex == 0) {
                        if (index > 0 && !currentItem.isEmpty()) {
                            try {
                                imageView.setImage(new Image(currentItem, true));
                                imageView.setFitWidth(150); imageView.setFitHeight(150);
                                imageView.setPreserveRatio(true);
                                setGraphic(imageView); setText(null); setAlignment(Pos.CENTER);
                            } catch (Exception e) {
                                setText("No Image"); setGraphic(null); setAlignment(Pos.CENTER);
                            }
                        } else { setText(""); setGraphic(null); }
                        setPrefHeight(160); setStyle("-fx-border-color: lightgrey; -fx-border-width: 0 0 1 0;");
                    } else {
                        setText(currentItem); setGraphic(null); setAlignment(Pos.CENTER_LEFT);
                        setPrefHeight(Control.USE_COMPUTED_SIZE);
                        setStyle(currentIndex % 2 == 0 ? "-fx-background-color: #f9f9f9;" : "-fx-background-color: white;");
                        if (index == 0) { setStyle(getStyle() + "-fx-font-weight: bold;"); }
                    }
                }
            }
        });
    }
}
