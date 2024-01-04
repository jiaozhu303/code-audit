package com.dj.tool.render

import java.awt.Color
import java.awt.Component
import javax.swing.JTable
import javax.swing.table.DefaultTableCellRenderer

class CommentTableCellRender : DefaultTableCellRenderer() {
    override fun getTableCellRendererComponent(
        table: JTable,
        value: Any,
        isSelected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ): Component {
        val cellEditable = table.isCellEditable(row, column)
        if (cellEditable) {
            this.background = Color.ORANGE
            this.foreground = Color.BLUE
        } else {
            this.background = Color.white
            this.foreground = Color.BLACK
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
    }
}
