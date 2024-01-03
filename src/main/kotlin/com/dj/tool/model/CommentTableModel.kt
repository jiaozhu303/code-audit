package com.dj.tool.model

import javax.swing.table.DefaultTableModel

class CommentTableModel(data: Array<Array<Any?>>, columnNames: Array<Any?>?) : DefaultTableModel(data, columnNames) {
    override fun isCellEditable(row: Int, column: Int): Boolean {
        if (column > 0 && column < 7) {
            return true
        }
        return false
    }
}
