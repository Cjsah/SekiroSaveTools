package net.cjsah.sekiro

import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JPanel

class PopDialog(
    owner: MainFrame,
    title: String,
    message: String,
    val confirm: PopDialog.() -> Unit
) : JDialog(owner, title) {

    init {
        this.setSize(214, 166)
        this.setLocationRelativeTo(null)
        this.isResizable = false
        this.defaultCloseOperation = DISPOSE_ON_CLOSE
        this.layout = null

        this.add(JLabel("", JLabel.CENTER).apply {
            this.setBounds(12, 23, 174, 36)
            this.setLongText(message)
        })

        this.add(JButton("确认").apply {
            this.setBounds(12, 90, 75, 25)
            this.addActionListener { confirm().also { success() } }
        })

        this.add(JButton("取消").apply {
            this.setBounds(111, 90, 75, 25)
            this.addActionListener { cancel() }
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

    @Throws(InterruptedException::class)
    fun JLabel.setLongText(longString: String) {
        val builder = StringBuilder("<html>")
        val chars = longString.toCharArray()
        val font = this.getFontMetrics(this.font)
        var start = 0
        var len = 0
        while (start + len < longString.length) {
            while (true) {
                len++
                if (start + len > longString.length) break
                if (font.charsWidth(chars, start, len) > this.width) {
                    break
                }
            }
            builder.append(chars, start, len - 1).append("<br/>")
            start = start + len - 1
            len = 0
        }
        builder.append(chars, start, longString.length - start)
        builder.append("</html>")
        this.text = builder.toString()
    }

}