package net.cjsah.sekiro

import java.util.UUID

data class SaveData(val uuid: UUID, val name: String) {
    override fun toString() = name
}
