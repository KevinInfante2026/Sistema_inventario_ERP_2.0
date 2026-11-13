package dao;

import model.VentaDiaria;
import service.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDiariaDAO {

    public boolean guardar(VentaDiaria v) {
        String sql = "INSERT INTO ventas_diarias (fecha, id_producto, nombre, cantidad_vendida, precio_unitario) VALUES (?,?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, v.getFecha());
            ps.setInt(2, v.getIdProducto());
            ps.setString(3, v.getNombre());
            ps.setInt(4, v.getCantidadVendida());
            ps.setDouble(5, v.getPrecioUnitario());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public List<VentaDiaria> listarPorFecha(String fecha) {
        List<VentaDiaria> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas_diarias WHERE fecha = ? ORDER BY id";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, fecha);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<VentaDiaria> listarTodos() {
        List<VentaDiaria> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas_diarias ORDER BY fecha DESC, id DESC";
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    private VentaDiaria mapear(ResultSet rs) throws SQLException {
        VentaDiaria v = new VentaDiaria();
        v.setId(rs.getInt("id"));
        v.setFecha(rs.getString("fecha"));
        v.setIdProducto(rs.getInt("id_producto"));
        v.setNombre(rs.getString("nombre"));
        v.setCantidadVendida(rs.getInt("cantidad_vendida"));
        v.setPrecioUnitario(rs.getDouble("precio_unitario"));
        return v;
    }
}
