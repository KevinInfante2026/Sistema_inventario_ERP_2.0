-- ============================================================
-- SISTEMA DE INVENTARIO - Script SQL (SQLite)
-- Ejecutar solo como referencia. La app crea las tablas
-- automáticamente al iniciar.
-- ============================================================

CREATE TABLE IF NOT EXISTS inventario (
    id_producto     INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre          TEXT    NOT NULL,
    categoria       TEXT,
    cantidad        INTEGER NOT NULL DEFAULT 0,
    precio_unitario REAL    NOT NULL DEFAULT 0,
    fecha_ingreso   TEXT    DEFAULT (date('now'))
);

CREATE TABLE IF NOT EXISTS clientes (
    id_nit          TEXT PRIMARY KEY,
    nombre_completo TEXT NOT NULL,
    direccion       TEXT,
    ciudad          TEXT,
    telefono        TEXT,
    correo          TEXT,
    saldo           REAL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS empleados (
    codigo_empleado     INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_completo     TEXT NOT NULL,
    cargo               TEXT,
    documento_identidad TEXT
);

CREATE TABLE IF NOT EXISTS facturas (
    id_factura      INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha           TEXT    DEFAULT (date('now')),
    id_cajero       INTEGER,
    nombre_cajero   TEXT,
    cliente_id_nit  TEXT,
    forma_pago      TEXT,
    domicilio       REAL DEFAULT 0,
    descuento       REAL DEFAULT 0,
    total           REAL DEFAULT 0,
    dinero_recibido REAL DEFAULT 0,
    vueltos         REAL DEFAULT 0,
    abono_saldo     REAL DEFAULT 0,
    fianza          REAL DEFAULT 0,
    saldo_nuevo     REAL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS detalle_factura (
    id_detalle          INTEGER PRIMARY KEY AUTOINCREMENT,
    id_factura          INTEGER NOT NULL,
    id_producto         INTEGER,
    descripcion_articulo TEXT,
    cantidad            INTEGER NOT NULL DEFAULT 0,
    valor_unitario      REAL    NOT NULL DEFAULT 0,
    importe             REAL    NOT NULL DEFAULT 0,
    FOREIGN KEY (id_factura) REFERENCES facturas(id_factura)
);

CREATE TABLE IF NOT EXISTS ventas_diarias (
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha            TEXT DEFAULT (date('now')),
    id_producto      INTEGER,
    nombre           TEXT,
    cantidad_vendida INTEGER DEFAULT 0,
    precio_unitario  REAL    DEFAULT 0
);

CREATE TABLE IF NOT EXISTS cuentas (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha       TEXT DEFAULT (date('now')),
    cliente_id  TEXT,
    metodo_pago TEXT,
    total_venta REAL DEFAULT 0,
    fianza      REAL DEFAULT 0,
    empleado    TEXT,
    tipo        TEXT   -- INGRESO | EGRESO | ABONO
);
