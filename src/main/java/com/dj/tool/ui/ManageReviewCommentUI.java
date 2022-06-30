package com.dj.tool.ui;

import com.dj.tool.common.ApplicationCache;
import com.dj.tool.common.CommonUtil;
import com.dj.tool.common.ExcelOperateUtil;
import com.dj.tool.common.HttpRequestFactory;
import com.dj.tool.common.ProjectCache;
import com.dj.tool.model.CommentTableModel;
import com.dj.tool.model.ReviewCommentInfoModel;
import com.dj.tool.render.CommentTableCellRender;
import com.dj.tool.service.CodeAuditSettingProjectService;
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
    private JTable commentTable;
    public JPanel fullPanel;
    private JButton copyButton;
    private JButton syncConfluenceButton;
    private final Project project;

    private List<ReviewCommentInfoModel> tableData;

    public ManageReviewCommentUI(Project project) {
        this.project = project;
        CodeAuditSettingProjectService projectCache = ProjectCache.getInstance(project);
        this.tableData = projectCache.getProjectAllData();
    }


    public void initUI() {
        bindButtons();
        reloadTableData();
        bindTableListeners();
    }

    public void reloadTableData() {
        CodeAuditSettingProjectService projectCache = ProjectCache.getInstance(project);
        this.tableData = projectCache.getProjectAllData();
        List<Object[]> rowDataList = new ArrayList<>();
        for (ReviewCommentInfoModel model : this.tableData) {
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
                CodeAuditSettingProjectService projectCache = ProjectCache.getInstance(project);
                projectCache.updateProjectData(model);
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
                super.mouseClicked(e);
            }
        });
    }

    private void bindButtons() {

        copyButton.addActionListener(e -> {
            try {
                String copyData = CommonUtil.copyToString(this.tableData);
                Messages.showMessageDialog("Copy successfully!", "Copy Finished", Icons.EXPORT_ICON);
                CopyPasteManager.getInstance()
                    .setContents(new TextTransferable(copyData));
            } catch (Exception ex) {
                Messages.showErrorDialog("Copy failed! Cause:" + System.lineSeparator() + ex.getMessage(), "Copy Failed");
            }

        });

        clearButton.addActionListener(e -> {

            if (new ClearConfirmDialog().showAndGet()) {
                List<Long> clearCommentIdList = Optional.ofNullable(this.tableData).orElseGet(Lists::newArrayList)
                    .stream()
                    .map(ReviewCommentInfoModel::getIdentifier)
                    .collect(Collectors.toList());
                CodeAuditSettingProjectService projectCache = ProjectCache.getInstance(project);
                projectCache.cleanAllData();
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
            try {//"<form><input type='checkbox'>test-01<br/><input type='checkbox'>test-02<br/><input type='checkbox'>test-03<br/></form>"
                HttpRequestFactory.sendDataToConf("zhaodj5", "daDI@lenovo", "cr-01", "PPC", "172484093",
                    "<h1>test context</h1>");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("[" + this.project.getName() + "]_code_review_report_" + CommonUtil.getFormattedTimeForFileName()));
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel表格(*.xlsx)", ".xlsx"));
            int saveDialog = fileChooser.showSaveDialog(fullPanel);
            if (saveDialog == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getPath();
                if (!path.toLowerCase().endsWith(".xlsx")) {
                    path += ".xlsx";
                }

                try {
                    ExcelOperateUtil.exportExcel(path, this.tableData);
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

                    CodeAuditSettingProjectService projectCache = ProjectCache.getInstance(project);
                    projectCache.deleteComments(deleteIndentifierList);
                    ApplicationCache.deleteCacheList(deleteIndentifierList);
                }
                reloadTableData();
            }
        });
    }

}
