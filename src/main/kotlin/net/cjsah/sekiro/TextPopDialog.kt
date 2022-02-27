package net.cjsah.sekiro

import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class TextPopDialog(
    owner: MainFrame,
    title: String,
    message: String,
    val confirm: TextPopDialog.(String) -> Unit
) : JDialog(owner, title) {

    private val textField = JTextField().apply {
        this.setBounds(12, 50, 174, 21)
    }

    private val confirmBtn = JButton("确认").apply {
        this.setBounds(12, 90, 75, 25)
        this.isEnabled = false
        this.addActionListener { if (textField.text != "") confirm(textField.text).also { this@TextPopDialog.success() } }
    }

    init {
        this.setSize(214, 166)
        this.setLocationRelativeTo(null)
        this.isResizable = false
        this.defaultCloseOperation = DISPOSE_ON_CLOSE
        this.layout = null

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
                else if (e.keyChar == '\n' && confirmBtn.isEnabled && textField.text != "")
                    confirm(textField.text).also { this@TextPopDialog.success() }
            }

            override fun keyPressed(e: KeyEvent?) {}
            override fun keyReleased(e: KeyEvent?) {}
        })

        this.addWindowListener(object : WindowListener {
            override fun windowClosing(e: WindowEvent?) {
                owner.cancelLabel()
            }
            override fun windowClosed(e: WindowEvent?) {}
            override fun windowOpened(e: WindowEvent?) {}
            override fun windowIconified(e: WindowEvent?) {}
            override fun windowDeiconified(e: WindowEvent?) {}
            override fun windowActivated(e: WindowEvent?) {}
            override fun windowDeactivated(e: WindowEvent?) {}
        })

    }

    private fun cancel() {
        (owner as? MainFrame)?.cancelLabel()
        this.dispose()
    }

    private fun success() {
        (owner as? MainFrame)?.successLabel()
        this.dispose()
    }

}