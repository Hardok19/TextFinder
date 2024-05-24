package org.finder;

import org.finder.Main.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    private JButton button123;
    public JPanel panelMain;

    public App() {
            button123.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "ULA");
            }


        });

            //EJEMPLOS DE PARA LLAMAR MÉTODOS DE A BIBLIOTECA EN MAIN
        Main.addFile("src/main/resources/example.txt");
        Main.deleteFile("src/main/resources/example.txt");
    }
}
