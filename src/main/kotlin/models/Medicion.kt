package org.example.models

import java.time.LocalDate
import java.time.LocalTime

data class Medicion (
    val provincia: String,
    val cuidad: String,
    val fecha: LocalDate,
    val tempMax: Double,
    val horaTempMax: LocalTime,
    val tempMin: Double,
    val horaTempMin: LocalTime,
    val precipitacion: Double,
) {
}