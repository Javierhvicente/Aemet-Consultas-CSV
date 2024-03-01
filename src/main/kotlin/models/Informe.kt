package org.example.models

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime
@Serializable
data class Informe(
    val provincia: String,
    val tempMediaMaxima: Double,
    val tempMediaMinima: Double,
    val tempMaxima: Double,
    val tempMinima: Double,
    val precipitacionFechaNumLugaresMedia: List<Pair<String, Pair<Int, Double>>>
) {
}