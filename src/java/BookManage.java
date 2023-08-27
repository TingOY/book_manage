import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookManage {
    public static void main(String[] args) {
        start();
    }

    /**
     * 主程序开始
     * */
    public static void start() {
        //获取初始数据
        BookData bookData = new BookData();
        List<Book> books = bookData.getData();
        MyLinkedList list = new MyLinkedList();
        //借阅者数据
        List<BorrowLog> borrowLogs = new ArrayList<>();

        for (Book book : books) {
            list.addNode(book);
        }

        System.out.println("欢迎来到图书管理系统！");
        fun();

        Scanner sc = new Scanner(System.in);
        int id; //书号
        String  name;    //书名
        String author;  //作者
        int borrowId;    //借阅者id
        String data;    //归还时间
        int sign = 0;
        while (true) {
            int select = sc.nextInt();
            switch (select) {
                case 1:
                    Book book = new Book();
                    System.out.println("书号：");
                    id = sc.nextInt();
                    System.out.println("书名：");
                    name = sc.next();
                    System.out.println("作者：");
                    author = sc.next();
                    book.setId(id);
                    book.setName(name);
                    book.setAuthor(author);
                    addBook(book, list);
                    showBook(list);
                    fun();
                    break;
                case 2:
                    System.out.println("请输入要清除的书号：");
                    id = sc.nextInt();
                    deleteBook(id, list);
                    showBook(list);
                    fun();
                    break;
                case 3:
                    BorrowLog borrowLog = new BorrowLog();
                    System.out.println("借阅者信息(日期格式YY-MM-DD)");
                    System.out.println("书号：");
                    id = sc.nextInt();
                    System.out.println("借阅者证号：");
                    borrowId = sc.nextInt();
                    System.out.println("归还日期：");
                    data = sc.next();
                    borrowLog.setId(borrowId);
                    borrowLog.setBookId(id);
                    borrowLog.setReturnTime(data);
                    boolean flag = borrowBook(id, list);
                    if (flag)
                        borrowLogs.add(borrowLog);
                    fun();
                    break;
                case 4:
                    System.out.println("请输入借阅记录编号：");
                    int logId = sc.nextInt();
                    BorrowLog borrowLog1 = borrowLogs.get(logId - 1);
                    returnBook(borrowLog1.getBookId(), list);
                    borrowLogs.remove(logId-1);
                    fun();
                case 5:
                    list.show();
                    fun();
                    break;
                case 6:
                    for (BorrowLog log : borrowLogs) {
                        System.out.println(log);
                    }
                    fun();
                    break;
                default:
                    System.out.println("谢谢使用!");
                    sign = 1;
                    break;
            }
            if (sign == 1)
                break;
        }
    }

    /**
     * 采编入库
     * */
    public static void addBook(Book book, MyLinkedList list) {
        if (list.search(book.getId()) == null)
            list.addNode(book);
        else {
            Book b = list.search(book.getId());
            b.setNowStore(b.getNowStore()+1);
            b.setTotalStore(b.getTotalStore()+1);
        }
        System.out.println("添加成功！");
    }

    /**
     * 清除库存
     * */
    public static void deleteBook(int id, MyLinkedList list) {
        if (list.search(id) == null) {
            System.out.println("该图书不存在！");
        } else {
            list.deleteNode(id);
            System.out.println("删除成功！");
        }
    }

    /**
     * 借阅
     * */
    public static boolean borrowBook(int bookId, MyLinkedList list) {
        Book book = list.search(bookId);
        if (book == null) {
            System.out.println("不存在该书籍!");
            return false;
        }
        if (book.getNowStore() > 0) {
            book.setNowStore(book.getNowStore()-1);
            System.out.println("借阅成功!");
            return true;
        } else {
            System.out.println("该书库存容量不足!");
            return false;
        }
    }

    /**
     * 归还
     * */
    public static void returnBook(int bookId, MyLinkedList list) {
        Book book = list.search(bookId);
        book.setNowStore(book.getNowStore()+1);
        System.out.println("归还成功！");
    }

    /**
     * 显示
     * */
    public static void showBook(MyLinkedList list) {
        list.show();
    }

    /**
     * 显示菜单
     * */
    public static void fun() {
        System.out.println("----------菜单----------");
        System.out.println("1.采编入库");
        System.out.println("2.清除库存");
        System.out.println("3.借阅");
        System.out.println("4.归还");
        System.out.println("5.显示");
        System.out.println("6.借阅记录");
        System.out.println("7.退出系统");
        System.out.println("请输入你的操作:");
    }
}

class MyLinkedList {
    int size;
    Node head;

    public MyLinkedList() {
        size = 0;
        head = new Node(0,null);
    }

    /*添加结点*/
    public void addNode(Book book) {
        Node temp = head;
        while(temp.next != null) {
            if (book.getId() < temp.next.key)
                break;
            temp = temp.next;
        }
        Node node = new Node(book.getId(), book);
        node.next = temp.next;
        temp.next = node;
        size++;
    }

    /*删除结点*/
    public void deleteNode(Integer key) {
        Node temp = head;
        while(temp.next != null) {
            if(temp.next.key == key)
                break;
            temp = temp.next;
        }
        temp.next = temp.next.next;
        size--;
    }

    public Book search(Integer key) {
        Node temp = head;
        while (temp != null) {
            if (temp.key == key)
                return temp.book;
            temp = temp.next;
        }
        return null;
    }

    /*输出链表*/
    public void show() {
        Node temp = head.next;
        while (temp != null) {
            System.out.println(temp);
            temp = temp.next;
        }
        System.out.println("总共" + size + "条数据");
    }
}
class Node {
    Integer key;
    Book book;
    Node next;

    Node(Integer key, Book book) {
        this.key = key;
        this.book = book;
    }

    @Override
    public String toString() {
        return "Node{" +
                "key=" + key +
                ", book=" + book +
                '}';
    }
}

