package dao;

import model.Cliente;
import service.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nombre_completo";
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public Cliente buscarPorId(String idNit) {
        String sql = "SELECT * FROM clientes WHERE id_nit = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idNit);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean guardar(Cliente cl) {
        String sql = "INSERT INTO clientes (id_nit, nombre_completo, direccion, ciudad, telefono, correo, saldo) VALUES (?,?,?,?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cl.getIdNit());
            ps.setString(2, cl.getNombreCompleto());
            ps.setString(3, cl.getDireccion());
            ps.setString(4, cl.getCiudad());
            ps.setString(5, cl.getTelefono());
            ps.setString(6, cl.getCorreo());
            ps.setDouble(7, cl.getSaldo());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean actualizar(Cliente cl) {
        String sql = "UPDATE clientes SET nombre_completo=?, direccion=?, ciudad=?, telefono=?, correo=?, saldo=? WHERE id_nit=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cl.getNombreCompleto());
            ps.setString(2, cl.getDireccion());
            ps.setString(3, cl.getCiudad());
            ps.setString(4, cl.getTelefono());
            ps.setString(5, cl.getCorreo());
            ps.setDouble(6, cl.getSaldo());
            ps.setString(7, cl.getIdNit());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean eliminar(String idNit) {
        String sql = "DELETE FROM clientes WHERE id_nit = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idNit);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean actualizarSaldo(String idNit, double nuevoSaldo) {
        String sql = "UPDATE clientes SET saldo = ? WHERE id_nit = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, nuevoSaldo);
            ps.setString(2, idNit);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getString("id_nit"),
                rs.getString("nombre_completo"),
                rs.getString("direccion"),
                rs.getString("ciudad"),
                rs.getString("telefono"),
                rs.getString("correo"),
                rs.getDouble("saldo")
        );
    }
}
