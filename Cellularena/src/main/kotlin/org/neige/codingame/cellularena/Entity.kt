package org.neige.codingame.cellularena

sealed class Entity {
    data class RawData(
        val type: String,
        val owner: Int,
        val organId: Int,
        val organDir: String,
        val organParentId: Int,
        val organRootId: Int
    ) : Entity()

    object EMPTY : Entity()
}

