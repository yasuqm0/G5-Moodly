# Moodly — Estructura del Proyecto Android Studio

## Árbol de carpetas

```
Moodly/
├── app/
│   ├── build.gradle.kts                          ← dependencias del módulo
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/moodly/app/
│           ├── MainActivity.kt                   ← Activity principal (Navigation Host)
│           │
│           ├── data/
│           │   ├── model/
│           │   │   ├── EntradaDiaria.kt          ← Entity Room + enums
│           │   │   └── EstadoAnimo.kt            ← Enum 5 niveles
│           │   └── local/
│           │       ├── EntradaDao.kt             ← DAO Room
│           │       ├── MoodlyDatabase.kt         ← Singleton Room DB
│           │       └── EntradaRepository.kt      ← Repositorio (fuente única de verdad)
│           │
│           ├── ui/
│           │   ├── theme/
│           │   │   ├── Color.kt                  ← Paleta de colores
│           │   │   ├── Type.kt                   ← Tipografía
│           │   │   └── Theme.kt                  ← MoodlyTheme
│           │   │
│           │   ├── home/
│           │   │   ├── HomeViewModel.kt
│           │   │   └── HomeScreen.kt
│           │   │
│           │   ├── registro/
│           │   │   ├── RegistroViewModel.kt
│           │   │   └── RegistroScreen.kt
│           │   │
│           │   ├── historial/
│           │   │   ├── HistorialViewModel.kt
│           │   │   └── HistorialScreen.kt
│           │   │
│           │   ├── detail/
│           │   │   └── DetailScreen.kt           ← Detalle de una entrada
│           │   │
│           │   └── recursos/
│           │       └── RecursosScreen.kt         ← Pantalla estática
│           │
│           └── navigation/
│               ├── MoodlyNavHost.kt              ← NavHost + rutas
│               └── BottomNavBar.kt               ← Barra inferior
│
├── build.gradle.kts                              ← build raíz
└── settings.gradle.kts
```

## Orden de creación en Android Studio

1. Crea el proyecto: **Empty Activity** · Package: `com.moodly.app` · Min SDK 26
2. Reemplaza `build.gradle.kts` (módulo app) con el proporcionado
3. Crea los packages en `java/com/moodly/app/` replicando la estructura
4. Copia cada archivo `.kt` en su carpeta correspondiente
5. Reemplaza `MainActivity.kt`
6. Reemplaza `AndroidManifest.xml`
7. Sync Gradle → Run
