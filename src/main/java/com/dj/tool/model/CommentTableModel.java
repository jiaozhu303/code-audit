package com.dj.tool.model;

import javax.swing.table.DefaultTableModel;


public class CommentTableModel extends DefaultTableModel {

    public CommentTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column > 0 && column < 7) {
            return true;
        }
        return false;
    }


}
