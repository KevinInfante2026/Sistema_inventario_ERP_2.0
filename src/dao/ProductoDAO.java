package dao;

import model.Producto;
import service.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM inventario ORDER BY id_producto";
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public Producto buscarPorId(int id) {
        String sql = "SELECT * FROM inventario WHERE id_producto = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public Producto buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM inventario WHERE LOWER(nombre) = LOWER(?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    /** Si el producto ya existe (por nombre) suma la cantidad; si no, lo crea */
    public boolean guardarOActualizar(Producto p) {
        Producto existente = buscarPorNombre(p.getNombre());
        if (existente != null) {
            // Sumar cantidad
            String sql = "UPDATE inventario SET cantidad = cantidad + ?, precio_unitario = ?, categoria = ? WHERE id_producto = ?";
            try (Connection c = DatabaseConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, p.getCantidadDisponible());
                ps.setDouble(2, p.getPrecioUnitario());
                ps.setString(3, p.getCategoria());
                ps.setInt(4, existente.getIdProducto());
                ps.executeUpdate();
                return true;
            } catch (SQLException e) { e.printStackTrace(); }
        } else {
            // Insertar nuevo
            String sql = "INSERT INTO inventario (nombre, categoria, cantidad, precio_unitario, fecha_ingreso) VALUES (?,?,?,?,?)";
            try (Connection c = DatabaseConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, p.getNombre());
                ps.setString(2, p.getCategoria());
                ps.setInt(3, p.getCantidadDisponible());
                ps.setDouble(4, p.getPrecioUnitario());
                ps.setString(5, p.getFechaIngreso() != null ? p.getFechaIngreso() :
                        new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
                ps.executeUpdate();
                return true;
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }

    public boolean actualizar(Producto p) {
        String sql = "UPDATE inventario SET nombre=?, categoria=?, cantidad=?, precio_unitario=? WHERE id_producto=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCategoria());
            ps.setInt(3, p.getCantidadDisponible());
            ps.setDouble(4, p.getPrecioUnitario());
            ps.setInt(5, p.getIdProducto());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM inventario WHERE id_producto = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    /** Descuenta stock. Retorna false si no hay suficiente. */
    public boolean descontarStock(int idProducto, int cantidad) {
        Producto p = buscarPorId(idProducto);
        if (p == null || p.getCantidadDisponible() < cantidad) return false;
        String sql = "UPDATE inventario SET cantidad = cantidad - ? WHERE id_producto = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        return new Producto(
                rs.getInt("id_producto"),
                rs.getString("nombre"),
                rs.getString("categoria"),
                rs.getInt("cantidad"),
                rs.getDouble("precio_unitario"),
                rs.getString("fecha_ingreso")
        );
    }
}
