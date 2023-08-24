public class Book {

    private Integer id;    //书号
    private String name;    //书名
    private String author;   //著者
    private Integer nowStore;   //现存量
    private Integer totalStore;     //总库存量

    public Book() {
        nowStore = 1;
        totalStore = 1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getNowStore() {
        return nowStore;
    }

    public void setNowStore(Integer nowStore) {
        this.nowStore = nowStore;
    }

    public Integer getTotalStore() {
        return totalStore;
    }

    public void setTotalStore(Integer totalStore) {
        this.totalStore = totalStore;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner='" + author + '\'' +
                ", nowStore=" + nowStore +
                ", totalStore=" + totalStore +
                '}';
    }
}
