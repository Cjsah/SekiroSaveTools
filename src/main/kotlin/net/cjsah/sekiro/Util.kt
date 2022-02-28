package net.cjsah.sekiro

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.io.File
import java.util.UUID
import javax.swing.AbstractListModel
import javax.swing.JList

object Util {
    private val sekiroPath = File("${System.getProperty("user.home")}/AppData/Roaming/Sekiro")
    private val bakPaths = File(sekiroPath, "backups")
    private val gamePath = File(sekiroPath, "76561198845150511")
    private val configPath = File(sekiroPath, "config.json")

    private val gson = GsonBuilder().setPrettyPrinting().create()

    private val maps: MutableMap<UUID, String> = LinkedHashMap()

    val backups: List<SaveData> get() = maps.map { SaveData(it.key, it.value) }

    private fun update() {
        maps.clear()
        gson.fromJson(configPath.readText(), JsonArray::class.java).forEach {
            maps[UUID.fromString(it.asJsonObject["uuid"].asString)] = it.asJsonObject["name"].asString
        }
        MainFrame.frame?.list?.update()
    }

    fun init() {
        if (!configPath.exists()) {
            configPath.createNewFile()
            configPath.writeText(gson.toJson(JsonArray()))
        }
        update()
    }

    private fun save() {
        configPath.writeText(gson.toJson(JsonArray().apply {
            maps.forEach {
                this.add(JsonObject().apply {
                    this.addProperty("name", it.value)
                    this.addProperty("uuid", it.key.toString())
                })
            }
        }))
        update()
    }

    fun add(name: String): Boolean {
        val uuid = UUID.randomUUID()
        return gamePath.copyRecursively(File(bakPaths, uuid.toString())).also {
            if (it) { maps[uuid] = name; save() }
        }
    }


    fun remove(uuid: UUID): Boolean {
        return File(bakPaths, uuid.toString()).deleteRecursively().also {
            if (it) maps.remove(uuid).also { save() }
        }
    }

    fun redo(uuid: UUID): Boolean {
        return gamePath.deleteRecursively() && File(bakPaths, uuid.toString()).copyRecursively(gamePath)
    }

    fun rename(uuid: UUID, newName: String): Boolean {
        maps[uuid] = newName
        save()
        return true
    }

    fun JList<SaveData>.update() {
        this.model = object : AbstractListModel<SaveData>() {
            override fun getSize() = backups.size
            override fun getElementAt(index: Int) = backups[index]
        }
        if (this.model.size > 0) this.selectedIndex = 0
    }

}