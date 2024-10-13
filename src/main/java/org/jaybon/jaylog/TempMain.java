package org.jaybon.jaylog;

public class TempMain {
    public static void main(String[] args) {
        System.out.println("Hello, world!");
    }
}

class Human {

    String name;
    int age;

    public boolean isAdult() {
        return age > 18;
    }

    // 이 메소드는 굳이 Human 클래스에 있을 필요가 없다
    public static boolean isAdult(int age) {
        return age > 18;
    }

}

class Util {

    public static boolean isAdult(int age) {
        return age > 18;
    }

}
