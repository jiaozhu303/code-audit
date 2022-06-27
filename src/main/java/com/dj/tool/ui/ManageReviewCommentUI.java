package com.dj.tool.ui;

import com.dj.tool.common.ApplicationCache;
import com.dj.tool.common.CopyOperateUtil;
import com.dj.tool.common.DateTimeUtil;
import com.dj.tool.common.ExcelOperateUtil;
import com.dj.tool.common.InnerProjectCache;
import com.dj.tool.common.ProjectInstanceManager;
import com.dj.tool.model.CommentTableModel;
import com.dj.tool.model.ReviewCommentInfoModel;
import com.dj.tool.render.CommentTableCellRender;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.Icons;
import com.intellij.util.ui.TextTransferable;
import org.apache.commons.compress.utils.Lists;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dj.tool.common.Constants.*;


public class ManageReviewCommentUI {

    private static final Logger log = Logger.getInstance(ManageReviewCommentUI.class);

    private static final Object[] COLUMN_NAMES = {"ID", "Reviewer", "Comments", "Author", "Type",
        "Severity", "TriggerFactor", "ProjectName", "File", "Line", "CodeFragment", "Time"};
    private JButton clearButton;
    private JButton deleteButton;
    private JButton exportButton;
    //    private JButton importButton;
    private JTable commentTable;
    public JPanel fullPanel;
    private JButton copyButton;
    private JButton syncConfluenceButton;
    private final Project project;

    public ManageReviewCommentUI(Project project) {
        this.project = project;
    }


    public void initUI() {
        bindButtons();
        reloadTableData();
        bindTableListeners();
    }

    public void refreshTableDataShow() {
        reloadTableData();
    }

    private void reloadTableData() {
        InnerProjectCache projectCache = ProjectInstanceManager.getInstance().getProjectCache(ManageReviewCommentUI.this.project.getLocationHash());
        List<ReviewCommentInfoModel> cachedComments = projectCache.getCachedComments();
        List<Object[]> rowDataList = new ArrayList<>();
        for (ReviewCommentInfoModel model : cachedComments) {
            Object[] row = {model.getIdentifier(), model.getReviewer(), model.getComments(), model.getAuthor(), model.getType(),
                model.getSeverity(), model.getFactor(), model.getProjectName(), model.getFilePath(), model.getLineRange(), model.getContent(),
                model.getDateTime()
            };
            rowDataList.add(row);
        }
        Object[][] rowData = rowDataList.stream().toArray(Object[][]::new);
        TableModel dataModel = new CommentTableModel(rowData, COLUMN_NAMES);
        commentTable.setModel(dataModel);
        commentTable.setEnabled(true);

        // 设置指定列只能通过下拉框选择数据
        JComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.addItem(TYPE_QUESTION);
        typeComboBox.addItem(TYPE_ADVICE);
        typeComboBox.addItem(TYPE_ALLEGATIONS);
        commentTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(typeComboBox));

        JComboBox<String> severityComboBox = new ComboBox<>();
        severityComboBox.addItem(SEVERITY_WARNING);
        severityComboBox.addItem(SEVERITY_GENERAL);
        severityComboBox.addItem(SEVERITY_SERIOUS);
        commentTable.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(severityComboBox));

        JComboBox<String> factorComboBox = new ComboBox<>();
        factorComboBox.addItem(FACTOR_BASIC);
        factorComboBox.addItem(FACTOR_BUSINESS);
        factorComboBox.addItem(FACTOR_SECURITY);
        TableColumn column = commentTable.getColumnModel().getColumn(6);
        column.setCellEditor(new DefaultCellEditor(factorComboBox));


        commentTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                log.info("table changed...");
                int row = e.getFirstRow();
                Long identifier = (Long) commentTable.getValueAt(row, 0);
                String reviewer = (String) commentTable.getValueAt(row, 1);
                String comments = (String) commentTable.getValueAt(row, 2);
                String author = (String) commentTable.getValueAt(row, 3);
                String type = (String) commentTable.getValueAt(row, 4);
                String severity = (String) commentTable.getValueAt(row, 5);
                String factor = (String) commentTable.getValueAt(row, 6);
                ReviewCommentInfoModel model = new ReviewCommentInfoModel();
                model.setIdentifier(identifier);
                model.setReviewer(reviewer);
                model.setComments(comments);
                model.setAuthor(author);
                model.setType(type);
                model.setSeverity(severity);
                model.setFactor(factor);
                InnerProjectCache projectCache = ProjectInstanceManager.getInstance().getProjectCache(ManageReviewCommentUI.this.project.getLocationHash());
                projectCache.updateCommonColumnContent(model);
            }
        });
    }

    private void bindTableListeners() {
        // 指定可编辑列颜色变更
        commentTable.setDefaultRenderer(Object.class, new CommentTableCellRender());

        // 双击跳转到源码位置
        commentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 2) {
//                    int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint());
//                    int column = ((JTable) e.getSource()).columnAtPoint(e.getPoint());
//                    if (!commentTable.isCellEditable(row, column)) {
//                        doubleClickDumpToOriginal(ManageReviewCommentUI.this.project, row, column);
//                        return;
//                    }
//                }
                // 其它场景，默认的处理方法
                super.mouseClicked(e);
            }
        });
    }

//    private void doubleClickDumpToOriginal(Project project, int row, int column) {
//        String filePath = (String) commentTable.getValueAt(row, 8);
//        String line = (String) commentTable.getValueAt(row, 9);
//        int startLine = 0;
//        try {
//            if (filePath == null || line == null) {
//                throw new Exception("filePath or line is null");
//            }
//
//            String[] lines = line.split("~");
//            if (lines.length != 2) {
//                throw new Exception("line format illegal");
//            }
//
//            startLine = Integer.parseInt(lines[0].trim()) - 1;
//            if (startLine < 0) {
//                startLine = 0;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Messages.showErrorDialog("open failed! Cause:" + System.lineSeparator() + e.getMessage(), "Open Failed");
//            return;
//        }
//
//        PsiFile[] filesByName = PsiShortNamesCache.getInstance(project).getFilesByName(filePath);
//        if (filesByName.length > 0) {
//            PsiFile psiFile = filesByName[0];
//            VirtualFile virtualFile = psiFile.getVirtualFile();
//            // 打开对应的文件
//            OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, virtualFile);
//            Editor editor = FileEditorManager.getInstance(project).openTextEditor(openFileDescriptor, true);
//            if (editor == null) {
//                Messages.showErrorDialog("open failed! Cause:" + System.lineSeparator() + "editor is null", "Open Failed");
//                return;
//            }
//
//            // 跳转到指定的位置
//            CaretModel caretModel = editor.getCaretModel();
//            LogicalPosition logicalPosition = caretModel.getLogicalPosition();
//            logicalPosition.leanForward(true);
//            LogicalPosition logical = new LogicalPosition(startLine, logicalPosition.column);
//            caretModel.moveToLogicalPosition(logical);
//            SelectionModel selectionModel = editor.getSelectionModel();
//            selectionModel.selectLineAtCaret();
//        } else {
//            Messages.showErrorDialog("open failed! Cause:" + System.lineSeparator() + "not found file in current project", "Open Failed");
//        }
//
//    }

    private void bindButtons() {

        copyButton.addActionListener(e -> {
            try {
                InnerProjectCache projectCache = ProjectInstanceManager.getInstance().getProjectCache(ManageReviewCommentUI.this.project.getLocationHash());
                String copyData = CopyOperateUtil.copyToString(projectCache.getCachedComments());
                Messages.showMessageDialog("Copy successfully!", "Copy Finished", Icons.EXPORT_ICON);
                CopyPasteManager.getInstance()
                    .setContents(new TextTransferable(copyData));
            } catch (Exception ex) {
                Messages.showErrorDialog("Copy failed! Cause:" + System.lineSeparator() + ex.getMessage(), "Copy Failed");
            }

        });

        clearButton.addActionListener(e -> {

            if (new ClearConfirmDialog().showAndGet()) {
                InnerProjectCache projectCache = ProjectInstanceManager.getInstance().getProjectCache(ManageReviewCommentUI.this.project.getLocationHash());
                List<Long> clearCommentIdList = projectCache.clearComments();
                reloadTableData();
                ApplicationCache.deleteCacheList(clearCommentIdList);
            }

        });

        syncConfluenceButton.addActionListener(e -> {
            System.out.println(Optional.ofNullable(ApplicationCache.getAllDataList())
                .orElseGet(Lists::newArrayList)
                .stream()
                .filter(Objects::nonNull)
                .map(ReviewCommentInfoModel::toCopyString)
                .collect(Collectors.toList())
            );
        });

//        importButton.addActionListener(e -> {
//
//            List<ReviewCommentInfoModel> reviewCommentInfoModels = null;
//            try {
//                JFileChooser fileChooser = new JFileChooser();
//                int saveDialog = fileChooser.showOpenDialog(fullPanel);
//                if (saveDialog == JFileChooser.APPROVE_OPTION) {
//                    String importPath = fileChooser.getSelectedFile().getPath();
//
//                    reviewCommentInfoModels = ExcelOperateUtil.importExcel(importPath);
//                    InnerProjectCache projectCache = ProjectInstanceManager.getInstance().getProjectCache(ManageReviewCommentUI.this.project.getLocationHash());
//                    projectCache.importComments(reviewCommentInfoModels);
//                    CommonUtil.reloadCommentListShow(ManageReviewCommentUI.this.project);
//                    Messages.showMessageDialog("Import successfully!", "Import Finished", Icons.IMPORT_ICON);
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                Messages.showErrorDialog("import failed! Cause:" + System.lineSeparator() + ex.getMessage(), "Export Failed");
//            }
//        });


        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("[" + this.project.getName() + "]_code_review_report_" + DateTimeUtil.getFormattedTimeForFileName()));
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel表格(*.xlsx)", ".xlsx"));
            int saveDialog = fileChooser.showSaveDialog(fullPanel);
            if (saveDialog == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getPath();
                if (!path.toLowerCase().endsWith(".xlsx")) {
                    path += ".xlsx";
                }

                try {
                    InnerProjectCache projectCache = ProjectInstanceManager.getInstance().getProjectCache(ManageReviewCommentUI.this.project.getLocationHash());
                    ExcelOperateUtil.exportExcel(path, projectCache.getCachedComments());
                    Messages.showMessageDialog("Export successfully!", "Export Finished", Icons.EXPORT_ICON);
                } catch (Exception ex) {
                    Messages.showErrorDialog("export failed! Cause:" + System.lineSeparator() + ex.getMessage(), "Export Failed");
                }

            }

        });

        deleteButton.addActionListener(e -> {
            if (new DeleteConfirmDialog().showAndGet()) {
                List<Long> deleteIndentifierList = new ArrayList<>();
                int[] selectedRows = commentTable.getSelectedRows();
                if (selectedRows != null && selectedRows.length > 0) {
                    for (int rowId : selectedRows) {
                        Long valueAt = (Long) commentTable.getValueAt(rowId, 0);
                        deleteIndentifierList.add(valueAt);
                    }
                    InnerProjectCache projectCache = ProjectInstanceManager.getInstance().getProjectCache(ManageReviewCommentUI.this.project.getLocationHash());
                    projectCache.deleteComments(deleteIndentifierList);
                    ApplicationCache.deleteCacheList(deleteIndentifierList);
                }
                reloadTableData();
            }
        });
    }

}
