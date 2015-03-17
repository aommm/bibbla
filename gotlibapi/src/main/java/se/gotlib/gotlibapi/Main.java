package se.gotlib.gotlibapi;

import se.gotlib.gotlibapi.tests.LoginTest;

public class Main {

    public static void main(String[] args) {
        System.out.println("--- running tests...");
        LoginTest.run();

        System.out.println("--- ... all tests done");
    }

}
