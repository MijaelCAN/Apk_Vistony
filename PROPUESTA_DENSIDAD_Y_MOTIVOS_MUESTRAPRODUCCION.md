# Propuesta — Incorporar cálculo de densidad y catálogo de motivos en `muestraProduccion`

**Contexto:** el formulario original (`clean/` — ver [`INFORME_MODULO_LABORATORIO.md`](./INFORME_MODULO_LABORATORIO.md)) no soportaba que cada muestra (Tanque / Envasado-Inicio / Envasado-Fin) tuviera **varios intentos** hasta ser aprobada: cada actualización sobreescribía el registro anterior y se perdía el historial. Por eso se diseñó un modelo nuevo (`Screen/muestraProduccion/*`, `Service/MuestraProduccionService.kt`, endpoints `v1/muestras/*`) donde **cada intento es un documento propio** (`docEntry` + `counter` + `version`), enlazado a la OF (`numOf`)/envase (`numEn`) y a un "tipo" de etapa. Ese modelo nuevo todavía no tiene (a) cálculo de densidad y (b) catálogo real de motivos de rechazo/corrección — hoy son un valor hardcodeado y una lista fija en el cliente. Este documento propone cómo cerrar esas dos brechas **sin reintroducir** los problemas ya detectados en el formulario viejo.

---

## 1. Mapeo conceptual: Laboratorio (clean) → muestraProduccion (v1)

| Concepto en `clean/` (Laboratorio)                                                                                                                          | Equivalente en `muestraProduccion` (v1)                                                                                                                                                          | Notas                                                                                                                                                                                                                                             |
| ----------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Una OF con 3 resultados simultáneos: Muestra 1 (Tanque), Muestra 2 (Envasado-Inicio), Muestra 3 (Envasado-Fin) — todo en una sola fila que se sobreescribía | Cada etapa es su propio recurso (`type = "MEZCLA"`, `"ENVASADO_INICIO"`, `"ENVASADO_FIN"`), y cada **intento** dentro de una etapa es un `docEntry` nuevo (`counter`/`version` crecientes)       | `DetalleMezclaScreen.kt:316-319` (`siguienteTipoMuestra()`): no se avanza de etapa hasta que la actual queda `APROBADO`. Resuelve la pérdida de historial: el `timeline` (`MuestraProduccionTimelineItem`) muestra todos los intentos anteriores. |
| Varios envases bajo una misma OF (`detail: List<ManufacturingOrderDetailModel>`)                                                                            | Varias Órdenes de Envase (`numEn`) bajo una misma OF (`numOf`), cada una con su propia secuencia de intentos                                                                                     | Ver `ConsultaNuevaMuestra` (`Entidad/Muestra.kt:622-650`): "Puede traer varios envases... cuando se consulta solo con numOf + type".                                                                                                              |
| Estados `Aprobado/Rechazado/Pendiente/No Conforme` colapsando "Rechazado" y "No Conforme" al mismo código (`B1` del informe)                                | `status` es un string libre (`PENDIENTE`, `EN_ANALISIS`, `APROBADO`, `RECHAZADO`, `NO_CONFORME`) — **ya distingue ambos casos**                                                                  | `DetalleMezclaScreen.kt:408-429` (`DecisionChip` "NO CONFORME" envía `status="NO_CONFORME"`, distinto de `"RECHAZADO"`). ✅ Bug B1 ya resuelto por este modelo.                                                                                    |
| "¿Se ha realizado corrección?" + "Motivo Corrección" como flags aparte del estado                                                                           | No existen como flags aparte: la corrección **es** el siguiente intento (nuevo `counter`/`version`), y el motivo del intento anterior queda visible en el `timeline`                             | Simplifica el modelo: no hace falta una pregunta "¿hubo corrección?" — si hay un intento nuevo, hubo corrección.                                                                                                                                  |
| `pesoOptimo`/`pesoMaximo` fijos en el DTO, con el bug de copiar uno sobre el otro (`B2`)                                                                    | No hay equivalente fijo: el mecanismo genérico `parametros: List<ParametroValorRequest>` (`Entidad/Muestra.kt:710-723`) está pensado para cualquier valor medido, identificado por `idParametro` | Aquí es donde debe entrar la densidad (sección 3) y, si aplica, peso óptimo/máximo de envase — pero **vía catálogo**, no como campos fijos del DTO.                                                                                               |
| Densidad: `POST CalculoDensidad` recalcula estado a partir del valor digitado                                                                               | **No existe todavía**                                                                                                                                                                            | Brecha a cerrar — sección 3.                                                                                                                                                                                                                      |
| Motivos de rechazo: `GET MotivoCorreccion` (catálogo real desde SAP)                                                                                        | Lista hardcodeada `MOTIVOS_RECHAZO` en el cliente (`DetalleMezclaScreen.kt:321-328`)                                                                                                             | Brecha a cerrar — sección 4.                                                                                                                                                                                                                      |

---

## 2. Qué bugs del informe anterior ya vienen resueltos por este modelo

Antes de proponer cambios, vale registrar que el rediseño en `muestraProduccion` ya corrige, por construcción, varios de los problemas señalados en `INFORME_MODULO_LABORATORIO.md` sobre `clean/`. **No hay que volver a tocar esto, solo no romperlo al integrar densidad/motivos:**

| Bug del informe anterior                             | Estado en `muestraProduccion`                                                                                                                                                                                                                       |
| ---------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| B1 — "Rechazado" y "No Conforme" mismo código        | ✅ Resuelto: `status` los distingue (`"RECHAZADO"` vs `"NO_CONFORME"`).                                                                                                                                                                              |
| B3 — errores de API tragados en silencio             | ✅ Resuelto: `MuestraRepository` usa `Result<T>` con `fold`, y `MuestraViewModel` siempre setea `_errorMessage` visible en pantalla (`MuestraViewModel.kt:332-505`).                                                                                 |
| B4 — JSON armado a mano sin escapar                  | ✅ Resuelto: todos los `@Body` de `MuestraProduccionService.kt` son DTOs tipados serializados por Gson/Retrofit.                                                                                                                                     |
| B5 — confusión entre `docNum` de orden vs. de envase | ✅ Resuelto: cada intento tiene su propio `docEntry: Int` inequívoco; `numOf`/`numEn` viajan aparte y con significado fijo.                                                                                                                          |
| B6 — carrera entre guardar y refrescar               | ✅ Resuelto: `finalizarAnalisis`/`iniciarAnalisis` llaman a `obtenerMuestraProduccionDetalle(docEntry)` **dentro del mismo `result.fold` onSuccess**, de forma secuencial — no hay temporizadores paralelos (`MuestraViewModel.kt:464-468,490-494`). |
| B7 — sin control de rol                              | ✅ Resuelto: `esCalidad = currentUser.role.equals("ASEG. CALIDAD", ...)` gatea las acciones de laboratorio en `DetalleMezclaScreen.kt:103` y `MisOFScreen.kt:93`.                                                                                    |
| M5 — `KeyboardType.NumberPassword` en campo numérico | ✅ Resuelto: `NuevaMuestraScreen.kt:213` usa `KeyboardType.Number`.                                                                                                                                                                                  |
| M6 — doble envío sin loading state                   | ✅ Resuelto: los botones de acción se deshabilitan con `enabled = !isProcesando`/`!isCreating` (`DetalleMezclaScreen.kt:348,377`, `NuevaMuestraScreen.kt:414`).                                                                                      |

Esto confirma que conviene seguir extendiendo **este** modelo (en vez de portar piezas del viejo `clean/`), y que el patrón a imitar para lo nuevo es: DTO tipado + `Result.fold` + estado de error visible + sin temporizadores paralelos.

---

## 3. Brecha 1 — Cálculo de densidad

### 3.1 Qué falta hoy

En `DetalleMezclaScreen.kt:112-126`, al confirmar la resolución de un intento, se envía siempre:

```kotlin
// TODO: reemplazar por los parámetros reales medidos cuando se defina el catálogo
parametros = listOf(ParametroValorRequest(idParametro = 1, valor = "1000"))
```

Es un valor inventado, igual para **toda** muestra, de **cualquier** tipo (MEZCLA/ENVASADO_INICIO/ENVASADO_FIN). No hay ningún campo en la UI para que Calidad digite la densidad medida, y no hay forma de saber si el valor está dentro de especificación.

### 3.2 Diseño propuesto

El propio modelo ya da la pista correcta: `parametros: List<ParametroValorRequest>` (`idParametro` + `valor`) es un mecanismo **genérico**, pensado para cualquier medición (densidad, viscosidad, peso, temperatura, etc.), no solo densidad. Conviene resolver esto con un catálogo, igual que ya existe para las especificaciones de soplado (`obtenerEspecificacionSoplado` + `validarRango()`, `MuestraViewModel.kt:222-264`), en vez de volver a hardcodear un campo fijo "densidad" como tenía `clean/`.

**Paso 1 — Catálogo de parámetros por tipo de muestra** (nuevo endpoint backend):

```
GET v1/muestras/parametros?type=MEZCLA
```

```jsonc
{
  "data": [
    { "idParametro": 1, "nombre": "Densidad", "unidad": "g/cm3", "tipoValor": "NUMERICO", "min": 0.95, "max": 1.05, "obligatorio": true },
    { "idParametro": 2, "nombre": "Viscosidad", "unidad": "cP", "tipoValor": "NUMERICO", "min": 3500, "max": 4200, "obligatorio": false }
  ]
}
```

El backend ya conoce el rango (lo usaba `CalculoDensidad`/`EspecificacionSoplado` en el modelo viejo) — aquí simplemente se expone como catálogo en vez de estar atado a un único endpoint de densidad.

**Paso 2 — (Opcional) Verificación en vivo, sin mutar estado:**

```
POST v1/muestras/{docEntry}/parametros/verificar
Body: { "parametros": [ { "idParametro": 1, "valor": 0.978 } ] }
Response: { "data": [ { "idParametro": 1, "valor": 0.978, "cumple": true, "mensaje": null } ] }
```

Este endpoint **no persiste nada** (a diferencia del viejo `CalculoDensidad`, que mezclaba "calcular" con "actualizar el estado de la OF"). Sirve solo para que Calidad vea, antes de confirmar, si el valor digitado cae dentro de especificación — evitando revivir la condición de carrera B6 (nada de temporizadores: es un botón explícito "Verificar", con su propio loading state, que no dispara ningún refresh de pantalla).

**Paso 3 — Persistencia real, al confirmar resolución:**
Los valores digitados (verificados o no) se mandan como ya está pensado, dentro de `finalizarAnalisis`:

```kotlin
parametros = parametrosCatalogo.map { p ->
    ParametroValorRequest(idParametro = p.idParametro, valor = valoresIngresados[p.idParametro])
}
```

El backend ya devuelve `todosCumplen`/`parametrosRegistrados` en `FinalizarAnalisisResponse` (`Entidad/Muestra.kt:726-734`) — hoy esa respuesta se ignora; debe mostrarse (p. ej. un aviso si `todosCumplen == false` pese a que Calidad eligió "Aprobado", para que no se autoricen lotes fuera de rango por error de digitación).

### 3.3 Cambios concretos por capa

| Capa                                              | Cambio                                                                                                                                                                                                                                                                                                                                                                                                           |
| ------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `Entidad/Muestra.kt`                              | Agregar `ParametroCatalogoItem` (`idParametro, nombre, unidad, tipoValor, min, max, obligatorio`) y `ParametroCatalogoResponse`. Opcional: `VerificarParametrosRequest/Response`.                                                                                                                                                                                                                                |
| `Service/MuestraProduccionService.kt`             | Agregar `obtenerCatalogoParametros(type: String): Response<ParametroCatalogoResponse>` y, si se adopta el paso 2, `verificarParametros(docEntry, body)`.                                                                                                                                                                                                                                                         |
| `Repository/MuestraRepository.kt`                 | Wrappers `Result<...>` siguiendo el mismo patrón que el resto del archivo (try/catch + `mensajeErrorApi`).                                                                                                                                                                                                                                                                                                       |
| `ViewModel/MuestraViewModel.kt`                   | Nuevo `_catalogoParametros: MutableStateFlow<List<ParametroCatalogoItem>>`, `_valoresParametros: MutableStateFlow<Map<Int, String>>`, función `cargarCatalogoParametros(type)` (llamada junto con `obtenerMuestraProduccionDetalle`), función `actualizarValorParametro(idParametro, valor)`, y construir `parametros` reales dentro de `finalizarAnalisis` en lugar de recibirlos ya armados desde la pantalla. |
| `Screen/muestraProduccion/DetalleMezclaScreen.kt` | En `AccionesCalidad`, cuando `analisisIniciado && !analisisFinalizado`, renderizar un campo por cada ítem del catálogo (`OutlinedTextField` numérico, con `validarRango()` ya existente para feedback en vivo), eliminar el `parametros` hardcodeado del callback `onConfirmarResolucion`, y mostrar el resultado `todosCumplen` tras confirmar.                                                                 |

---

## 4. Brecha 2 — Catálogo de motivos (corrección / rechazo)

### 4.1 Qué falta hoy

`DetalleMezclaScreen.kt:321-328`:

```kotlin
private val MOTIVOS_RECHAZO = listOf(
    "Viscosidad fuera de rango", "Partículas visibles", "Tonalidad incorrecta",
    "Olor no conforme", "Contaminación", "Otro"
)
```

Lista fija en el cliente, igual para cualquier `type` de muestra, y **solo se muestra cuando `decisionSeleccionada == "RECHAZADO"`** (`:432`) — si Calidad elige **"NO CONFORME"**, no hay forma de registrar motivo ni causa, solo queda la observación libre. Esto es justo el mismo problema de fondo que ya existía en `clean/` (catálogo de motivos no conectado al backend), solo que ahora ni siquiera hay endpoint de por medio.

### 4.2 Diseño propuesto

**Opción A (recomendada) — Catálogo v1 nuevo, consistente con el resto del modelo:**

```
GET v1/muestras/motivos?type=MEZCLA
```

```jsonc
{ "data": [ { "codigo": "VISC_FUERA_RANGO", "nombre": "Viscosidad fuera de rango" }, ... ] }
```

Igual que el catálogo de parámetros, parametrizable por `type` (los motivos de un "Envasado-Fin" no son los mismos que los de una "Mezcla"). Mantiene la misma forma de trabajar que ya se adoptó para todo lo demás en v1 (DTOs tipados, `Result.fold`).

**Opción B (más rápida, reutilizando lo que ya existe) — exponer el catálogo legacy:**
Reutilizar `GET /api/Laboratorio/MotivoCorreccion` (`ApiService.kt:29-30`, ya documentado en el informe anterior) desde `MuestraRepository`, si el catálogo de motivos en SAP es el mismo para ambos flujos. Es más rápido de implementar (cero cambios de backend) pero acopla `muestraProduccion` a un endpoint pensado para el módulo viejo, y no permite filtrar motivos por `type`. Recomendado solo como solución puente si hay urgencia.

**Tratamiento de "No Conforme":** se propone extender la condición de la UI para que el bloque de motivo/causa aparezca también cuando `decisionSeleccionada == "NO_CONFORME"` (hoy solo aplica a `"RECHAZADO"`, `DetalleMezclaScreen.kt:432`), ya que de lo contrario un "No Conforme" queda sin trazabilidad de causa. Esto es una decisión de negocio a confirmar con el equipo de Calidad: ¿"No Conforme" necesita motivo igual que "Rechazado", o es deliberadamente más simple?

### 4.3 Cambios concretos por capa

| Capa                                              | Cambio                                                                                                                                                                                                                                                                                    |
| ------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `Entidad/Muestra.kt`                              | `MotivoCorreccionItem(codigo, nombre)` + `MotivoCorreccionResponse`.                                                                                                                                                                                                                      |
| `Service/MuestraProduccionService.kt`             | `obtenerMotivos(type: String): Response<MotivoCorreccionResponse>` (Opción A) — o reutilizar `MuestraService.kt` si se va por Opción B.                                                                                                                                                   |
| `Repository/MuestraRepository.kt`                 | `obtenerMotivos(type)` con el mismo patrón `Result.fold`.                                                                                                                                                                                                                                 |
| `ViewModel/MuestraViewModel.kt`                   | `_motivosRechazo: MutableStateFlow<List<MotivoCorreccionItem>>`, cargada al entrar a `DetalleMezclaScreen` (junto al detalle), reemplazando la dependencia del `MOTIVOS_RECHAZO` estático.                                                                                                |
| `Screen/muestraProduccion/DetalleMezclaScreen.kt` | El `ExposedDropdownMenuBox` de motivo pasa a iterar `motivosRechazo` (desde el ViewModel) en lugar de `MOTIVOS_RECHAZO`; ampliar la condición `decisionSeleccionada == "RECHAZADO"` para incluir `"NO_CONFORME"` (pendiente de confirmar con negocio). Eliminar la constante hardcodeada. |

---

## 5. Reglas de negocio que deben preservarse explícitamente

**No se pierde el historial**: cada intento (rechazo, no conforme o aprobación) queda como un registro propio en el `timeline`; nada se sobreescribe. *(ya garantizado por el modelo `docEntry`/`counter`/`version` — no tocar.)*

**No se puede registrar un intento nuevo si el anterior está sin resolver**: `tieneIntentoPendiente`/`mensajeIntentoPendiente` (`Entidad/Muestra.kt:634-649`) ya lo bloquea en `NuevaMuestraScreen`. Cualquier campo nuevo (densidad, motivo) debe respetar este guardrail — no agregar atajos que permitan crear un intento nuevo "para corregir" sin que el anterior quede `APROBADO`/`RECHAZADO`/`NO_CONFORME` resuelto.

**No se avanza de etapa sin aprobar la anterior**: `siguienteTipoMuestra()` no debe modificarse al añadir parámetros — la validación de "todosCumplen" es adicional a esto, no un reemplazo.

**La densidad (y cualquier parámetro medido) se captura en el momento de "Confirmar Resolución"**, no al registrar la muestra (`NuevaMuestraScreen` es responsabilidad de Producción, que entrega la muestra; la medición es responsabilidad de Calidad al analizarla) — coherente con cómo ya está repartido el resto del formulario.

**El motivo de rechazo/no-conformidad debe ser trazable y consultable después**: por eso debe venir de catálogo (`typeReject`/`reason`), no quedar solo en el campo libre "Observación".

---

## 6. Preguntas abiertas para validar con el equipo (no asumidas en esta propuesta)

- ¿El catálogo de motivos debe variar según `type` (Mezcla vs. Envasado-Inicio vs. Envasado-Fin), o es uno solo para todos?
- ¿"No Conforme" requiere motivo/causa igual que "Rechazado", o es intencionalmente un estado "informativo" sin catálogo?
- ¿La verificación de densidad (paso 2, sección 3.2) debe ser un endpoint de "preview" separado, o basta con validar rangos en el cliente (como ya se hace para soplado con `validarRango()`) y dejar que el backend solo valide al persistir en `finalizar-analisis`?
- ¿El catálogo de parámetros (`idParametro`) es el mismo para Mezcla/Envasado, o cada etapa mide cosas distintas (p. ej. Envasado no mide densidad sino peso/altura, ya cubierto hoy por `EspecificacionSopladoData`)? Si es así, probablemente conviene que el endpoint del catálogo determine automáticamente qué mostrar según `type`, sin que el cliente tenga que decidirlo.
