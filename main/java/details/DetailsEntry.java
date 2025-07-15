package details;

public class DetailsEntry {
    private final String label;
    private final String value;

    public DetailsEntry(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() { return label; }
    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        //Kiểm tra o có phải là một đối tượng thuộc lớp DetailsEntry không.
        //Nếu đúng, ép kiểu o sang DetailsEntry và gán vào biến that.
        if (!(o instanceof DetailsEntry that)) return false;
        return label.equals(that.label) && value.equals(that.value);
    }

    @Override
    //hashCode() là một hàm sinh ra một số nguyên (int) đại diện cho giá trị băm (hash) của đối tượng.
    //Hai đối tượng bằng nhau theo equals() thì bắt buộc phải có cùng hashCode().
    public int hashCode() {
        return 31 * label.hashCode() + value.hashCode();
    }

    @Override
    public String toString() {
        return "DetailEntry{" +
                "label='" + label + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

