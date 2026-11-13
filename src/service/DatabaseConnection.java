package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String DB_FILE = "sistema_inventario.db";
    private static final String URL = "jdbc:sqlite:" + DB_FILE;
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(URL);
                connection.setAutoCommit(true);
                inicializarBaseDatos(connection);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver SQLite no encontrado. Verifica que sqlite-jdbc-3.45.1.0.jar esté en la carpeta lib/", e);
            }
        }
        return connection;
    }

    private static void inicializarBaseDatos(Connection conn) throws SQLException {
        Statement st = conn.createStatement();

        // Tabla Inventario
        st.execute("CREATE TABLE IF NOT EXISTS inventario (" +
                "id_producto    INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre         TEXT    NOT NULL," +
                "categoria      TEXT," +
                "cantidad        INTEGER NOT NULL DEFAULT 0," +
                "precio_unitario REAL    NOT NULL DEFAULT 0," +
                "fecha_ingreso   TEXT    DEFAULT (date('now'))" +
                ")");

        // Tabla Clientes
        st.execute("CREATE TABLE IF NOT EXISTS clientes (" +
                "id_nit          TEXT PRIMARY KEY," +
                "nombre_completo TEXT NOT NULL," +
                "direccion       TEXT," +
                "ciudad          TEXT," +
                "telefono        TEXT," +
                "correo          TEXT," +
                "saldo           REAL DEFAULT 0" +
                ")");

        // Tabla Empleados
        st.execute("CREATE TABLE IF NOT EXISTS empleados (" +
                "codigo_empleado     INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre_completo     TEXT NOT NULL," +
                "cargo               TEXT," +
                "documento_identidad TEXT" +
                ")");

        // Tabla Facturas
        st.execute("CREATE TABLE IF NOT EXISTS facturas (" +
                "id_factura      INTEGER PRIMARY KEY AUTOINCREMENT," +
                "fecha           TEXT    DEFAULT (date('now'))," +
                "id_cajero       INTEGER," +
                "nombre_cajero   TEXT," +
                "cliente_id_nit  TEXT," +
                "forma_pago      TEXT," +
                "domicilio       REAL DEFAULT 0," +
                "descuento       REAL DEFAULT 0," +
                "total           REAL DEFAULT 0," +
                "dinero_recibido REAL DEFAULT 0," +
                "vueltos         REAL DEFAULT 0," +
                "abono_saldo     REAL DEFAULT 0," +
                "fianza          REAL DEFAULT 0," +
                "saldo_nuevo     REAL DEFAULT 0" +
                ")");

        // Tabla Detalle Factura
        st.execute("CREATE TABLE IF NOT EXISTS detalle_factura (" +
                "id_detalle          INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_factura          INTEGER NOT NULL," +
                "id_producto         INTEGER," +
                "descripcion_articulo TEXT," +
                "cantidad            INTEGER NOT NULL DEFAULT 0," +
                "valor_unitario      REAL    NOT NULL DEFAULT 0," +
                "importe             REAL    NOT NULL DEFAULT 0," +
                "FOREIGN KEY (id_factura) REFERENCES facturas(id_factura)" +
                ")");

        // Tabla Ventas Diarias
        st.execute("CREATE TABLE IF NOT EXISTS ventas_diarias (" +
                "id               INTEGER PRIMARY KEY AUTOINCREMENT," +
                "fecha            TEXT DEFAULT (date('now'))," +
                "id_producto      INTEGER," +
                "nombre           TEXT," +
                "cantidad_vendida INTEGER DEFAULT 0," +
                "precio_unitario  REAL    DEFAULT 0" +
                ")");

        // Tabla Cuentas
        st.execute("CREATE TABLE IF NOT EXISTS cuentas (" +
                "id          INTEGER PRIMARY KEY AUTOINCREMENT," +
                "fecha       TEXT DEFAULT (date('now'))," +
                "cliente_id  TEXT," +
                "metodo_pago TEXT," +
                "total_venta REAL DEFAULT 0," +
                "fianza      REAL DEFAULT 0," +
                "empleado    TEXT," +
                "tipo        TEXT" +
                ")");

        st.close();
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
