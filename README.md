# Sistema de Inventario - María Camila
## Aplicación de Escritorio Java (NetBeans + ANT)

---

## 📁 Estructura del proyecto

```
SistemaInventario/
├── build.xml              ← Archivo ANT (compila y ejecuta)
├── manifest.mf            ← Manifiesto del JAR
├── schema.sql             ← Script SQL de referencia
├── lib/
│   └── sqlite-jdbc-3.45.1.0.jar   ← ⚠ DESCARGAR MANUALMENTE
└── src/
    ├── model/
    │   ├── Producto.java
    │   ├── Cliente.java
    │   ├── Empleado.java
    │   ├── Factura.java
    │   ├── DetalleFactura.java
    │   ├── VentaDiaria.java
    │   └── Cuenta.java
    ├── dao/
    │   ├── ProductoDAO.java
    │   ├── ClienteDAO.java
    │   ├── EmpleadoDAO.java
    │   ├── FacturaDAO.java
    │   ├── VentaDiariaDAO.java
    │   └── CuentaDAO.java
    ├── service/
    │   └── DatabaseConnection.java
    └── ui/
        ├── MainFrame.java          ← Menú principal
        ├── InventarioFrame.java
        ├── ClientesFrame.java
        ├── EmpleadosFrame.java
        ├── FacturacionFrame.java
        ├── FacturasListaFrame.java
        ├── VentasDiariasFrame.java
        └── CuentasFrame.java
```

---

## ⚠ PASO 1 — Descargar el driver SQLite

1. Abre este enlace en tu navegador:
   https://github.com/xerial/sqlite-jdbc/releases/download/3.45.1.0/sqlite-jdbc-3.45.1.0.jar

2. Guarda el archivo descargado en la carpeta **`lib/`** del proyecto.

---

## 🛠 PASO 2 — Abrir en NetBeans

### Opción A: Abrir como proyecto ANT existente
1. **File → Open Project**
2. Navega hasta la carpeta `SistemaInventario/`
3. NetBeans lo detecta automáticamente como proyecto ANT

### Opción B: Crear proyecto nuevo en NetBeans
1. **File → New Project → Java → Java Application with Existing Sources**
2. En "Project Folder" selecciona `SistemaInventario/`
3. En "Source Package Folders" agrega la carpeta `src/`
4. Agrega el JAR de SQLite a las librerías del proyecto:
   - Clic derecho en el proyecto → **Properties → Libraries → Add JAR/Folder**
   - Selecciona `lib/sqlite-jdbc-3.45.1.0.jar`
5. Configura **Main Class** como `ui.MainFrame`

---

## ▶ PASO 3 — Ejecutar

### Con ANT (terminal):
```bash
cd SistemaInventario/
ant run
```

### Con NetBeans:
- Presiona **F6** o **Run → Run Project**

---

## 💾 Base de datos

- La base de datos **`sistema_inventario.db`** se crea automáticamente
  en el directorio donde ejecutes el JAR.
- No necesitas instalar ningún servidor de base de datos.
- Para ver los datos puedes usar [DB Browser for SQLite](https://sqlitebrowser.org/)

---

## 📦 Módulos incluidos

| Módulo         | Funcionalidad                                                     |
|----------------|-------------------------------------------------------------------|
| Inventario     | CRUD. Si el producto existe → suma cantidad. Si no → lo crea     |
| Clientes       | CRUD con manejo de saldo                                          |
| Empleados      | CRUD básico                                                       |
| Facturación    | Factura multi-producto, descuento automático de inventario,       |
|                | abonos, fianzas, formas de pago, cálculo de vueltos              |
| Ventas Diarias | Registro automático al facturar. Filtro por fecha                 |
| Cuentas        | Ingresos, egresos y abonos. Resumen de caja                       |

---

## ✅ Validaciones implementadas

- Campos obligatorios en todos los módulos
- No permite stock negativo en facturación
- ID/NIT único en clientes
- Cantidades numéricas validadas antes de guardar
