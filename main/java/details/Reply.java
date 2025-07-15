package details;

public class Reply {
    private String replier;
    private String reply_content;

    public Reply(){}

    public Reply(String replier, String reply_content) {
        this.replier = replier;
        this.reply_content = reply_content;
    }

    public String getReplier() { return replier; }
    public void setReplier(String replier) { this.replier = replier; }

    public String getReplyContent() { return reply_content; }
    public void setReply_content(String reply_content) { this.reply_content = reply_content; }
}
