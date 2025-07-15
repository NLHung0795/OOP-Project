package details;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Comment {
    private String commentator;
    private String content;
    private List<Reply> replies;

    public Comment(){}

    public Comment(String commentator, String content, List<Reply> replies) {
        this.commentator = commentator;
        this.content = content;
        this.replies = replies != null
                ? new ArrayList<>(replies)
                : new ArrayList<>();
    }

    public String getCommentator() { return commentator; }
    public void setCommentator(String commentator) { this.commentator = commentator; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<Reply> getReplies() {
        return Collections.unmodifiableList(replies);
    }
    public void setReplies(List<Reply> replies) {this.replies = new ArrayList<>(replies);
    }
}
