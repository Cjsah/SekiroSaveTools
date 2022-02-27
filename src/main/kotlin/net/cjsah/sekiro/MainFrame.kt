package net.cjsah.sekiro

import net.cjsah.sekiro.Util.update
import java.awt.Font
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JScrollPane
import javax.swing.ListSelectionModel.SINGLE_SELECTION

class MainFrame : JFrame("只狼存档管理器") {

    companion object {
        var frame: MainFrame? = null
    }

    private val label = JLabel("", JLabel.CENTER).apply {
        this.setBounds(26, 149, 120, 27)
        this.font = Font("宋体", Font.PLAIN, 26)
        this.isVisible = false
    }

    val list = JList<String>().apply {
        this.selectionMode = SINGLE_SELECTION
        this.update()
    }

    init {
        this.setSize(300, 247)
        this.setLocationRelativeTo(null)
        this.isResizable = false
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.layout = null

        this.add(JScrollPane(list).apply {
            this.setBounds(172, 12, 100, 184)
        })

        this.add(JButton("备份").apply {
            this.setBounds(12, 12, 71, 50)
            this.addActionListener {
                TextPopDialog(this@MainFrame, "新建存档", "存档名称") {
                    Util.add(it)
                }.isVisible = true
            }
        })

        this.add(JButton("回档").apply {
            this.setBounds(89, 12, 71, 50)
            this.addActionListener {
                if (list.selectedIndex == -1) this@MainFrame.setLabel("没有存档")
                else if (Util.redo(list.selectedValue)) successLabel()
                else cancelLabel()
            }
        })

        this.add(JButton("删除").apply {
            this.setBounds(12, 68, 71, 50)
            this.addActionListener {
                if (list.selectedIndex == -1) this@MainFrame.setLabel("没有存档")
                else PopDialog(this@MainFrame, "删除存档", "确认要删除存档 \"${list.selectedValue}\" 吗") {
                    Util.remove(list.selectedValue)
                }.isVisible = true
            }
        })

        this.add(JButton("重命名").apply {
            this.setBounds(89, 68, 71, 50)
            this.addActionListener {
                if (list.selectedIndex == -1) this@MainFrame.setLabel("没有存档")
                else TextPopDialog(this@MainFrame, "重命名存档", "存档名称") {
                    Util.rename(list.selectedValue, it)
                }.isVisible = true
            }
        })

        this.add(label)

        frame = this
    }

    fun cancelLabel() {
        setLabel("操作取消")
    }

    fun successLabel() {
        setLabel("操作成功")
    }

    private fun setLabel(message: String) {
        label.text = message
        label.isVisible = true
    }

}