package org.example.services

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.adaptivity.xmlutil.serialization.XML
import org.example.models.Informe
import org.example.models.Medicion
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime

class MeteoService {
    private val _mediciones: MutableList<Medicion> = mutableListOf<Medicion>()
    val mediciones: List<Medicion>
        get() = _mediciones.toList()

    init {
        println("Meteo Service")
        val meteoFiles = arrayOf("Aemet20171029.csv", "Aemet20171030.csv", "Aemet20171031.csv")
        meteoFiles.forEach {
            val file = ("src/main/resources/$it")
            println("Reading file: $file")
            _mediciones.addAll(readFromFile(File(file)))
        }
    }

    private fun readFromFile(meteoFile: File): List<Medicion> {
        return meteoFile.readLines(charset = Charset.forName("Windows-1252"))
            .map { line -> line.split(";") }
            .map { parts ->
                Medicion(
                    cuidad = parts[0].trim(),
                    provincia = parts[1].trim(),
                    fecha = parseLocalDate(meteoFile.name.substring(5)),
                    tempMax = parts[2].toDouble(),
                    horaTempMax = parseLocalTime(parts[3]),
                    tempMin = parts[4].toDouble(),
                    horaTempMin = parseLocalTime(parts[5]),
                    precipitacion = parts[6].toDouble()
                )
            }
    }

    private fun parseLocalDate(fileName: String): LocalDate {
        return LocalDate.of(
            fileName.substring(0, 4).toInt(),
            fileName.substring(4, 6).toInt(),
            fileName.substring(6, 8).toInt()
        )
    }

    private fun parseLocalTime(time: String): LocalTime{
        val parts = time.split(":")
        return LocalTime.of(parts[0].toInt(), parts[1].toInt())
    }

    fun exportToJson(informe: Informe){
        val json = Json { prettyPrint = true ; ignoreUnknownKeys = true }
        Files.createDirectories(Path.of("reports"))
        val fileName = "report-${LocalDate.now()}.json"
        val file = File("reports/$fileName")
        file.writeText(json.encodeToString<Informe>(informe))
    }

    fun exportToXml(informe: Informe){
        val xml = XML {indent = 4}
        Files.createDirectories(Path.of("repots"))
        val fileName = "report-${LocalDate.now()}.xml"
        val file = File("reports/$fileName")
        file.writeText(xml.encodeToString<Informe>(informe))
    }

}