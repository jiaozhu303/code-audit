package com.dj.tool.ui;


import com.dj.tool.common.ApplicationCache;
import com.dj.tool.common.ProjectCache;
import com.dj.tool.common.ReviewManagerFactory;
import com.dj.tool.model.ReviewCommentInfoModel;
import com.dj.tool.service.CodeAuditSettingProjectService;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;


public class AddReviewCommentUI {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    private JTextField reviewerTextField;
    private JTextArea commentsTextArea;
    private JComboBox questionTypeComboBox;
    private JComboBox severityComboBox;
    private JComboBox triggerFactorComboBox;
    private JButton saveButton;
    private JButton cancelButton;
    private JPanel addReviewCommentPanel;
    private JTextField filePathTextField;
    private JTextArea codeContentsTextArea;
    private JTextField lineTextField;
    private JTextField authorTextField;

    public static void showDialog(ReviewCommentInfoModel model, Project project) {

        JDialog dialog = new JDialog(new JFrame());
        dialog.setTitle("Add Code Audit Record");
        AddReviewCommentUI addComment = new AddReviewCommentUI();
        addComment.reviewerTextField.setText(model.getReviewer());
        addComment.commentsTextArea.setText(model.getComments());
        addComment.codeContentsTextArea.setText(model.getContent());
        addComment.filePathTextField.setText(model.getFilePath());
        addComment.lineTextField.setText(model.getLineRange());
        addComment.authorTextField.setText(model.getAuthor());
        addComment.questionTypeComboBox.setSelectedItem(model.getType());
        addComment.severityComboBox.setSelectedItem(model.getSeverity());
        addComment.triggerFactorComboBox.setSelectedItem(model.getFactor());
        addComment.saveButton.addActionListener(e -> {
            model.setContent(addComment.codeContentsTextArea.getText());
            model.setComments(addComment.commentsTextArea.getText());
            model.setReviewer(addComment.reviewerTextField.getText());
            model.setAuthor(addComment.authorTextField.getText());
            model.setType(addComment.questionTypeComboBox.getSelectedItem().toString());
            model.setSeverity(addComment.severityComboBox.getSelectedItem().toString());
            model.setFactor(addComment.triggerFactorComboBox.getSelectedItem().toString());
            CodeAuditSettingProjectService projectCache = ProjectCache.getInstance(project);
            projectCache.addProjectData(model);
            ReviewManagerFactory.getInstance(project).reloadTableData();
            ApplicationCache.addOneToCache(model);
            dialog.dispose();
        });

        addComment.cancelButton.addActionListener(e -> {
            dialog.dispose();
        });

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (screenSize.width - WIDTH) / 2;
        int h = (screenSize.height * 95 / 100 - HEIGHT) / 2;
        dialog.setLocation(w, h);

        dialog.setContentPane(addComment.addReviewCommentPanel);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setVisible(true);
    }
}
