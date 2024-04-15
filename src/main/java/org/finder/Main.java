package org.finder;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        JFrame Frame = new JFrame("App");
        Frame.setContentPane(new App().panelMain);
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.pack();
        Frame.setVisible(true);
    }
}