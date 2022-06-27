package com.dj.tool.ui;

import com.dj.tool.common.ApplicationCache;
import com.dj.tool.model.CodeAuditSettingModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CodeAuditSettingDialog extends JDialog {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField urlTextField;
    private JTextField userNameTextField;
    private JTextField passwordTextField;


    public CodeAuditSettingDialog() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Code Audit Setting");
        setMinimumSize(new Dimension(600, 200));
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        ApplicationCache.saveCodeAuditSetting(
            this.urlTextField.getText(),
            this.userNameTextField.getText(),
            this.passwordTextField.getText());
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void showDialog(CodeAuditSettingModel model) {
        CodeAuditSettingDialog dialog = new CodeAuditSettingDialog();
        dialog.urlTextField.setText(model.getUrl());
        dialog.userNameTextField.setText(model.getUserName());
        dialog.passwordTextField.setText(model.getPassword());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (screenSize.width - WIDTH) / 2;
        int h = (screenSize.height * 95 / 100 - HEIGHT) / 2;
        dialog.setLocation(w, h);
        dialog.pack();
        dialog.setVisible(true);
    }
}
