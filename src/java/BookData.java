import java.util.ArrayList;
import java.util.List;

/**
 * 用来模拟初始数据的类
 * */
public class BookData {


    int[] ids = new int[]{35, 16, 18, 70, 5, 50, 22, 60, 13, 17, 12, 45, 25, 42, 15, 90, 30, 7};

    String[] names = new String[]{"Spring实战 第5版","Spring 5核心原理与30个类手写实战","Spring 5 设计模式","Spring MVC+MyBatis开发从入门到项目实战"
    ,"轻量级Java Web企业应用实战","Java核心技术 卷I 基础知识（原书第11版）","深入理解Java虚拟机","Java编程思想（第4版）",
    "零基础学Java（全彩版）","直播就该这么做：主播高效沟通实战指南","直播销讲实战一本通","直播带货：淘宝、天猫直播从新手到高手"
    ,"Spring框架","C语言基础","C++语言基础","数据结构与算法","软件工程","数据库概论"};
    String[] owners = new String[]{"李一帆","吴凡","逸风","何璐","李一帆","吴凡","逸风","何璐","李一帆","吴凡","逸风","何璐","李一帆","吴凡","逸风","何璐","李武","五河"};
    int[] now = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
    int[] total = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};

    public List<Book> getData() {
        List<Book> data = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            Book book = new Book();
            book.setId(ids[i]);
            book.setName(names[i]);
            book.setAuthor(owners[i]);
            book.setNowStore(now[i]);
            book.setTotalStore(total[i]);
            data.add(book);
        }
        return data;
    }
}
