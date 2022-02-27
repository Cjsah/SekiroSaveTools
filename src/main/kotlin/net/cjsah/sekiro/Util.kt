package net.cjsah.sekiro

import java.io.File
import javax.swing.AbstractListModel
import javax.swing.JList

object Util {
    private val sekiroPath = File(System.getProperty("user.home") + "/AppData/Roaming/Sekiro")
    private val bakPaths = File(sekiroPath, "backups")
    private val gamePath = File(sekiroPath, "76561198845150511")


    private lateinit var bks: List<String>

    val backups get() = bks

    fun update() {
        bks = bakPaths.listFiles()?.filter { it.isDirectory }?.map { it.name } ?: emptyList()
        MainFrame.frame?.list?.update()
    }

    fun add(name: String): Boolean {
        return if (bks.contains(name)) false
        else gamePath.copyRecursively(File(bakPaths, name)).also { update() }
    }

    fun remove(name: String) = File(bakPaths, name).deleteRecursively().also { update() }

    fun redo(name: String): Boolean {
        return if (!backups.contains(name)) false
        else gamePath.deleteRecursively() && File(bakPaths, name).copyRecursively(gamePath)
    }

    fun rename(oldName: String, newName: String): Boolean {
        return if (!backups.contains(oldName)) false
        else File(bakPaths, oldName).renameTo(File(bakPaths, newName)).also { update() }
    }

    fun JList<String>.update() {
        this.model = object : AbstractListModel<String>() {
            override fun getSize() = backups.size
            override fun getElementAt(index: Int) = backups[index]
        }
        if (this.model.size > 0) this.selectedIndex = 0
    }

}