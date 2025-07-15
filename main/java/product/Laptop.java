package product;

import details.LaptopDetails;
import details.Comment;

import java.util.List;

public class Laptop extends Product<LaptopDetails> {
    public Laptop(String name, String price, String type, String picture_url, String brand, String main_content, int total_count, double average_rating, List<Comment> comments, LaptopDetails details) {
        super(name, price, type, picture_url, brand, main_content, total_count, average_rating, details, comments);
    }

    public Laptop(){
        super(null, null, null, null, null, null, 0, 0, null, null);
    }
}