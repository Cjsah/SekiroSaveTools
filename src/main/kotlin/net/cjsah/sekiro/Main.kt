package net.cjsah.sekiro

import javax.swing.UIManager

fun main() {
    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")
    Util.init()
    MainFrame().isVisible = true
}