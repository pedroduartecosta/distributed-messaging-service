package GUI;

import javax.swing.*;
import java.awt.event.*;

import Client.Client;
import Controller.Controller;

public class Menu extends InterfaceView {
    private JPanel contentPane;
    private JButton signUpButton;
    private JButton exitButton;
    private JButton signInButton;
    //private Menu dialog;

    private Controller controller;

    public Menu(Controller controller){
        super(controller);
        this.controller = controller;

        setContentPane(contentPane);
        this.pack();
        this.setVisible(true);

        //setModal(true);
        //getRootPane().setDefaultButton(buttonOK);

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onExit();
            }
        });

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                showSignInView();

            }
        });
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                showSignUpView();

            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onExit() {
        // add your code here if necessary
        dispose();
        System.exit(0);
    }
}
