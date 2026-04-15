# 🔍 ¿Qué es @SerializedName?

## 📚 Definición

`@SerializedName` es una **anotación** (annotation) que viene de la librería **Gson** y se usa para mapear (conectar) los nombres de los campos en un JSON con los nombres de las propiedades en una clase de Kotlin.

---

## 🏗️ De dónde viene

### Librería: **Gson (Google Gson)**
- **Desarrollada por:** Google
- **Propósito:** Convertir objetos Java/Kotlin a JSON y viceversa
- **Package:** `com.google.gson.annotations.SerializedName`

### Ya la tenemos en el proyecto:
```kotlin
// En libs.versions.toml
gsonc = "2.11.0"
gsonc = { group = "com.squareup.retrofit2", name = "converter-gson", version.ref = "gsonc"}

// En build.gradle.kts
implementation(libs.gsonc)
```

---

## 🎯 ¿Para qué sirve?

### Problema que resuelve:

Cuando el **nombre del campo en el JSON** es diferente al **nombre que queremos usar en Kotlin**, necesitamos decirle a Gson cómo hacer la conexión.

---

## 📊 Ejemplos Prácticos

### Ejemplo 1: Nombres idénticos (NO necesita @SerializedName)

**JSON:**
```json
{
  "name": "Mexico",
  "population": 128932753
}
```

**Kotlin (SIN anotación):**
```kotlin
data class Country(
    val name: String,
    val population: Long
)
```

✅ **Funciona** porque los nombres coinciden exactamente: `name` → `name`, `population` → `population`

---

### Ejemplo 2: Nombres diferentes (SÍ necesita @SerializedName)

**JSON:**
```json
{
  "country_name": "Mexico",
  "total_population": 128932753
}
```

**Kotlin (CON anotación):**
```kotlin
data class Country(
    @SerializedName("country_name")
    val name: String,
    
    @SerializedName("total_population")
    val population: Long
)
```

✅ **Funciona** porque le decimos a Gson:
- El campo `"country_name"` del JSON → va a la propiedad `name` de Kotlin
- El campo `"total_population"` del JSON → va a la propiedad `population` de Kotlin

---

## 🌎 Nuestro caso: REST Countries API

### JSON que nos llega:

```json
{
  "name": {
    "common": "Mexico",
    "official": "United Mexican States"
  },
  "capital": ["Mexico City"],
  "population": 128932753,
  "flags": {
    "png": "https://flagcdn.com/w320/mx.png",
    "svg": "https://flagcdn.com/mx.svg"
  }
}
```

### Nuestras clases Kotlin:

```kotlin
data class Country(
    @SerializedName("name")        // ← Le dice a Gson: "busca el campo 'name' en el JSON"
    val name: CountryName,
    
    @SerializedName("capital")     // ← Le dice a Gson: "busca el campo 'capital' en el JSON"
    val capital: List<String>?,
    
    @SerializedName("population")  // ← Le dice a Gson: "busca el campo 'population' en el JSON"
    val population: Long,
    
    @SerializedName("flags")       // ← Le dice a Gson: "busca el campo 'flags' en el JSON"
    val flags: CountryFlags
)

data class CountryName(
    @SerializedName("common")      // ← Dentro del objeto "name", busca "common"
    val common: String
)

data class CountryFlags(
    @SerializedName("png")         // ← Dentro del objeto "flags", busca "png"
    val png: String
)
```

---

## 🔄 ¿Cómo funciona el proceso?

### Paso a paso cuando Retrofit + Gson recibe el JSON:

1. **Retrofit descarga el JSON** desde `https://restcountries.com/v3.1/all`

2. **Gson lee el JSON** y encuentra:
   ```json
   {
     "name": { ... },
     "capital": [ ... ],
     "population": 128932753,
     "flags": { ... }
   }
   ```

3. **Gson busca las anotaciones `@SerializedName`** en la clase `Country`:
   - Ve `@SerializedName("name")` → busca el campo `"name"` en el JSON
   - Ve `@SerializedName("capital")` → busca el campo `"capital"` en el JSON
   - Ve `@SerializedName("population")` → busca el campo `"population"` en el JSON
   - Ve `@SerializedName("flags")` → busca el campo `"flags"` en el JSON

4. **Gson crea el objeto Kotlin** automáticamente:
   ```kotlin
   Country(
       name = CountryName(common = "Mexico"),
       capital = listOf("Mexico City"),
       population = 128932753L,
       flags = CountryFlags(png = "https://flagcdn.com/w320/mx.png")
   )
   ```

---

## ❓ Preguntas frecuentes

### 1. ¿Es obligatorio usar @SerializedName?

**NO**, pero es una **buena práctica** porque:
- ✅ Hace el código más claro y explícito
- ✅ Protege contra cambios de nombres en refactorización
- ✅ Funciona aunque los nombres no coincidan exactamente

**Ejemplo sin @SerializedName (también funciona):**
```kotlin
data class Country(
    val name: CountryName,      // Gson busca automáticamente un campo "name"
    val capital: List<String>?, // Gson busca automáticamente un campo "capital"
    val population: Long,       // Gson busca automáticamente un campo "population"
    val flags: CountryFlags     // Gson busca automáticamente un campo "flags"
)
```

### 2. ¿Cuándo SÍ es obligatorio?

Cuando los nombres **NO coinciden**:

```json
{
  "country_flag_url": "https://..."
}
```

```kotlin
data class Country(
    @SerializedName("country_flag_url")  // ← OBLIGATORIO aquí
    val flagUrl: String
)
```

### 3. ¿Qué pasa si no lo uso y los nombres son diferentes?

Gson **NO podrá hacer el mapeo** y la propiedad quedará con valor `null` o causará un error.

---

## 🎨 Ventajas de usar @SerializedName

| Ventaja | Descripción |
|---------|-------------|
| **Claridad** | Documenta explícitamente qué campo JSON mapea a cada propiedad |
| **Flexibilidad** | Permite nombres diferentes entre JSON y Kotlin |
| **Protección** | Si renombras una propiedad en Kotlin, el mapeo sigue funcionando |
| **Convención** | Es la práctica recomendada en proyectos profesionales |

---

## 📝 Ejemplo completo en contexto

### JSON original:
```json
[
  {
    "name": {
      "common": "Mexico"
    },
    "capital": ["Mexico City"],
    "population": 128932753,
    "flags": {
      "png": "https://flagcdn.com/w320/mx.png"
    }
  }
]
```

### Kotlin con @SerializedName:
```kotlin
import com.google.gson.annotations.SerializedName

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

data class CountryName(
    @SerializedName("common")
    val common: String
)

data class CountryFlags(
    @SerializedName("png")
    val png: String
)
```

### Resultado en uso:
```kotlin
// Gson convierte automáticamente:
val countries: List<Country> = retrofit.create(CountryAPI::class.java).getAllCountries()

// Ahora podemos acceder:
val firstCountry = countries[0]
println(firstCountry.name.common)      // "Mexico"
println(firstCountry.capital?.get(0))  // "Mexico City"
println(firstCountry.population)       // 128932753
println(firstCountry.flags.png)        // "https://flagcdn.com/w320/mx.png"
```

---

## 🎓 Resumen

| Concepto | Descripción |
|----------|-------------|
| **¿Qué es?** | Anotación de Gson para mapear JSON a Kotlin |
| **¿De dónde viene?** | `com.google.gson.annotations.SerializedName` |
| **¿Cuándo usarla?** | Siempre que trabajes con JSON y Retrofit/Gson |
| **¿Es obligatoria?** | No, pero es buena práctica |
| **¿Qué hace?** | Conecta campos JSON con propiedades Kotlin |

---

**Elaborado por:** Christofher Ontiveros Espino  
**Proyecto:** AplicacionConsultaPaises  
**Contexto:** Programación Móvil - Android
