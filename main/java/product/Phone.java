package product;

import details.PhoneDetails;
import details.Comment;

import java.util.List;


public class Phone extends Product<PhoneDetails> {
    public Phone(String name, String price, String brand, String type, PhoneDetails details,
                 int total_count, double average_rating, String main_content, String picture_url, List<Comment> comments) {
        super(name, price, type, picture_url, brand, main_content, total_count, average_rating, details, comments);
    }

    public Phone(){
        super(null, null, null, null, null, null, 0, 0, null, null);
    }
}
