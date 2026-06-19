# Informe técnico — Módulo "Formulario de Calidad / Laboratorio" (`clean/`)

**Alcance:** todo el paquete `app/src/main/java/com/vistony/app/clean/**`.
**Ruta de navegación:** `composable("manufacturingOrder")` → `MainActivity.kt:214-218`.
**Pantalla (TopBar):** "Formulario de Calidad" (`ManuFacturingOrderTemplate.kt:60`).

> Nota de alcance: este módulo es **independiente** del flujo legacy "Muestra Producción" (`Screen/muestraProduccion/*`, `MuestraRepository.kt`, endpoints `muestraProduccionService.*`). Ambos tratan temas de laboratorio/calidad de producción pero no comparten código, DTOs ni backend (este módulo solo habla con `/api/Laboratorio/*`). Si la intención es que `clean/` reemplace al flujo legacy, hoy conviven dos implementaciones distintas — vale la pena confirmarlo con el equipo.

---

## 1. Arquitectura

Clean Architecture de 3 capas, con Hilt para DI:

```
presentation/  (pages → templates → organisms → moleculs → atoms)  +  viewmodels/
domain/        (model, repository interface, usecases)
data/          (dto, mappers dto→model, datasource remoto, repository impl)
core/          (network: ApiService/RetrofitClient, utils: Constants, Zebra scanner)
```

Una sola entidad de negocio: **Orden de Fabricación (OF)**, con una cabecera (`ManufacturingOrderModel`) y un detalle de envases (`ManufacturingOrderDetailModel`).

### Endpoints (`ApiService.kt:19-32`)

| Método | Endpoint | Uso | Body |
|---|---|---|---|
| GET | `/api/Laboratorio/ListOF?Parametro=` | Buscar OF por código/escaneo | — |
| POST | `/api/Laboratorio/CalculoDensidad` | Recalcular densidad y refrescar estado de la OF | JSON manual |
| PATCH | `/api/Laboratorio/Liberacion` | Guardar/actualizar aprobación (liberación de lote) | JSON manual |
| GET | `/api/Laboratorio/MotivoCorreccion` | Listado de motivos de rechazo/corrección | — |

**Ninguno de los 4 endpoints envía cabecera de autenticación/autorización** (no hay `@Header`, ni `Interceptor`, ni token) — ver sección 6.4.

---

## 2. Flujo funcional (paso a paso)

1. Usuario entra a "Formulario de Calidad" → `ManuFacturingOrderSectionMain` carga automáticamente el listado de motivos de rechazo (`getReasonForRejections()`).
2. Usuario escanea (Zebra DataWedge, vía `ScanViewModel`/`ObservableObject`) o escribe el número de OF → se llama `getManufacturingOrder(orderCode)` → `GET ListOF`.
3. Se muestra la cabecera de la OF: descripción, spinner **"Resultado de Análisis Muestra 1 - Tanque"** (Aprobado/Rechazado/Pendiente/No Conforme), spinner **"¿Se ha realizado corrección?"**, spinner condicional **"Motivo Corrección"**, campo **Observaciones**, campo **Densidad** (con botón para recalcular → `POST CalculoDensidad`).
4. Cada cambio en cualquiera de esos campos dispara `onStatusAprobationHeader1Change(...)`, que:
   - Llama `PATCH Liberacion` con el estado actual de cabecera.
   - Si el resultado es **"No Conforme"**, fuerza los 3 estados de envase (`Linea1/2/3`) a "Rechazado" y los persiste.
   - Si no, recorre **todos** los envases de la OF y vuelve a llamar `PATCH Liberacion` una vez por cada uno, fijo con `"Linea1"`.
5. Debajo se listan los **envases** ("Envases"), cada uno con 3 `StatusChip` (Muestra 1/2/3) y un botón de edición (✏️) habilitado **solo si** la cabecera está en "Aprobado".
6. El botón ✏️ abre un diálogo (`ManufacturingOrderCustomDialog`) con 3 spinners (Muestra 1/2/3 del envase) y un botón "Guardar" que llama `saveStatusAprobation()` → hasta 3 `PATCH Liberacion` (Linea1/2/3) para ese envase.

---

## 3. Reglas de negocio identificadas

- **R1 – Validación de pesos:** para guardar una aprobación de envase (`saveStatusAprobation`), peso óptimo y peso máximo deben existir y ser distintos de `"0"`; si no, se bloquea con un diálogo de error (`ManufacturingOrderViewModel.kt:342-401`).
- **R2 – Cascada por "No Conforme":** si la Muestra 1 (tanque) se marca "No Conforme", los 3 envases se marcan automáticamente "Rechazado" (`ManufacturingOrderViewModel.kt:227-233`).
- **R3 – Habilitación progresiva:** la edición de Muestra 2/3 de un envase (envasado) solo se habilita si la Muestra 1 (tanque) está "Aprobado" (`ManuFacturingOrderMoleculs.kt:454,470,545,558`).
- **R4 – Recalcular densidad:** al cambiar la densidad o cualquier estado de cabecera, se reconsulta el cálculo de densidad contra el backend, que devuelve de vuelta el estado de aprobación, corrección, motivo y observaciones (es decir, **el backend decide** el resultado de aprobación, no el cliente).
- **R5 – Mapeo de estados:** `Pendiente`→`*`, `Aprobado`→`S`, `Rechazado`/`No Conforme`→`N` (ver bug B1).

---

## 4. Bugs y problemas — Críticos

### B1. "Rechazado" y "No Conforme" colapsan al mismo código → se pierde la distinción al refrescar
`ManufacturingOrderViewModel.kt:200-203`:
```kotlin
approvalStatus = if (newValue.equals("Pendiente")) "*" 
    else if (newValue.equals("Aprobado")) "S" 
    else if (newValue.equals("Rechazado")) "N" 
    else if (newValue.equals("No Conforme")) "N" else "*",
```
El dropdown de cabecera ofrece 4 opciones (`DEFAULT_APPROBATIONS_HEADER`, `ManufacturingOrderModel.kt:57-62`), pero solo existen 3 códigos en el contrato con el backend (`*`, `S`, `N`). Al elegir "No Conforme" se envía `"N"`, idéntico a "Rechazado". Si el backend deriva el texto `aprobacion1` a partir de ese código (lo más probable, dado que es el mismo campo que se actualiza), la próxima vez que se cargue la OF el spinner mostrará **"Rechazado"** en vez de "No Conforme" — el estado elegido por el usuario no persiste tal cual. Es necesario que el backend exponga/acepte un cuarto código distinto, o que el cliente lo módele con un campo adicional.

Además, justo arriba (`:181-185`) se busca `approval` por nombre en `DEFAULT_APPROBATIONS_HEADER` solo para validar que exista, pero su `.code` **nunca se usa** — el código real se recalcula a mano en el `if/else` citado, duplicando la lógica y permitiendo que ambas reglas diverjan en el futuro.

### B2. Copy-paste: el "peso máximo" se llena con el "peso óptimo"
`ManuFacturingOrderMoleculs.kt:265-270`, al cambiar el spinner de cabecera:
```kotlin
viewModel.onOptimalWeightChange(it.detail.firstOrNull()?.optimumWeight ?: "0")
viewModel.onMaximunWeightChange(it.detail.firstOrNull()?.optimumWeight ?: "0")  // ← debería ser maximumWeight
```
`onMaximunWeightChange` recibe `optimumWeight` en lugar de `maximumWeight`. Consecuencia: el "peso máximo" que se valida (regla R1) y que se envía al backend como `pesoMaximo` en cada `PATCH Liberacion` disparado desde este flujo **siempre es igual al peso óptimo**, nunca el valor real de peso máximo de la OF/envase. Esto corrompe el dato persistido en el servidor para `pesoMaximo` cada vez que se cambia el estado de cabecera (no solo un problema de UI).

### B3. Falta verificación de éxito de la API — errores del backend son invisibles
- `getManufacturingOrder`, `recalculateDensity`, `getReasonForRejections` (`ManufacturingOrderRemoteDataSource.kt:17-106`) atrapan **toda** excepción y, en caso de error HTTP o de red, devuelven silenciosamente un DTO vacío (`data = listOf()`), solo dejando rastro en `Log.e`. La UI no distingue "OF no existe", "sin conexión" o "error 500" — en los 3 casos se ve el mismo ícono de fábrica vacío (`ManuFacturingOrderMoleculs.kt:142-160`), sin mensaje.
- `updateApprovalStatus` (`ManufacturingOrderRemoteDataSource.kt:62-89`) **no retorna nada** (`Unit`), ni siquiera el booleano `isSuccessful`. El `ViewModel`/UI nunca sabe si la liberación de lote realmente se guardó en el servidor. En un flujo de control de calidad que decide si un lote pasa o no a producción, esto es crítico: si el PATCH falla (red, validación de SAP, etc.), **la app sigue mostrando como aprobado/rechazado localmente** sin avisar al operario que el cambio no se persistió.
- En ningún punto del `ViewModel` se lee `manufacturingOrderResponseModel.value.sucess` o `.message`, ni `reasonForRejectionsResponseModel.value.success`/`.message`. Esos campos existen en los modelos (`ManufacturingOrderModel.kt:6-9,80-85`) pero se ignoran sistemáticamente.

### B4. Construcción manual de JSON sin escapar — riesgo de payload corrupto
`ManufacturingOrderRemoteDataSource.kt:38-46` y `:66-69` construyen el body a mano con interpolación de strings:
```kotlin
json = "{ \"nroof\":\"${docNum}\",...,\"comentario\":\"${observations}\"}"
```
Si `observations` (texto libre escrito por el usuario, `ManuFacturingOrderEditTextView` sin restricciones) contiene comillas `"` o backslashes, el JSON queda mal formado. La petición fallará silenciosamente (cae en el `catch` genérico de B3) sin que el usuario lo note. Ya existe un DTO tipado para esto, `ManufacturingOrderUpdateStatus` (`data/api/ManufacturingOrderDto.kt:75-91`) con sus `@SerializedName`, **pero no se usa en ningún lado** (código muerto) — Retrofit ya soporta `@Body` con objetos serializados por Gson en el resto del proyecto; aquí se optó por strings manuales, perdiendo el escapado automático que Gson provee gratis.

### B5. `saveStatusAprobationDetail()` usa el identificador equivocado como `docNum`
`ManufacturingOrderViewModel.kt:417-461` (camino "No Conforme") recorre `detail` (envases) y usa `it.batchName` (lote del **envase**) como `docNum`/`nroof` en el PATCH. En cambio, el camino normal de cabecera (`onStatusAprobationHeader1Change`, parámetro `docNum`) usa el `batchName` de la **orden** (ver invocación en `ManuFacturingOrderMoleculs.kt:264`: `viewModel.onDocNumChange(it.batchName)` donde `it` es la OF). Son dos significados distintos de "docNum" (orden vs. envase) usados indistintamente para el mismo campo `nroof` del backend, dependiendo de qué camino del código se ejecute — alto riesgo de que el backend actualice el registro equivocado o rechace la petición.

### B6. Carrera entre "guardar" y "refrescar" — se puede ver estado desactualizado
`ManufacturingOrderCustomDialog` (`ManuFacturingOrderMoleculs.kt:507-513,523-527`): al confirmar, se dispara `viewModel.saveStatusAprobation()` (que internamente hace hasta 3 llamadas `PATCH` secuenciales en su propia coroutine) y, **en paralelo**, un temporizador independiente de 2 segundos que luego llama `getCalculateDensity` para refrescar la vista. Si las 3 escrituras tardan más de 2 segundos en total (plausible en red lenta), el refresco se ejecuta **antes** de que el guardado termine, mostrando al usuario el estado anterior como si fuera el definitivo. Ya existe un segundo temporizador casi idéntico en `ManuFacturingOrderDetailBody` (`:228-234`, delay de 3000 ms) que puede dispararse en superposición con el anterior, generando dos llamadas concurrentes a `getCalculateDensity` cuya respuesta más lenta "gana" y pisa a la otra.

### B7. No hay control de roles/permisos en esta pantalla
La ruta `manufacturingOrder` (`MainActivity.kt:214-218`) no aplica ninguna validación de rol antes de mostrar el formulario, y dentro de `clean/` no hay ninguna referencia a rol/permiso (`UserState` solo se usa para loguear y para el drawer). Cualquier usuario que llegue a esa ruta puede aprobar/rechazar lotes y "liberar" producción. Esto contrasta con el flujo legacy hermano (`Screen/muestraProduccion/*`), que sí implementa condicionales por rol de Laboratorio/Producción (commit `4dee3c3`). Vale la pena confirmar si la falta de control aquí es intencional o un vacío de seguridad pendiente.

---

## 5. Bugs y problemas — Moderados

### M1. Comparaciones de estado sensibles a mayúsculas, inconsistentes entre sí
`ManufacturingOrderViewModel.kt:303,325`:
```kotlin
_statusCorrection.value = if(order.correction.equals("NO")) "No" else if (order.correction.equals("SI")) "Si" else "No"
```
usa `.equals()` **sin** `ignoreCase = true`, mientras que casi todas las demás comparaciones de estado en el mismo archivo sí lo usan (p. ej. `:454,470,545,558` en Moleculs, o `ignoreCase = true` en `onStatusAprobationHeader1Change`). Si el backend devuelve `"Si"/"No"` o `"si"/"no"` en vez de `"SI"/"NO"`, el valor cae silenciosamente al `else → "No"`, perdiendo el dato real devuelto por el servidor.

### M2. Una sola acción de usuario dispara N+1 llamadas PATCH
`onStatusAprobationHeader1Change` (`ManufacturingOrderViewModel.kt:192-264`), cuando el resultado no es "No Conforme", primero hace **un** `PATCH Liberacion` para la cabecera y luego **recorre todos los envases de la OF** llamando `PATCH Liberacion` otra vez por cada uno, fijando siempre `"Linea1"` (`:236-251`). Cambiar un solo spinner genera entre 2 y N+1 peticiones de red secuenciales sin agrupar, sin debounce y sin manejo de fallo parcial (si la 2ª de 5 falla, las demás igual continúan y nadie se entera, ver B3).

### M3. Estado "contenedor" muerto en el ViewModel
`_isStatusApprobationContainer1/2/3` (`ManufacturingOrderViewModel.kt:74-81,136-146`) se exponen, se leen en `ManuFacturingOrderMoleculs.kt:500-502`, pero **nunca se usan** para habilitar/deshabilitar nada (la lógica real usa `statusAprobationHeader1` directamente, y la línea que los consumía está comentada: `:557`). Indica una refactorización a medio terminar (se intentó habilitar Muestra 2/3 de forma progresiva — secuencial — y se abandonó sin limpiar el código).

### M4. Reseteo de densidad que nunca surte efecto
En `ManuFacturingOrderHead` (`ManuFacturingOrderMoleculs.kt:78-84`) y en los 3 disparadores de búsqueda de OF (`onClickLeadingIcon`, `eventKeyboardGO`, `onClickTrailingIcon`, `:110-123`), el patrón es siempre:
```kotlin
viewModel.getManufacturingOrder(it)   // coroutine asíncrona
viewModel.onDensityChange("0")        // se ejecuta antes de que la red responda
```
Como `getManufacturingOrder` solo suspende en el punto de la llamada de red, `onDensityChange("0")` corre primero y luego, cuando la respuesta llega, `_density.value = order.density` la sobrescribe. El `onDensityChange("0")` no cumple ninguna función real (en el mejor caso es un parpadeo visual de "0" antes de mostrar el valor real) — código muerto/confuso que debería eliminarse o re-pensarse (p. ej. limpiar el campo realmente esperando a que la nueva data llegue).

### M5. `KeyboardType.NumberPassword` en el campo de número de OF
`ManuFacturingOrderMoleculs.kt:108`, el campo "Digite o escanee el Nro. de Orden de Fabricación" usa `KeyboardType.NumberPassword`, pensado para PIN/contraseñas numéricas (puede ocultar los dígitos tipeados). Para un código que el operario necesita verificar visualmente mientras escribe, lo esperable es `KeyboardType.Number`. Parece un copy-paste de otro campo.

### M6. Falta de feedback de carga/duplicado de envío al guardar aprobación de envase
`saveStatusAprobation()` (`ManufacturingOrderViewModel.kt:336-403`) no activa ningún flag de "cargando" (a diferencia de `onStatusAprobationHeader1Change`, que sí marca `_isLoadingBodyDetail`). El diálogo (`ManufacturingOrderCustomDialog`) no deshabilita el botón "Guardar" mientras la operación está en curso, por lo que un doble tap puede disparar el guardado (y por tanto el PATCH) dos veces.

### M7. `motivoCorreccion` envía `"-1"` cuando no hay motivo seleccionado/encontrado
`ManufacturingOrderViewModel.kt:188-190`:
```kotlin
val reasonSelected = _reasonForRejectionsResponseModel.value.data.firstOrNull { it.name == reasonSelectedName }
val reasonCode = reasonSelected?.code ?: -1
```
(nótese además que `code` es `String` y el valor por defecto es el `Int -1`, infiriendo un tipo común `Any` — funciona porque ambos tienen `toString()`, pero es un code smell). Si el usuario no eligió motivo, se envía literalmente el string `"-1"` al backend como `motivoCorreccion`, en vez de vacío o `null`; no está confirmado que el backend tolere ese valor "mágico".

---

## 6. Deuda técnica / observaciones de diseño

### 6.1 DTO de actualización nunca utilizado
`ManufacturingOrderUpdateStatus` (`data/api/ManufacturingOrderDto.kt:75-91`) está completamente definido con `@SerializedName` pero no tiene mapper ni se usa como `@Body` en ningún lugar — el código real construye el JSON a mano (ver B4). Sugiere una migración a "Retrofit + DTO tipado" que quedó incompleta.

### 6.2 Dos clientes Retrofit en paralelo
Existen **dos** configuraciones independientes de Retrofit/OkHttp para esta misma `ApiService`:
- `RetrofitClient` (`core/network/RetrofitClient.kt`), objeto singleton manual con timeouts explícitos de 30s — confirmado que **no se usa en ningún archivo del proyecto** (grep no encontró otra referencia fuera del propio archivo). Código muerto.
- `NetworkModule` (`di/NetworkModule.kt`), el que realmente inyecta Hilt, construye un `Retrofit` **sin** cliente OkHttp explícito (usa los timeouts default de OkHttp, no los 30s de `RetrofitClient`).

Mantener ambos es una fuente de confusión: si alguien necesita ajustar timeouts, agregar logging o un interceptor de autenticación, es fácil que lo agregue al objeto que no está realmente en uso.

### 6.3 URL base hardcodeada a una IP de LAN, sin HTTPS
`core/utils/Constants.kt:4`:
```kotlin
const val URL_BASE = "http://192.168.254.27:8036"
```
- Sin HTTPS: los datos del formulario de calidad (incluyendo observaciones y decisiones de aprobación/rechazo) viajan sin cifrar.
- IP fija de LAN: la app solo funciona conectada a esa red/VPN específica, y cualquier cambio de IP o despliegue a otro ambiente (staging/prod) requiere recompilar y republicar el APK — no hay flavor/`BuildConfig` para alternar entornos en este módulo.

### 6.4 Sin autenticación en los endpoints de Laboratorio
Ningún request de `ApiService.kt` agrega cabecera de autorización ni hay un `Interceptor` de Auth en `RetrofitClient`/`NetworkModule`. Si el resto de la app usa un token de sesión (login) para otros módulos, este conjunto de endpoints quedaría como el único expuesto sin protección — confirmar si el backend exige auth a nivel de gateway/red o si es un vacío real.

### 6.5 Catálogo de aprobación con códigos duplicados
`ApprobationDefaults.DEFAULT_APPROBATIONS_HEADER` (`ManufacturingOrderModel.kt:57-62`) define dos entradas con `code = "N"` ("Rechazado" y "No Conforme"). Es la causa raíz de B1; convendría que el dominio tenga un código distinto por estado (p. ej. "N" y "NC") y que el backend lo soporte, o documentar explícitamente que "No Conforme" es solo un atajo de UI para "Rechazado total".

---

## 7. Resumen priorizado de acciones recomendadas

| # | Prioridad | Acción |
|---|---|---|
| B2 | 🔴 Alta | Corregir `onMaximunWeightChange` para usar `maximumWeight` en vez de `optimumWeight` (`ManuFacturingOrderMoleculs.kt:268`). |
| B3 | 🔴 Alta | Propagar éxito/error real de `updateApprovalStatus`/`recalculateDensity`/`getManufacturingOrder` hasta la UI; mostrar mensaje de error explícito al usuario en vez de tragar excepciones. |
| B1 | 🔴 Alta | Definir un código de backend distinto para "No Conforme" (o aceptar que es un alias visual de "Rechazado" y documentarlo) para que no se pierda al refrescar. |
| B5 | 🔴 Alta | Unificar qué identificador (`docNum` de orden vs. de envase) se envía como `nroof` en cada camino de guardado. |
| B4 | 🟠 Media | Reemplazar la construcción manual de JSON por el DTO `ManufacturingOrderUpdateStatus` ya existente, serializado con Gson vía `@Body`. |
| B6 | 🟠 Media | Encadenar el refresco (`getCalculateDensity`) **después** de que la escritura (`saveStatusAprobation`) confirme éxito, no con un timer independiente. |
| B7 | 🟠 Media | Confirmar con negocio si este formulario debe restringirse por rol (Laboratorio/Producción) como su módulo hermano. |
| M2 | 🟡 Media-baja | Agrupar las llamadas PATCH por OF en una sola petición batch, o al menos esperar/confirmar cada una antes de continuar. |
| 6.2/6.3 | 🟡 Media-baja | Eliminar `RetrofitClient` (código muerto) y mover `URL_BASE` a configuración por build-type/flavor en vez de constante fija. |
| M3 | 🟢 Baja | Eliminar el estado muerto `isStatusApprobationContainer1/2/3` o terminar la habilitación progresiva que se dejó a medias. |
| M4, M5 | 🟢 Baja | Limpiar el `onDensityChange("0")` redundante; cambiar `NumberPassword` → `Number` en el campo de código de OF. |
