package com.dj.tool.ui;

import com.dj.tool.common.ApplicationCache;
import com.dj.tool.common.CommonUtil;
import com.dj.tool.common.ExcelOperateUtil;
import com.dj.tool.common.HttpRequestFactory;
import com.dj.tool.model.CodeAuditSettingModel;
import com.dj.tool.model.CommentTableModel;
import com.dj.tool.model.ReviewCommentInfoModel;
import com.dj.tool.publisher.DateRefreshMessagePublisher;
import com.dj.tool.render.CommentTableCellRender;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.util.ui.TextTransferable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

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
import java.util.Collection;
import java.util.List;

import static com.dj.tool.common.ApplicationCache.getCodeAuditSetting;
import static com.dj.tool.common.CommonUtil.buildConfluenceFormatString;
import static com.dj.tool.common.CommonUtil.getFormattedTimeForTitle;
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
        this.tableData = ApplicationCache.getProjectAllData(project.getName());
    }


    public void initUI() {
        bindButtons();
        reloadTableData();
        bindTableListeners();
    }

    public void reloadTableData() {
        this.tableData = ApplicationCache.getProjectAllData(project.getName());
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
                ApplicationCache.updateProjectData(model);
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
            final Project copyProject = this.project;
            if (CollectionUtils.isEmpty(this.tableData)) {
                CodeAuditNotifier.notifyWarning(copyProject, "Has no record to copy");
                return;
            }
            try {
                String copyData = CommonUtil.copyToString(this.tableData);
                CodeAuditNotifier.notifyInfo(copyProject, "Copy successfully!");
                CopyPasteManager.getInstance()
                        .setContents(new TextTransferable(copyData));
            } catch (Exception ex) {
                CodeAuditNotifier.notifyWarning(copyProject, "Copy failed! Cause:" + System.lineSeparator() + ex.getMessage());
            }

        });

        clearButton.addActionListener(e -> {
            final Project cleanProject = this.project;
            if (CollectionUtils.isEmpty(this.tableData)) {
                CodeAuditNotifier.notifyWarning(cleanProject, "Has no record to clean");
                return;
            }
            if (new ClearConfirmDialog().showAndGet()) {
                ApplicationCache.cleanAllCache(cleanProject.getName());
                DateRefreshMessagePublisher.getInstance(cleanProject).fireDateRefreshExecute("clean code record", cleanProject);
            }
        });

        syncConfluenceButton.addActionListener(e -> {
            final Project syncProject = this.project;
            if (CollectionUtils.isEmpty(this.tableData)) {
                CodeAuditNotifier.notifyWarning(syncProject, "Has no record to sync");
                return;
            }
            CodeAuditSettingModel codeAuditSetting = getCodeAuditSetting();
            boolean valid = codeAuditSetting.isValid();
            if (!valid) {
                CodeAuditNotifier.notifyWarning(syncProject, "Please setting confluence info!");
                return;
            }
            String projectName = syncProject.getName();
            try {
                Collection<ReviewCommentInfoModel> allDataList = ApplicationCache.getAllDataList(projectName);
                String data = buildConfluenceFormatString(allDataList);
                if (StringUtils.isBlank(data)) {
                    CodeAuditNotifier.notifyWarning(syncProject, "There is no record need to sync!");
                    return;
                }
                HttpRequestFactory.sendDataToConf(codeAuditSetting.getUrl(), codeAuditSetting.getUserName(), codeAuditSetting.getPassword(),
                        getFormattedTimeForTitle(projectName), codeAuditSetting.getSpaceKey(), codeAuditSetting.getParentId(),
                        data, successMessage -> {
                            CodeAuditNotifier.notifyInfo(syncProject, successMessage);
                        },
                        failMessage -> {
                            CodeAuditNotifier.notifyInfo(syncProject, failMessage);
                        });

            } catch (Exception ex) {
                CodeAuditNotifier.notifyError(syncProject, "Sync to confluence fail!");
                throw new RuntimeException(ex);
            }

        });

        exportButton.addActionListener(e -> {
            final Project exportProject = this.project;
            if (CollectionUtils.isEmpty(this.tableData)) {
                CodeAuditNotifier.notifyWarning(exportProject, "Has no record to export");
                return;
            }
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
                    CodeAuditNotifier.notifyInfo(exportProject, "Export successfully!");
                } catch (Exception ex) {
                    CodeAuditNotifier.notifyError(exportProject, "Export failed! Cause:" + System.lineSeparator() + ex.getMessage());
                }

            }

        });

        deleteButton.addActionListener(e -> {
            final Project deleteButtonProject = this.project;
            int[] selectedRows = commentTable.getSelectedRows();
            if (selectedRows.length <= 0) {
                CodeAuditNotifier.notifyWarning(deleteButtonProject, "Please select item first!");
                return;
            }
            if (new DeleteConfirmDialog().showAndGet()) {
                List<Long> deleteIndentifierList = new ArrayList<>();
                if (selectedRows != null && selectedRows.length > 0) {
                    for (int rowId : selectedRows) {
                        Long valueAt = (Long) commentTable.getValueAt(rowId, 0);
                        deleteIndentifierList.add(valueAt);
                    }
                    ApplicationCache.deleteCacheList(deleteIndentifierList);
                }
                DateRefreshMessagePublisher.getInstance(deleteButtonProject).fireDateRefreshExecute("refresh data list", deleteButtonProject);

            }
        });
    }

}
