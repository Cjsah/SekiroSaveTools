package net.cjsah.sekiro

import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class TextPopDialog(
    owner: MainFrame,
    title: String,
    message: String,
    val confirm: TextPopDialog.(String) -> Boolean
) : PopDialog(owner, title) {

    private val textField = JTextField().apply {
        this.setBounds(12, 50, 174, 21)
    }

    private val confirmBtn = JButton("确认").apply {
        this.setBounds(12, 90, 75, 25)
        this.isEnabled = false
        this.addActionListener { confirm(textField.text).also { if (it) success() else failure() } }
    }

    init {
        this.add(JLabel(message, JLabel.CENTER).apply {
            this.setBounds(12, 23, 174, 12)
        })

        this.add(textField)

        this.add(confirmBtn)

        this.add(JButton("取消").apply {
            this.setBounds(111, 90, 75, 25)
            this.addActionListener { cancel() }
        })

        textField.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent) {
                if (e.offset == 0) confirmBtn.isEnabled = true
            }

            override fun removeUpdate(e: DocumentEvent) {
                if (e.offset == 0) confirmBtn.isEnabled = false
            }

            override fun changedUpdate(e: DocumentEvent?) {}
        })

        textField.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent) {
                if (e.keyChar == '') cancel()
                else if (e.keyChar == '\n' && confirmBtn.isEnabled)
                    confirm(textField.text).also { if (it) success() else failure() }
            }

            override fun keyPressed(e: KeyEvent?) {}
            override fun keyReleased(e: KeyEvent?) {}
        })
    }
}