//package com.dj.tool.ui;
//
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.ui.DialogWrapper;
//import org.jetbrains.annotations.Nullable;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class AddCommentDialog extends DialogWrapper {
//
//    private JPanel south = new JPanel();
//    private JPanel center = new JPanel();
//
//    private final JLabel label = new JLabel("drop down");
//    private final JComboBox<Object> box = new JComboBox();
//    private final JLabel txtInput = new JLabel("input txt...");
//    private final JTextField field = new JTextField();
//
//    /**
//     * Creates modal {@code DialogWrapper} that can be a parent for other windows.
//     * The currently active window will be the dialog's parent.
//     *
//     * @param project parent window for the dialog will be calculated based on focused window for the
//     *                specified {@code project}. This parameter can be {@code null}. In this case parent window
//     *                will be suggested based on current focused window.
//     * @throws IllegalStateException if the dialog is invoked not on the event dispatch thread
//     * @see DialogWrapper#DialogWrapper(Project, boolean)
//     */
//    protected AddCommentDialog(@Nullable Project project, String title) {
//        super(project);
//        init();
//        setTitle(title);
//    }
//
//    /**
//     * Factory method. It creates panel with dialog options. Options panel is located at the
//     * center of the dialog's content pane. The implementation can return {@code null}
//     * value. In this case there will be no options panel.
//     */
//    @Override
//    protected @Nullable JComponent createCenterPanel() {
//        GridLayout gridLayout = new GridLayout(4, 1);
//        center.setLayout(gridLayout);
//        box.addItem("item 1");
//        box.addItem("item 2");
//    }
//}
