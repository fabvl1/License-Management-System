package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.User;
import utils.ConnectionManager;

public class UserService implements EntityService<User>  {

	@Override
	public List<User> getAll() {

		List<User> lista = new ArrayList<>();
		String sql = "SELECT name_user , password , rols FROM \"user\" u JOIN \"rols\" r ON u.name_User = r.name_user_rols";
		Connection conn;
		 
		try {
			conn = ConnectionManager.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql);
		      ResultSet result = pstmt.executeQuery();
		      while(result.next()) {
		        	lista.add(mapResultSetTouSER(result));
		        }
		} catch (SQLException e) {
			 handleSQLException("Error retrieving licenses", e);
			 
		}
       
        
        return lista;
	}

	@Override
	public boolean create(User user) {
	    String sqlUser = "INSERT INTO \"user\" (name_user, password) VALUES (?, ?)";
	    String sqlRol = "INSERT INTO \"rols\" (name_user_rols, rols) VALUES (?, ?)";

	    try (Connection conn = ConnectionManager.getConnection();
	         PreparedStatement pstmtUser = conn.prepareStatement(sqlUser);
	         PreparedStatement pstmtRol = conn.prepareStatement(sqlRol)) {

	        // Insertar en tabla user
	        pstmtUser.setString(1, user.getNombre());
	        pstmtUser.setString(2, user.getContra());

	        // Insertar en tabla rols
	        pstmtRol.setString(1, user.getNombre());
	        pstmtRol.setString(2, user.getRol());

	        return pstmtUser.executeUpdate() > 0 && pstmtRol.executeUpdate() > 0;

	    } catch (SQLException e) {
	        handleSQLException("Error creating user", e);
	        return false;
	    }
	}



	@Override
	public boolean update(User user) {
		String sql = "UPDATE user SET "
                + "name_user = ?,name_user_rol = ? "
                + "WHERE exam_code = ?";

     try (Connection conn = ConnectionManager.getConnection();
          PreparedStatement pstmt = conn.prepareStatement(sql)) {

    	 setUserParameters(pstmt, user);
         pstmt.setString(3, user.getNombre());

         return pstmt.executeUpdate() > 0;

     } catch (SQLException e) {
         handleSQLException("Error updating user", e);
         return false;
     }
	}

	@Override
	public boolean delete(String id) {
		String sql = "DELETE FROM user WHERE name_user = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            handleSQLException("Error deleting user", e);
            return false;
        }
    }

	@Override
	public User getById(String id) {
		 String sql = "SELECT * FROM license WHERE license_code = ?";
		 User user = new User();
	        
	        try (Connection conn = ConnectionManager.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            
	            pstmt.setString(1, id);
	            try (ResultSet rs = pstmt.executeQuery()) {
	                if (rs.next()) {
	                	user = mapResultSetTouSER(rs);
	                }
	            }
	        } catch (SQLException e) {
	            handleSQLException("Error retrieving license", e);
	        }
	        return user;
	    }


	public boolean autenticar(String nombre,String contra) {
		String sql = "SELECT Name_user , password , Rols FROM \"user\" u JOIN \"rols\" r ON u.Name_User = r.Name_User_rols WHERE name_user = ? AND password = ?";
		Connection conn;
		 boolean devuelto = false;
		try {
			conn = ConnectionManager.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, nombre);
			 pstmt.setString(2, contra);
		      ResultSet result = pstmt.executeQuery();
		      devuelto = result.next();
		     
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return devuelto;
	}
	
	public String getRolPorUsuario(String nombreUsuario) {
	    String rol = null;
	    String sql = "SELECT r.rols FROM \"user\" u " +
	                 "JOIN \"rols\" r ON u.name_user = r.name_user_rols " +
	                 "WHERE u.name_user = ?";

	    try (Connection conn = ConnectionManager.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, nombreUsuario);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            rol = rs.getString("rols");
	        }

	    } catch (SQLException e) {
	        handleSQLException("Error retrieving user role", e);
	    }

	    return rol;
	}
	
	private void setUserParameters(PreparedStatement pstmt, User user) throws SQLException {
        pstmt.setString(1, user.getNombre());
        pstmt.setString(2, user.getContra());    
    }
	

	private void handleSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }
	
	private User mapResultSetTouSER(ResultSet rs) throws SQLException {
        User user = new User();
        user.setNombre(rs.getString("name_user"));
        user.setContra(rs.getString("password"));
        user.setRol(rs.getString("rols"));
       
        return user;
    }
	
	public boolean exist(String nombreUsuario) {
	    String sql = "SELECT 1 FROM \"user\" WHERE name_user = ?";
	    try (Connection conn = ConnectionManager.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, nombreUsuario);
	        ResultSet rs = pstmt.executeQuery();

	        return rs.next(); // Devuelve true si encuentra un resultado

	    } catch (SQLException e) {
	        handleSQLException("Error verificando existencia del usuario", e);
	        return false; // o true, dependiendo de si prefieres prevenir inserciones si hay error
	    }
	}

}
