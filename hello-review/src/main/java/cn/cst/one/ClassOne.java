package cn.cst.one;

public abstract class ClassOne implements IClassOne, IClassOne2 {
    abstract String getName();

    String son = "son";

    ClassOne() {
    }

    int getAge() {
        return 1;
    }
}
