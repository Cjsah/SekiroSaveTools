package net.cjsah.sekiro

import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import javax.swing.JDialog

abstract class PopDialog(
    owner: MainFrame,
    title: String
) : JDialog(owner, title) {

    init {
        this.setSize(214, 166)
        this.setLocationRelativeTo(null)
        this.isResizable = false
        this.defaultCloseOperation = DISPOSE_ON_CLOSE
        this.layout = null

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

    protected fun cancel() {
        (owner as MainFrame).cancelLabel()
        this.dispose()
    }

    protected fun success() {
        (owner as MainFrame).successLabel()
        this.dispose()
    }

    protected fun failure() {
        (owner as MainFrame).failureLabel()
        this.dispose()
    }

}