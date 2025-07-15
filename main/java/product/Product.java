package product;

import details.Comment;
import details.DetailsEntry;
import details.IDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Product<D extends IDetails> {
    private String name;
    private String price;
    private String type;
    private String picture_url;
    private String brand;
    private String main_content;
    private int total_count;
    private double average_rating;
    private D details;
    private List<Comment> comments;

    public Product(){}

    public Product(String name, String price, String type, String picture_url, String brand,
                   String main_content, int total_count, double average_rating, D details, List<Comment> comments) {
        this.name = name;
        this.price = price;
        this.details = details;
        this.type = type;
        this.picture_url = picture_url;
        this.brand = brand;
        this.main_content = main_content;
        this.total_count = total_count;
        this.average_rating = average_rating;
        this.comments = comments != null
                ? new ArrayList<>(comments)
                : new ArrayList<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getType() { return type; }

    public String getPicture_url() { return picture_url; }

    public String getBrand() { return brand; }

    public String getMain_content() { return main_content; }

    public int getTotal_count() { return total_count; }

    public double getAverage_rating() { return average_rating; }

    public D getDetails() { return details; }
    public void setDetails(D details) { this.details = details; }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }
    public void setComments(List<Comment> comments) {
        this.comments = new ArrayList<>(comments);
    }

    public List<DetailsEntry> allDetails() {
        List<DetailsEntry> list = new ArrayList<>();
        list.add(new DetailsEntry("Name", name));
        list.add(new DetailsEntry("Price", price));
        if (details != null) {
            list.addAll(details.details());
        }
        else System.out.println("NF");
        return Collections.unmodifiableList(list);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setMain_content(String main_content) {
        this.main_content = main_content;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public void setAverage_rating(double average_rating) {
        this.average_rating = average_rating;
    }
}

