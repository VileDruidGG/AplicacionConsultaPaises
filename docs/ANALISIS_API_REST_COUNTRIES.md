# 📊 ANÁLISIS DEL API REST COUNTRIES v3.1

**Proyecto:** Aplicación Consulta Países  
**API:** https://restcountries.com/v3.1/all  
**Fecha:** Abril 2026  
**Estudiante:** Christofher Ontiveros Espino

---

## 🎯 PASO 1: ANÁLISIS DEL API

### Información General del API

- **URL Base:** `https://restcountries.com/v3.1/`
- **Endpoint principal:** `/all`
- **Método HTTP:** `GET`
- **Formato de respuesta:** JSON
- **Tipo de respuesta:** Array de objetos (lista de países)
- **Requiere autenticación:** NO
- **Requiere filtro de campos:** SÍ (obligatorio para `/all`)

### ⚠️ Características Importantes

1. **Filtro de campos obligatorio:** El endpoint `/all` requiere especificar los campos que se necesitan (máximo 10 campos), de lo contrario retorna un error 400 Bad Request.

2. **Sintaxis del filtro:**
   ```
   https://restcountries.com/v3.1/all?fields=name,capital,region,population,flags
   ```

3. **Ventajas del filtrado:**
   - Optimiza el tiempo de respuesta
   - Reduce el consumo de ancho de banda
   - Descarga solo la información necesaria

---

## 🔍 PASO 2: IDENTIFICACIÓN DE ATRIBUTOS NECESARIOS

### Estructura General del JSON Response

La API retorna un **Array de objetos**, donde cada objeto representa un país:

```json
[
  {
    "name": { ... },
    "capital": [ ... ],
    "region": "...",
    "population": 123456,
    "flags": { ... },
    "languages": { ... }
  },
  {
    // Siguiente país...
  }
]
```

---

## 📋 CAMPOS IDENTIFICADOS PARA EL PROYECTO

### 1️⃣ **name** (Objeto anidado)

Contiene información del nombre del país en diferentes formatos.

**Estructura:**
```json
"name": {
  "common": "Mexico",
  "official": "United Mexican States",
  "nativeName": {
    "spa": {
      "official": "Estados Unidos Mexicanos",
      "common": "México"
    }
  }
}
```

**Campo a utilizar:**
- ✅ `name.common` → Nombre común del país (ej: "Mexico", "Japan", "France")

**Justificación:** Es el nombre más reconocible para mostrar en la lista.

---

### 2️⃣ **capital** (Array de strings)

Lista de capitales del país (algunos países tienen múltiples capitales).

**Estructura:**
```json
"capital": ["Mexico City"]
// o múltiples:
"capital": ["Pretoria", "Cape Town", "Bloemfontein"]
```

**Campo a utilizar:**
- ✅ `capital[0]` → Primera capital del array

**Justificación:** Muestra la capital principal del país.

---

### 3️⃣ **region** (String)

Región geográfica a la que pertenece el país.

**Estructura:**
```json
"region": "Americas"
// Posibles valores: "Africa", "Americas", "Asia", "Europe", "Oceania", "Antarctic"
```

**Campo a utilizar:**
- ✅ `region` → Región del país

**Justificación:** Permite agrupar o filtrar países por continente.

---

### 4️⃣ **population** (Number)

Población total del país.

**Estructura:**
```json
"population": 128932753
```

**Campo a utilizar:**
- ✅ `population` → Número de habitantes

**Justificación:** Dato estadístico relevante para mostrar.

---

### 5️⃣ **flags** (Objeto anidado) ⭐ **CRÍTICO**

URLs de las banderas del país en diferentes formatos.

**Estructura:**
```json
"flags": {
  "png": "https://flagcdn.com/w320/mx.png",
  "svg": "https://flagcdn.com/mx.svg",
  "alt": "The flag of Mexico is composed of three equal vertical bands..."
}
```

**Campo a utilizar:**
- ✅ `flags.png` → URL de la imagen en formato PNG

**Justificación:** 
- PNG es compatible con Coil
- Mejor rendimiento que SVG para app móvil
- Tamaño de 320px de ancho es adecuado

---

### 6️⃣ **languages** (Objeto - Opcional)

Idiomas oficiales del país.

**Estructura:**
```json
"languages": {
  "spa": "Spanish",
  "eng": "English"
}
```

**Campo a utilizar:**
- ⚪ `languages` → Objeto completo (opcional)

**Justificación:** Información adicional que puede enriquecer la app.

---

## 🎨 RESUMEN DE CAMPOS SELECCIONADOS

### Campos Principales (Obligatorios)

| Campo | Tipo | Descripción | Uso en la App |
|-------|------|-------------|---------------|
| `name.common` | String | Nombre común del país | Título principal |
| `flags.png` | String (URL) | URL de la bandera PNG | Imagen con Coil |
| `capital` | Array[String] | Lista de capitales | Subtítulo |
| `region` | String | Continente/Región | Categorización |
| `population` | Number | Población total | Dato estadístico |

### Campos Opcionales (Para enriquecer la app)

| Campo | Tipo | Descripción | Uso en la App |
|-------|------|-------------|---------------|
| `languages` | Object | Idiomas oficiales | Detalle adicional |

---

## 🔗 ENDPOINT FINAL PARA EL PROYECTO

```
GET https://restcountries.com/v3.1/all?fields=name,capital,region,population,flags,languages
```

### Ejemplo de Respuesta (Un país):

```json
[
  {
    "name": {
      "common": "Mexico",
      "official": "United Mexican States"
    },
    "capital": ["Mexico City"],
    "region": "Americas",
    "population": 128932753,
    "flags": {
      "png": "https://flagcdn.com/w320/mx.png",
      "svg": "https://flagcdn.com/mx.svg"
    },
    "languages": {
      "spa": "Spanish"
    }
  }
]
```

---

## 🏗️ ESTRUCTURA DE CLASES DE DATOS (Próximo Paso)

Basándonos en el análisis anterior, necesitaremos crear las siguientes **data classes** en Kotlin:

### 1. Clase Principal: `Country`
```kotlin
data class Country(
    val name: Name,
    val capital: List<String>?,
    val region: String,
    val population: Long,
    val flags: Flags,
    val languages: Map<String, String>?
)
```

### 2. Clase Anidada: `Name`
```kotlin
data class Name(
    val common: String,
    val official: String
)
```

### 3. Clase Anidada: `Flags`
```kotlin
data class Flags(
    val png: String,
    val svg: String
)
```

---

## 📊 JUSTIFICACIÓN DE LA SELECCIÓN

### ✅ Por qué estos campos:

1. **name.common** - Identifica el país de manera clara
2. **flags.png** - Componente visual principal (requiere Coil)
3. **capital** - Información geográfica básica
4. **region** - Permite categorización y filtros
5. **population** - Dato estadístico relevante
6. **languages** - Enriquece la información mostrada

### ✅ Por qué NO otros campos:

- ❌ `borders` - Demasiado técnico para una app básica
- ❌ `currencies` - Objeto complejo, no prioritario
- ❌ `timezones` - No relevante para el objetivo
- ❌ `translations` - Innecesario, usamos `name.common`
- ❌ `area`, `latlng` - Datos geográficos avanzados

---

## 🎯 CUMPLIMIENTO DE LA RÚBRICA

### Criterio: "Identificación de atributos según especificación"

✅ **Atributos identificados:**
- Nombre del país (`name.common`)
- Bandera (`flags.png`) - **CRÍTICO para Coil**
- Capital (`capital`)
- Región (`region`)
- Población (`population`)
- Idiomas (`languages`)

✅ **Solo campos necesarios:** No se incluyen todos los campos del JSON, solo los relevantes.

✅ **Manejo de objetos anidados:** Se identificó correctamente que `name` y `flags` son objetos internos que requieren clases adicionales.

---

## 📝 NOTAS TÉCNICAS

### Consideraciones para Retrofit

1. **Anotación necesaria:** `@GET("all?fields=name,capital,region,population,flags,languages")`
2. **Tipo de retorno:** `suspend fun getAllCountries(): List<Country>`
3. **Base URL:** `https://restcountries.com/v3.1/`

### Consideraciones para Gson

1. **Deserialización automática:** Gson convertirá automáticamente el JSON a objetos Kotlin
2. **Nombres de campos:** Los nombres de las propiedades en Kotlin deben coincidir con el JSON
3. **Campos opcionales:** Usar tipos nullables (`?`) para campos que pueden no estar presentes

### Consideraciones para Coil

1. **URL de imagen:** `flags.png` contiene la URL completa
2. **Formato:** PNG de 320px de ancho
3. **Dominio:** `https://flagcdn.com/`

---

## ✅ CONCLUSIÓN DEL ANÁLISIS

El API REST Countries v3.1 es **ideal** para este proyecto porque:

1. ✅ **Gratuito y sin autenticación**
2. ✅ **Retorna JSON bien estructurado**
3. ✅ **Permite filtrado de campos** (optimización)
4. ✅ **Incluye URLs de banderas** (compatible con Coil)
5. ✅ **Datos completos y actualizados**
6. ✅ **Documentación clara**

**Próximos pasos:**
- ✅ Paso 1 y 2: Completados
- ⏳ Paso 6: Crear las clases de datos (Country, Name, Flags)
- ⏳ Paso 7: Definir la interfaz del API con Retrofit
- ⏳ Paso 8 en adelante...

---

**Elaborado por:** Christofher Ontiveros Espino  
**Proyecto:** AplicacionConsultaPaises  
**GitHub:** https://github.com/VileDruidGG/AplicacionConsultaPaises
