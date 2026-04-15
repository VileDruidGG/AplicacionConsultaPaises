package com.example.aplicacionconsultapaises.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.aplicacionconsultapaises.modelo.Country
import java.text.NumberFormat
import java.util.Locale

/**
 * Componente que muestra la información de un país individual
 * 
 * Muestra:
 * - Bandera del país (usando Coil para descarga asíncrona)
 * - Nombre común del país
 * - Capital
 * - Población formateada
 */
@Composable
fun CountryItem(
    country: Country,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bandera del país usando Coil
            AsyncImage(
                model = country.flags.png,
                contentDescription = "Bandera de ${country.name.common}",
                modifier = Modifier
                    .size(80.dp, 60.dp),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Información del país
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Nombre del país
                Text(
                    text = country.name.common,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Capital
                Text(
                    text = "Capital: ${country.capital?.firstOrNull() ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Población formateada
                Text(
                    text = "Población: ${formatPopulation(country.population)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Formatea la población con separadores de miles
 * Ejemplo: 128932753 -> "128,932,753"
 */
private fun formatPopulation(population: Long): String {
    return NumberFormat.getNumberInstance(Locale.US).format(population)
}
