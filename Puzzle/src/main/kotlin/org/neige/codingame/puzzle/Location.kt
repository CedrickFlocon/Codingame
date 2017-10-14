package org.neige.codingame.puzzle

data class Location(val longitude: Double, val latitude: Double) {

    private val longitudeInRadians
        get() = Math.toRadians(longitude)
    private val latitudeInRadians
        get() = Math.toRadians(latitude)

    fun distanceFrom(location: Location): Double {
        val x = (location.longitudeInRadians - longitudeInRadians) * Math.cos((latitudeInRadians + location.latitudeInRadians) / 2)
        val y = location.latitudeInRadians - latitudeInRadians

        return Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0)) * 6371
    }

}