package com.dj.tool.ui;


import com.dj.tool.common.ApplicationCache;
import com.dj.tool.common.CommonUtil;
import com.dj.tool.common.InnerProjectCache;
import com.dj.tool.common.ProjectInstanceManager;
import com.dj.tool.model.ReviewCommentInfoModel;
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
    private String projectName;
    private JTextField authorTextField;

    public static void showDialog(ReviewCommentInfoModel model, Project project) {

        JDialog dialog = new JDialog(new JFrame());
        dialog.setTitle("Add Code Audit Record");
        AddReviewCommentUI reviewCommentUI = new AddReviewCommentUI();
        reviewCommentUI.reviewerTextField.setText(model.getReviewer());
        reviewCommentUI.commentsTextArea.setText(model.getComments());
        reviewCommentUI.codeContentsTextArea.setText(model.getContent());
        reviewCommentUI.filePathTextField.setText(model.getFilePath());
        reviewCommentUI.lineTextField.setText(model.getLineRange());
        reviewCommentUI.authorTextField.setText(model.getAuthor());
        reviewCommentUI.questionTypeComboBox.setSelectedItem(model.getType());
        reviewCommentUI.severityComboBox.setSelectedItem(model.getSeverity());
        reviewCommentUI.triggerFactorComboBox.setSelectedItem(model.getFactor());
        reviewCommentUI.saveButton.addActionListener(e -> {
            model.setContent(reviewCommentUI.codeContentsTextArea.getText());
            model.setComments(reviewCommentUI.commentsTextArea.getText());
            model.setReviewer(reviewCommentUI.reviewerTextField.getText());
            model.setAuthor(reviewCommentUI.authorTextField.getText());
            model.setType(reviewCommentUI.questionTypeComboBox.getSelectedItem().toString());
            model.setSeverity(reviewCommentUI.severityComboBox.getSelectedItem().toString());
            model.setFactor(reviewCommentUI.triggerFactorComboBox.getSelectedItem().toString());
            InnerProjectCache projectCache = ProjectInstanceManager.getInstance().getProjectCache(project.getLocationHash());
            projectCache.addNewComment(model);
            CommonUtil.reloadCommentListShow(project);
            ApplicationCache.addOneToCache(model);
            dialog.dispose();
        });

        reviewCommentUI.cancelButton.addActionListener(e -> {
            dialog.dispose();
        });

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (screenSize.width - WIDTH) / 2;
        int h = (screenSize.height * 95 / 100 - HEIGHT) / 2;
        dialog.setLocation(w, h);

        dialog.setContentPane(reviewCommentUI.addReviewCommentPanel);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setVisible(true);
    }
}
