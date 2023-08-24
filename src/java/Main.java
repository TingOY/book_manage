import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// B树节点类
class BTreeNode {
    private List<Integer> keys;
    private List<Book> books;
    private List<BTreeNode> children;
    private boolean isLeaf;

    public BTreeNode(boolean isLeaf) {
        keys = new ArrayList<>();
        books = new ArrayList<>();
        children = new ArrayList<>();
        this.isLeaf = isLeaf;
    }

    public List<Integer> getKeys() {
        return keys;
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<BTreeNode> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return isLeaf;
    }
}
// B树类
class BTree {
    private BTreeNode root;
    private int t;

    public BTree(int t) {
        this.root = new BTreeNode(true);
        this.t = t;
    }

    /*插入结点*/
    public void insert(Book book) {
        BTreeNode r = root;
        if (r.getKeys().size() == (2 * t - 1)) {
            BTreeNode s = new BTreeNode(false);
            root = s;
            s.getChildren().add(r);
            splitChild(s, 0);
            insertNonFull(s, book);
        } else {
            insertNonFull(r, book);
        }
    }

    /*未满时插入结点*/
    private void insertNonFull(BTreeNode node, Book book) {
        int i = node.getKeys().size() - 1;
        if (node.isLeaf()) {
            while (i >= 0 && book.getId() < node.getKeys().get(i)) {
                i--;
            }
            node.getKeys().add(i + 1, book.getId());
            node.getBooks().add(i + 1, book);
        } else {
            while (i >= 0 && book.getId() < node.getKeys().get(i)) {
                i--;
            }
            i++;
            if (node.getChildren().get(i).getKeys().size() == (2 * t - 1)) {
                splitChild(node, i);
                if (book.getId() > node.getKeys().get(i)) {
                    i++;
                }
            }
            insertNonFull(node.getChildren().get(i), book);
        }
    }

    /*分裂子节点*/
    private void splitChild(BTreeNode parentNode, int childIndex) {
        BTreeNode newChildNode = new BTreeNode(parentNode.getChildren().get(childIndex).isLeaf());
        BTreeNode childNode = parentNode.getChildren().get(childIndex);

        parentNode.getKeys().add(childIndex, childNode.getKeys().get(t - 1));
        parentNode.getBooks().add(childIndex, childNode.getBooks().get(t - 1));
        parentNode.getChildren().add(childIndex + 1, newChildNode);

        for (int i = 0; i < t - 1; i++) {
            newChildNode.getKeys().add(i, childNode.getKeys().get(i + t));
            newChildNode.getBooks().add(i, childNode.getBooks().get(i + t));
        }

        if (!childNode.isLeaf()) {
            for (int i = 0; i < t; i++) {
                newChildNode.getChildren().add(i, childNode.getChildren().get(i + t));
            }
        }

        childNode.getKeys().subList(t - 1, childNode.getKeys().size()).clear();
        childNode.getBooks().subList(t - 1, childNode.getBooks().size()).clear();
        childNode.getChildren().subList(t, childNode.getChildren().size()).clear();
    }

    /*按索引查找结点*/
    public Book search(int key) {
        return search(root, key);
    }

    /*按索引查找*/
    private Book search(BTreeNode node, int key) {
        int i = 0;
        while (i < node.getKeys().size() && key > node.getKeys().get(i)) {
            i++;
        }
        if (i < node.getKeys().size() && key == node.getKeys().get(i)) {
            return node.getBooks().get(i);
        } else if (node.isLeaf()) {
            return null;
        } else {
            return search(node.getChildren().get(i), key);
        }
    }

    /*按索引删除*/
    public void delete(int key) {
        delete(root, key);
        if (root.getKeys().isEmpty() && !root.isLeaf()) {
            root = root.getChildren().get(0);
        }
    }

    /*按索引删除*/
    private void delete(BTreeNode node, int key) {
        int i = 0;
        while (i < node.getKeys().size() && key > node.getKeys().get(i)) {
            i++;
        }
        if (i < node.getKeys().size() && key == node.getKeys().get(i)) {
            if (node.isLeaf()) {
                node.getKeys().remove(i);
                node.getBooks().remove(i);
            } else {
                BTreeNode pred = node.getChildren().get(i);
                if (pred.getKeys().size() >= t) {
                    int predecessorKey = getPredecessorKey(pred);
                    Book predecessorBook = search(predecessorKey);
                    node.getKeys().set(i, predecessorKey);
                    node.getBooks().set(i, predecessorBook);
                    delete(pred, predecessorKey);
                } else {
                    BTreeNode succ = node.getChildren().get(i + 1);
                    if (succ.getKeys().size() >= t) {
                        int successorKey = getSuccessorKey(succ);
                        Book successorBook = search(successorKey);
                        node.getKeys().set(i, successorKey);
                        node.getBooks().set(i, successorBook);
                        delete(succ, successorKey);
                    } else {
                        mergeChildren(node, i);
                        delete(pred, key);
                    }
                }
            }
        } else if (node.isLeaf()) {
            return;
        } else {
            BTreeNode child = node.getChildren().get(i);
            if (child.getKeys().size() < t) {
                BTreeNode leftSibling = (i > 0) ? node.getChildren().get(i - 1) : null;
                BTreeNode rightSibling = (i < node.getChildren().size() - 1) ? node.getChildren().get(i + 1) : null;

                if (leftSibling != null && leftSibling.getKeys().size() >= t) {
                    borrowFromLeftSibling(node, i);
                } else if (rightSibling != null && rightSibling.getKeys().size() >= t) {
                    borrowFromRightSibling(node, i);
                } else if (leftSibling != null) {
                    mergeChildren(node, i - 1);
                    child = leftSibling;
                } else {
                    mergeChildren(node, i);
                }
            }
            delete(child, key);
        }
    }

    private int getPredecessorKey(BTreeNode node) {
        if (node.isLeaf()) {
            return node.getKeys().get(node.getKeys().size() - 1);
        }
        return getPredecessorKey(node.getChildren().get(node.getChildren().size() - 1));
    }

    private int getSuccessorKey(BTreeNode node) {
        if (node.isLeaf()) {
            return node.getKeys().get(0);
        }
        return getSuccessorKey(node.getChildren().get(0));
    }

    /*调整左边*/
    private void borrowFromLeftSibling(BTreeNode parentNode, int childIndex) {
        BTreeNode childNode = parentNode.getChildren().get(childIndex);
        BTreeNode leftSibling = parentNode.getChildren().get(childIndex - 1);

        childNode.getKeys().add(0, parentNode.getKeys().get(childIndex - 1));
        childNode.getBooks().add(0, parentNode.getBooks().get(childIndex - 1));
        parentNode.getKeys().set(childIndex - 1, leftSibling.getKeys().get(leftSibling.getKeys().size() - 1));
        parentNode.getBooks().set(childIndex - 1, leftSibling.getBooks().get(leftSibling.getBooks().size() - 1));
        if (!leftSibling.isLeaf()) {
            childNode.getChildren().add(0, leftSibling.getChildren().get(leftSibling.getChildren().size() - 1));
            leftSibling.getChildren().remove(leftSibling.getChildren().size() - 1);
        }
        leftSibling.getKeys().remove(leftSibling.getKeys().size() - 1);
        leftSibling.getBooks().remove(leftSibling.getBooks().size() - 1);
    }

    /*调整右边*/
    private void borrowFromRightSibling(BTreeNode parentNode, int childIndex) {
        BTreeNode childNode = parentNode.getChildren().get(childIndex);
        BTreeNode rightSibling = parentNode.getChildren().get(childIndex + 1);

        childNode.getKeys().add(parentNode.getKeys().get(childIndex));
        childNode.getBooks().add(parentNode.getBooks().get(childIndex));
        parentNode.getKeys().set(childIndex, rightSibling.getKeys().get(0));
        parentNode.getBooks().set(childIndex, rightSibling.getBooks().get(0));
        if (!rightSibling.isLeaf()) {
            childNode.getChildren().add(rightSibling.getChildren().get(0));
            rightSibling.getChildren().remove(0);
        }
        rightSibling.getKeys().remove(0);
        rightSibling.getBooks().remove(0);
    }

    /*合并*/
    private void mergeChildren(BTreeNode parentNode, int childIndex) {
        BTreeNode leftChild = parentNode.getChildren().get(childIndex);
        BTreeNode rightChild = parentNode.getChildren().get(childIndex + 1);

        leftChild.getKeys().add(parentNode.getKeys().get(childIndex));
        leftChild.getBooks().add(parentNode.getBooks().get(childIndex));
        leftChild.getKeys().addAll(rightChild.getKeys());
        leftChild.getBooks().addAll(rightChild.getBooks());
        if (!rightChild.isLeaf()) {
            leftChild.getChildren().addAll(rightChild.getChildren());
        }

        parentNode.getKeys().remove(childIndex);
        parentNode.getBooks().remove(childIndex);
        parentNode.getChildren().remove(childIndex + 1);
    }

    //遍历B树
    public void traverse() {
        traverse(root);
    }

    private void traverse(BTreeNode node) {
        int i;
        for (i = 0; i < node.getKeys().size(); i++) {
            if (!node.isLeaf()) {
                traverse(node.getChildren().get(i));
            }
            System.out.println(node.getBooks().get(i));
        }
        System.out.println("总共：" + node.getKeys().size());
        if (!node.isLeaf()) {
            traverse(node.getChildren().get(i));
        }
    }
}

public class Main {
    public static void main(String[] args) {
        //获取初始数据
        BookData bookData = new BookData();
        List<Book> books = bookData.getData();

        //借阅者数据
        List<BorrowLog> borrowLogs = new ArrayList<>();

        // 创建B树
        int t = 100; // t值决定了B树的阶数
        BTree bTree = new BTree(t);

        //创建B树索引
        for (Book book : books) {
            bTree.insert(book);
        }

        System.out.println("欢迎来到图书管理系统！");
        fun();

        Scanner sc = new Scanner(System.in);
        int id; //书号
        String  name;    //书名
        String author;  //作者
        int borrowId;    //借阅者id
        String data;    //归还时间

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
                    addBook(book, bTree);
                    showBook(bTree);
                    fun();
                    break;
                case 2:
                    System.out.println("请输入要清除的书号：");
                    id = sc.nextInt();
                    deleteBook(id, bTree);
                    showBook(bTree);
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
                    boolean flag = borrowBook(id, bTree);
                    if (flag)
                        borrowLogs.add(borrowLog);
                    fun();
                    break;
                case 4:
                    System.out.println("请输入借阅记录编号：");
                    int logId = sc.nextInt();
                    BorrowLog borrowLog1 = borrowLogs.get(logId - 1);
                    returnBook(borrowLog1.getBookId(), bTree);
                    borrowLogs.remove(logId-1);
                    fun();
                case 5:
                    bTree.traverse();
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
                    break;
            }
        }
    }
    /**
     * 采编入库
     * */
    public static void addBook(Book book, BTree bTree) {
        if (bTree.search(book.getId()) == null)
            bTree.insert(book);
        else {
            Book b = bTree.search(book.getId());
            b.setNowStore(b.getNowStore()+1);
            b.setTotalStore(b.getTotalStore()+1);
        }
        System.out.println("添加成功！");
    }

    /**
     * 清除库存
     * */
    public static void deleteBook(int id, BTree bTree) {
        if (bTree.search(id) == null) {
            System.out.println("该图书不存在！");
        } else {
            bTree.delete(id);
            System.out.println("删除成功！");
        }
    }

    /**
     * 借阅
     * */
    public static boolean borrowBook(int bookId, BTree bTree) {
        Book book = bTree.search(bookId);
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
    public static void returnBook(int bookId, BTree bTree) {
        Book book = bTree.search(bookId);
        book.setNowStore(book.getNowStore()+1);
        System.out.println("归还成功！");
    }

    /**
    * 显示
    * */
    public static void showBook(BTree bTree) {
        bTree.traverse();
    }

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