package com.example.aplicacionconsultapaises.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionconsultapaises.modelo.Country
import com.example.aplicacionconsultapaises.modelo.CountryAPI
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * ViewModel para gestionar la lista de países
 * 
 * Responsabilidades:
 * - Crear y configurar el objeto Retrofit
 * - Descargar la lista de países de forma asíncrona
 * - Exponer los datos a través de LiveData para que la UI los observe
 * - Manejar el ciclo de vida de las corrutinas con viewModelScope
 */
class CountriesViewModel : ViewModel() {

    // LiveData privado (mutable) - solo el ViewModel puede modificarlo
    private val _countries = MutableLiveData<List<Country>>()
    
    // LiveData público (inmutable) - la UI solo puede observarlo
    val countries: LiveData<List<Country>> = _countries

    // LiveData para manejar estados de carga y errores
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /**
     * Objeto Retrofit configurado con:
     * - Base URL del API REST Countries
     * - Conversor Gson para parsear JSON a objetos Kotlin
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://restcountries.com/v3.1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * Instancia del API creada por Retrofit
     * 
     * Retrofit implementa automáticamente la interfaz CountryAPI
     * y genera el código necesario para hacer las peticiones HTTP
     */
    private val countryAPI = retrofit.create(CountryAPI::class.java)

    /**
     * Descarga la lista de países de forma asíncrona
     * 
     * Utiliza viewModelScope para:
     * - Ejecutar la corrutina en el ciclo de vida del ViewModel
     * - Cancelar automáticamente la descarga si el ViewModel es destruido
     * 
     * Actualiza el LiveData con los resultados para que la UI los observe
     */
    fun fetchCountries() {
        // Inicia una corrutina en el scope del ViewModel
        viewModelScope.launch {
            try {
                // Indica que está cargando
                _isLoading.value = true
                _error.value = null
                
                // Llama a la función suspend del API
                // La corrutina se suspende aquí hasta que la descarga termine
                val countriesList = countryAPI.getAllCountries()
                
                // Actualiza el LiveData con los países descargados
                _countries.value = countriesList
                
            } catch (e: Exception) {
                // Maneja errores de red o de parseo
                _error.value = "Error al cargar países: ${e.message}"
                _countries.value = emptyList()
                
            } finally {
                // Indica que terminó de cargar (éxito o error)
                _isLoading.value = false
            }
        }
    }
}
