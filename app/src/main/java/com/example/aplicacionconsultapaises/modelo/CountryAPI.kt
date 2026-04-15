package com.example.aplicacionconsultapaises.modelo

import retrofit2.http.GET

/**
 * Interfaz que modela el API de REST Countries v3.1
 * 
 * Define los endpoints disponibles del servicio web para obtener
 * información de países.
 * 
 * Base URL: https://restcountries.com/v3.1/
 */
interface CountryAPI {
    
    /**
     * Obtiene la lista completa de todos los países del mundo
     * 
     * El endpoint incluye un filtro de campos para optimizar la respuesta
     * y obtener solo la información necesaria:
     * - name: Nombre del país
     * - capital: Capital(es) del país
     * - population: Población total
     * - flags: URLs de las banderas
     * 
     * @return Lista de objetos Country con la información de cada país
     */
    @GET("all?fields=name,capital,population,flags")
    suspend fun getAllCountries(): List<Country>
}
