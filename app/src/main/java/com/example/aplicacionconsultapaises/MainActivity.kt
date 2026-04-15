package com.example.aplicacionconsultapaises

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.aplicacionconsultapaises.ui.screens.CountriesScreen
import com.example.aplicacionconsultapaises.ui.theme.AplicacionConsultaPaisesTheme

/**
 * Activity principal de la aplicación
 * 
 * Configura:
 * - Jetpack Compose como sistema de UI
 * - Tema de la aplicación
 * - Pantalla principal (CountriesScreen)
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplicacionConsultaPaisesTheme {
                CountriesScreen()
            }
        }
    }
}
