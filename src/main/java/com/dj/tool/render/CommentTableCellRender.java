package com.dj.tool.render;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;


public class CommentTableCellRender extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        boolean cellEditable = table.isCellEditable(row, column);
        if (cellEditable) {
            this.setBackground(Color.ORANGE);
            this.setForeground(Color.BLUE);
        } else {
            this.setBackground(Color.white);
            this.setForeground(Color.BLACK);
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
