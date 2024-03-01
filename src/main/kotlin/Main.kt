package org.example

import org.example.models.Informe
import org.example.services.MeteoService
import java.time.LocalTime

fun main() {
    println("Hello World!")
    val service = MeteoService()
    println("Temperatura máxima por día y lugar")
    service.mediciones.groupBy { it.fecha }
        .forEach{ (fecha, mediciones) ->
            println("Temperatura máxima el $fecha")
            mediciones.groupBy { it.cuidad }
                .forEach { (cuidad, mediciones) ->
                    val max = mediciones.maxOf { it.tempMax }
                    println("En $cuidad: $max")
                }
        }

    println("Temperatura mínima por dia y lugar")
    service.mediciones.groupBy { it.fecha }
        .forEach { (fecha, mediciones) ->
            println("Temperatura minima el $fecha ")
            mediciones.groupBy { it.cuidad }
                .forEach { (cuidad, mediciones) ->
                    val min = mediciones.minOf { it.tempMin }
                    println("En $cuidad: $min")
                }
        }

    println()
    println("Temperatura máxima por provincia (día, lugar, valor y momento) ")
    service.mediciones.groupBy { it.provincia }
        .forEach { (provincia, mediciones) ->
            println("Temperatura máxima en $provincia")
            mediciones.groupBy { it.cuidad }
                .forEach { (cuidad, mediciones) ->
                    println("Cuidad: $cuidad")
                    mediciones.groupBy { it.fecha }
                        .forEach { (fecha, mediciones) ->
                            mediciones.groupBy { it.horaTempMax }.forEach {
                                    (horaTempMax, mediciones) ->
                                val max = mediciones.maxOf { it.tempMax }
                                println("El $fecha, a las $horaTempMax, temperatura: $max")
                            }
                        }
                }
        }

    println()
    println("Temperatura mínima por provincia (día, lugar, valor")
    service.mediciones.groupBy { it.provincia }
        .forEach { (provincia, mediciones) ->
            println("Temperatura mínima en $provincia")
            mediciones.groupBy { it.cuidad }
                .forEach { (ciudad, mediciones) ->
                    println("Ciudad: $ciudad")
                    mediciones.groupBy { it.fecha }
                        .forEach { (fecha, mediciones)  ->
                            val min = mediciones.minOf { it.tempMin }
                            println("El $fecha, temperatura: $min")
                        }
                }
        }

    println()
    println("Temperatura media por provincia (día, lugar, valor")
    service.mediciones.groupBy { it.provincia }
        .forEach { (provincia, mediciones) ->
            println("Temperatura media en $provincia")
            mediciones.groupBy { it.fecha }
                .forEach { (fecha, mediciones) ->
                    println("El $fecha")
                    val maxMedia = mediciones.map { it.tempMax }.average()
                    val minMedia = mediciones.map { it.tempMin }.average()
                    val media = ((maxMedia + minMedia) / 2 ).aproximar()
                    println("Media de temperatura: $media")
                }
        }

    println()
    println("Listado de precipitación media por día y por provincia")
    service.mediciones.groupBy { it.provincia }.forEach { (provincia, mediciones) ->
        println("En la provincia de $provincia")
            service.mediciones.groupBy { it.fecha }.forEach { (fecha, mediciones) ->
                val mediaPrecipitacion = service.mediciones.map { it.precipitacion }.average().aproximar()
                println("El $fecha, con una media de precipiatcion de $mediaPrecipitacion")
            }
        }

    println()
    println("Número de lugares en el que llovió por día y provincia")
    service.mediciones.groupBy { it.provincia }.forEach{(provincia, mediciones) ->
        service.mediciones.groupBy { it.fecha }.forEach { (fecha, mediciones) ->
            val numeroLugares = service.mediciones.count{it.precipitacion > 0}
            println("Numero de lugares donde ha llovido en $provincia, el $fecha, Número: $numeroLugares")
        }
    }

    println()
    println("Temperatura media de la provincia de madrid")
    service.mediciones.filter { it.provincia == "Madrid" }.groupBy { it.fecha }.forEach { (fecha, mediciones) ->
        val mediaMax = service.mediciones.map { it.tempMax }.average().aproximar()
        val mediaMin = service.mediciones.map { it.tempMin }.average().aproximar()
        println("Tempertura Media el $fecha con Máxima: $mediaMax, Mínima: $mediaMin en Madrid")
    }

    println()
    println("Media de la temperatura máxima total")
    val maxTempMedia = service.mediciones.map { it.tempMax }.average().aproximar()
    println("Temperatura máxima media: $maxTempMedia")

    println()
    println("Media de la temperatura mínima total")
    val minTempMedia = service.mediciones.map { it.tempMin }.average().aproximar()
    println("Temperatura mínima media: $minTempMedia")

    println()
    println("Lugares donde la máxima ha sido antes de las 15:00 por día")
    service.mediciones.groupBy { it.fecha }.forEach { (fecha, mediciones) ->
        println("Lugares donde la máxima ha sido antes de las 15:00 el día $fecha")
        service.mediciones.filter { it.horaTempMax.isBefore(LocalTime.of(15, 0)) }
            .groupBy { it.cuidad }.forEach { (cuidad, mediciones) ->
                println("En $cuidad, ${mediciones.first().horaTempMax}")
            }
    }

    println()
    println("Lugares donde la mínima ha sido después de las 17:00 por día")
    service.mediciones.groupBy { it.fecha }.forEach { (fecha, mediciones) ->
        println("Lugares donde la mínima ha sido después de las 17:00 el $fecha")
        service.mediciones.filter { it.horaTempMin.isAfter(LocalTime.of(17,0)) }
            .groupBy { it.cuidad }.forEach { (cuidad, mediciones) ->
                println("En $cuidad, ${mediciones.first().horaTempMin}")
            }
    }

    println()
    val temperaturaMaximaMadrid = service.mediciones.filter { it.provincia == "Madrid" }.maxByOrNull { it.tempMax }
    println("Temperatura máxima de Madrid: $temperaturaMaximaMadrid")

    val temperaturaMinimaMadrid = service.mediciones.filter { it.provincia == "Madrid" }.maxByOrNull { it.tempMin }
    println("Temperatura mínima de Madrid: $temperaturaMinimaMadrid")

    val temperaturaMaximaMediaMadrid = service.mediciones.filter { it.provincia == "Madrid" }.map { it.tempMax }.average()
    println("Temperatura máxima media de Madrid: $temperaturaMaximaMediaMadrid")

    val temperaturaMinimaMediaMadrid = service.mediciones.filter { it.provincia == "Madrid" }.map { it.tempMin }.average()
    println("Temperatura mínima media de Madrid: $temperaturaMinimaMediaMadrid")

    val precipitacionMadrid = service.mediciones.filter { it.provincia == "Madrid" && it.precipitacion > 0 }.groupBy { it.fecha }.map { (fecha, mediciones) ->
        fecha.toString() to Pair(
            mediciones.count(),
            mediciones.map { it.precipitacion }.average().aproximar()
        )
    }
    println("Días de precipitación en madrid $precipitacionMadrid")

    val informe = Informe(
        provincia = "Madrid",
        tempMaxima = temperaturaMaximaMadrid?.tempMax ?: 0.0,
        tempMinima = temperaturaMinimaMadrid?.tempMin ?: 0.0,
        tempMediaMaxima = temperaturaMaximaMediaMadrid,
        tempMediaMinima = temperaturaMinimaMediaMadrid,
        precipitacionFechaNumLugaresMedia = precipitacionMadrid
    )
    service.exportToJson(informe)
    service.exportToXml(informe)
}


fun Double.aproximar(): Double{
    return (this * 100.0).toInt() / 100.0
}
