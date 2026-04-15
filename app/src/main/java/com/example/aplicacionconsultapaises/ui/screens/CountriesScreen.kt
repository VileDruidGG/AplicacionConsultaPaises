package com.example.aplicacionconsultapaises.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacionconsultapaises.modelo.Country
import com.example.aplicacionconsultapaises.viewmodel.CountriesViewModel

/**
 * Pantalla principal que muestra la lista de países
 * 
 * Implementa:
 * - LazyColumn para lista eficiente
 * - Observación de LiveData del ViewModel
 * - Estados de carga y error
 * - Componente CountryItem para cada país
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountriesScreen(
    viewModel: CountriesViewModel = viewModel()
) {
    // Observar los LiveData del ViewModel
    val countries by viewModel.countries.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()

    // Ejecutar la descarga al iniciar la pantalla
    LaunchedEffect(Unit) {
        viewModel.fetchCountries()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Países del Mundo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                // Estado de carga
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                
                // Estado de error
                error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = error ?: "Error desconocido",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(onClick = { viewModel.fetchCountries() }) {
                            Text("Reintentar")
                        }
                    }
                }
                
                // Estado de lista vacía
                countries.isEmpty() -> {
                    Text(
                        text = "No hay países disponibles",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                // Estado de éxito - mostrar lista
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(countries) { country ->
                            CountryItem(country = country)
                        }
                    }
                }
            }
        }
    }
}
