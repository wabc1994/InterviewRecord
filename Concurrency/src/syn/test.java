package syn;
public class test {
    public static void main(String[] args) {
        ThreadA a =new ThreadA();
        a.setName("A");
        ThreadB b =new ThreadB();
        b.setName("B");
        a.start();
        b.start();
    }
}
