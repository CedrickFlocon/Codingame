package org.neige.codingame.keepofthegrass

import java.util.Stack

class Zone(
    val tiles: Set<Tile>,
    val tileNumber: Int,
    val scrapAmount: Int,
    val player: Player?,
    val robot: Int
) : Debuggable {

    lateinit var borders: Map<Zone, List<Link>>

    private var _fieldZone: List<Zone>? = null
    val fieldZone: List<Zone>
        get() {
            _fieldZone?.let { return it }

            val allZones = mutableListOf<Zone>()
            val zoneToInspect = Stack<Zone>().apply { this.push(this@Zone) }
            while (zoneToInspect.isNotEmpty()) {
                val zone = zoneToInspect.pop()
                zone._fieldZone = allZones
                allZones.add(zone)
                zone.borders.keys
                    .distinct()
                    .filter { !allZones.contains(it) && !zoneToInspect.contains(it) }
                    .forEach {
                        zoneToInspect.push(it)
                    }
            }
            return allZones
        }

    private var _outsideBorder: List<Tile>? = null
    val outsideBorder: List<Tile>
        get() {
            _outsideBorder?.let { return it }
            return borders.values.flatten().map { it.outside }.distinct()
        }

    private var _insideBorder: List<Tile>? = null
    val insideBorder: List<Tile>
        get() {
            _insideBorder?.let { return it }
            return borders.values.flatten().map { it.inside }.distinct()
        }

    private fun name() = "Zone ${if (player == null) "Neutral" else "Owned"} ($tileNumber)"

    override fun debug(): String = """
        |${name()}
        |${this.tiles}
        |${this.borders.map { "Border ${it.key.name()}\nLink(${it.value.distinctBy { it.inside }.size}) ${it.value.map { it.debug() }}" }.joinToString("\n").ifEmpty { "No Border" }}
        |Scrap: $scrapAmount | Robot: $robot
    """.trimMargin()

}

data class Link(
    val inside: Tile,
    val outside: Tile
) : Debuggable {

    override fun debug() = "$inside-$outside"

}
