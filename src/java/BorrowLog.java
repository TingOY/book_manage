import javax.xml.crypto.Data;

public class BorrowLog {
    private Integer id;    //借阅者图书证号

    private Integer bookId;  //书号
    private String returnTime;    //归还时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    @Override
    public String toString() {
        return "BorrowLog{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", returnTime='" + returnTime + '\'' +
                '}';
    }
}
