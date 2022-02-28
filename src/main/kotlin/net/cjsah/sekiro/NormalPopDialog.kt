package net.cjsah.sekiro

import javax.swing.JButton
import javax.swing.JLabel

class NormalPopDialog(
    owner: MainFrame,
    title: String,
    message: String,
    val confirm: NormalPopDialog.() -> Boolean
) : PopDialog(owner, title) {

    init {
        this.add(JLabel("", JLabel.CENTER).apply {
            this.setBounds(12, 23, 174, 36)
            this.setLongText(message)
        })

        this.add(JButton("确认").apply {
            this.setBounds(12, 90, 75, 25)
            this.addActionListener { confirm().also { if (it) success() else failure() } }
        })

        this.add(JButton("取消").apply {
            this.setBounds(111, 90, 75, 25)
            this.addActionListener { cancel() }
        })
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