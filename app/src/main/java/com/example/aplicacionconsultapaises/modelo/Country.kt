package com.example.aplicacionconsultapaises.modelo

import com.google.gson.annotations.SerializedName

/**
 * Clase de datos que representa un país con la información
 * obtenida del API REST Countries v3.1
 *
 * @property name Nombre común del país
 * @property capital Lista de capitales (usaremos la primera)
 * @property population Población total del país
 * @property flagUrl URL de la imagen de la bandera en formato PNG
 */
data class Country(
    @SerializedName("name")
    val name: CountryName,
    
    @SerializedName("capital")
    val capital: List<String>?,
    
    @SerializedName("population")
    val population: Long,
    
    @SerializedName("flags")
    val flags: CountryFlags
)

/**
 * Clase auxiliar para extraer el nombre común del país
 * del objeto "name" del JSON
 */
data class CountryName(
    @SerializedName("common")
    val common: String
)

/**
 * Clase auxiliar para extraer la URL de la bandera en PNG
 * del objeto "flags" del JSON
 */
data class CountryFlags(
    @SerializedName("png")
    val png: String
)
