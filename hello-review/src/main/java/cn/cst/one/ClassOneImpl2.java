package cn.cst.one;

public class ClassOneImpl2 extends ClassOne {
    @Override
    String getName() {
        return null;
    }

    public static void main(String[] args) {
        ClassOne classOne = new ClassOneImpl();
        System.out.println(classOne.son);
        classOne.son = "sone1";
        System.out.println(classOne.son);
        ClassOne classOne1 = new ClassOneImpl2();
        System.out.println(classOne1.son);

    }

    @Override
    public String getUser() {
        return null;
    }

    @Override
    public String getBody() {
        return null;
    }
}
