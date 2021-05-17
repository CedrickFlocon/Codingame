package org.neige.codingame.totoro

data class Tree(
    val cellId: Int,
    var size: Int,
    val owner: Player,
    val isDormant: Boolean,
    val nutrients: Int
) {

    companion object {
        const val MAX_SIZE = 3
    }

    lateinit var cell: Cell

    val canBeGrown: Boolean
        get() = size < MAX_SIZE

    val spookyBy: Map<Int, List<Tree>>
        get() = cell.spookyBy.map { it.key to it.value.filter { it.size >= size } }.toMap()

    val sunPoint: Map<Int, Int>
        get() = spookyBy.map { (day, trees) -> day to (size.takeIf { trees.isEmpty() } ?: 0) }.toMap()

    val spookySize: Map<Int, Int?>
        get() = cell.spookyBy.map { (day, trees) -> day to trees.maxBy { it.size }?.size?.minus(size) }.toMap()

    override fun toString(): String {
        return "Tree[$cellId]"
    }

}
