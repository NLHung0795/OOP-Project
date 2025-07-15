package structure;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import product.Product;

import java.util.*;

public class TreeProduct extends TreeItem<String> {
    private static ArrayList<Product<?>> productList;
    private TreeSet<String> productBrand;
    private TreeSet<String> productType;
    private static int stt = 0;


    public TreeProduct(String root){
        super(root);
    }
    public void initData(ArrayList<Product<?>> productList, TreeSet<String> productBrand, TreeSet<String> productType){
        TreeProduct.productList = productList;
        this.productBrand = productBrand;
        this.productType = productType;
    }

    public ObservableList<TreeItem<String>> bfs(String check) {
        Queue<TreeItem<String>> queue = new ArrayDeque<>();

        queue.add(this);
        while (!queue.isEmpty()) {
            TreeItem<String> current = queue.poll();
            if (current.getValue().equalsIgnoreCase(check)) {
                ArrayList<TreeItem<String>> result = new ArrayList<>();
                for (TreeItem<String> child : current.getChildren()) {
                    if (child.getChildren().isEmpty()) return current.getChildren();
                    else result.addAll(child.getChildren());
                }
                return FXCollections.observableArrayList(result);
            } else {
                queue.addAll(current.getChildren());
            }
        }
        return null;
    }

    public void addProductToTree(Product<?> product, TreeItem<String> itemTree){
        String brand = product.getBrand().toLowerCase();
        TreeItem<String> brandNode = null;
        for(TreeItem<String> itemBrand : itemTree.getChildren()){
            if(itemBrand.getValue().equals(brand)){ brandNode = itemBrand; break; }
        }
        if(brandNode == null){
            brandNode = new TreeItem<>(brand);
            itemTree.getChildren().add(brandNode);
            productBrand.add(brand);
        }
        TreeItem<String> order = new TreeItem<>(""+ stt);
        brandNode.getChildren().add(order);
        stt++;
    }



    public void createTree(){
        this.getChildren().clear();
        stt = 0;
        for(Product<?> product : productList){
            String type = product.getType();
            if (type == null) continue;

            TreeItem<String> typeNode = null;
            for(TreeItem<String> itemType : this.getChildren()){
                if(itemType.getValue().equals(type)){
                    typeNode = itemType;
                    break;
                }
            }
            if(typeNode == null){
                typeNode = new TreeItem<>(type);
                this.getChildren().add(typeNode);
                productType.add(type);
            }
            addProductToTree(product, typeNode);
        }
        Comparator<TreeItem<String>> sorter = Comparator.comparing(TreeItem::getValue);
        this.getChildren().sort(sorter);
        for(TreeItem<String> type : this.getChildren()){ type.getChildren().sort(sorter); }
    }
}
