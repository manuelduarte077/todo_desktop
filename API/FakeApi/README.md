# FakeApi - Aplicación Android con Principios SOLID

Esta aplicación Android consume una API de productos y muestra los resultados en una interfaz de usuario moderna. El código ha sido refactorizado para seguir los principios SOLID.

## Capturas de Pantalla

| Lista de Productos | Lista de Productos |        Detalle del Producto        |
|:------------------:|:--------------------:|:----------------------------------:|
| <img src="docs/1.png" width="250"> | <img src="docs/3.png" width="250"> | <img src="docs/3.png" width="250"> |


## Principios SOLID Aplicados

### S - Principio de Responsabilidad Única (Single Responsibility Principle)
- Cada clase tiene una única responsabilidad:
  - `ApiService`: Define las operaciones de la API
  - `OkHttpApiService`: Implementa las operaciones de la API usando OkHttp
  - `ApiClient`: Actúa como fachada para el acceso a la API
  - Las actividades se han dividido en métodos con responsabilidades claras

### O - Principio Abierto/Cerrado (Open/Closed Principle)
- Las clases están abiertas para extensión pero cerradas para modificación:
  - `AbstractAdapter`: Proporciona implementación base para adaptadores
  - Los nuevos adaptadores pueden extender esta clase sin modificarla

### L - Principio de Sustitución de Liskov (Liskov Substitution Principle)
- Las clases derivadas pueden sustituir a sus clases base:
  - `Product` e `Image` implementan `BaseModel`
  - `ProductAdapter` e `ImageCarouselAdapter` extienden `AbstractAdapter`

### I - Principio de Segregación de Interfaces (Interface Segregation Principle)
- Las interfaces se han dividido en interfaces más pequeñas y específicas:
  - `OnSuccessCallback`: Para manejar respuestas exitosas
  - `OnErrorCallback`: Para manejar errores
  - `ApiCallback`: Combina ambas interfaces para clientes que necesitan ambas funcionalidades

### D - Principio de Inversión de Dependencias (Dependency Inversion Principle)
- Las dependencias se han invertido:
  - `ApiClient` depende de `ApiService` (abstracción) en lugar de `OkHttpApiService` (implementación)
  - `ApiServiceFactory` permite crear diferentes implementaciones de `ApiService`

## Estructura del Proyecto

```
app/src/main/java/dev/donmanuel/fakeapi/
├── adapters/
│   ├── AbstractAdapter.java
│   ├── BaseAdapter.java
│   ├── ImageCarouselAdapter.java
│   └── ProductAdapter.java
├── models/
│   ├── BaseModel.java
│   ├── Category.java
│   ├── Image.java
│   └── Product.java
├── network/
│   ├── ApiCallback.java
│   ├── ApiClient.java
│   ├── ApiService.java
│   ├── ApiServiceFactory.java
│   ├── OnErrorCallback.java
│   ├── OnSuccessCallback.java
│   └── OkHttpApiService.java
├── MainActivity.java
└── ProductDetailActivity.java
```

## Beneficios de la Refactorización SOLID

1. **Mayor mantenibilidad**: Cada clase tiene una responsabilidad clara
2. **Mejor testabilidad**: Las interfaces permiten crear mocks para pruebas unitarias
3. **Extensibilidad mejorada**: Nuevas funcionalidades pueden agregarse sin modificar código existente
4. **Acoplamiento reducido**: Las clases dependen de abstracciones, no de implementaciones concretas
5. **Cohesión aumentada**: Las clases tienen un propósito bien definido
