/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class UsuariosModel extends Conexion {

	private int empleado, idUsuario, banderin;
	private String usuario,
		password, permiso, nombreEmpleado;
	private PreparedStatement pst;
	private ResultSet rs;
	private Connection cn;
	private String consulta;
	private String[] listUsuarios;
	DefaultTableModel tableModel,tableModelEmpleados;

	public UsuariosModel() {
	}

	public int getEmpleado() {
		return empleado;
	}

	public void setEmpleado(int empleado) {
		this.empleado = empleado;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPermiso() {
		return permiso;
	}

	public void setPermiso(String permiso) {
		this.permiso = permiso;
	}

	public String getNombreEmpleado() {
		return nombreEmpleado;
	}

	public void setNombreEmpleado(String nombreEmpleado) {
		this.nombreEmpleado = nombreEmpleado;
	}

	public DefaultTableModel getTableMode() {
		return tableModel;
	}

	public void setTableMode(DefaultTableModel tableMode) {
		this.tableModel = tableMode;
	}

	public DefaultTableModel getTableModelEmpleados() {
		return tableModelEmpleados;
	}

	public void setTableModelEmpleados(DefaultTableModel tableModelEmpleados) {
		this.tableModelEmpleados = tableModelEmpleados;
	}

	public void guardar() {
		this.cn = conexion();
		this.consulta = "INSERT INTO usuarios(empleado,usuario,password,permiso) VALUES(?,?,?,?)";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, empleado);
			this.pst.setString(2, usuario);
			this.pst.setString(3, password);
			this.pst.setString(4, permiso);
			this.banderin = this.pst.executeUpdate();
			if (this.banderin > 0) {
				JOptionPane.showMessageDialog(null, "Usuario guardado con exito.");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Oops.. ocurrio un error al intentar agregar el usuario. -> " + e);
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(UsuariosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void editar() {
		this.cn = conexion();
		this.consulta = "SELECT u.usuario,password,permiso,e.id,nombres,apellidos FROM usuarios AS u INNER JOIN empleados AS e "
			+ "ON(u.empleado=e.id) WHERE u.id = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, idUsuario);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.empleado = this.rs.getInt("id");
				this.nombreEmpleado = this.rs.getString("nombres") + " " + this.rs.getString("apellidos");
				this.usuario = this.rs.getString("usuario");
				this.password = this.rs.getString("password");
				this.permiso = this.rs.getString("permiso");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Oops.. ocurrio un error al intentar editar este usuario. -> " + e);
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(UsuariosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void actualizar() {
		this.cn = conexion();
		this.consulta = "UPDATE usuarios SET empleado = ?, usuario = ?, password = ?, permiso = ? WHERE id = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, empleado);
			this.pst.setString(2, usuario);
			this.pst.setString(3, password);
			this.pst.setString(4, permiso);
			this.pst.setInt(5, idUsuario);
			this.banderin = this.pst.executeUpdate();
			if (this.banderin > 0) {
				JOptionPane.showMessageDialog(null, "Usuario actualizado con exito.");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Oops.. ocurrio un error al intentar actualizar el usuario. -> " + e);
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(UsuariosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void eliminar() {
		this.cn = conexion();
		this.consulta = "DELETE FROM usuarios WHERE id = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, idUsuario);
			this.banderin = this.pst.executeUpdate();
			if (this.banderin > 0) {
				JOptionPane.showMessageDialog(null, "Usuario eliminado con exito.");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Oops.. error al intentar eliminar el usuario -> "+e);
			e.printStackTrace();
		}finally{
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(UsuariosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void mostrar(String value) {
		this.cn = conexion();
		this.consulta = "SELECT u.id,usuario,password,permiso,e.nombres,apellidos FROM usuarios AS u INNER JOIN empleados"
			+ " AS e ON(u.empleado=e.id) WHERE CONCAT(e.nombres,e.apellidos,u.usuario) LIKE ?";
		this.listUsuarios = new String[5];
		String[] titulos = {"ID", "NOMBRE DE USUARIO", "PERMISO", "EMPLEADO"};
		this.tableModel = new DefaultTableModel(null, titulos) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setString(1, "%" + value + "%");
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.listUsuarios[0] = this.rs.getString("id");
				this.listUsuarios[1] = this.rs.getString("usuario");
				this.listUsuarios[2] = this.rs.getString("permiso");
				this.listUsuarios[3] = this.rs.getString("nombres") + " " + this.rs.getString("apellidos");
				this.tableModel.addRow(this.listUsuarios);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(UsuariosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void empleados(String value){
		this.cn = conexion();
		this.consulta = "SELECT id,nombres,apellidos FROM empleados WHERE CONCAT(id,nombres,apellidos) LIKE ? ORDER BY id DESC";
		this.listUsuarios = new String[3];
		String[] titulos = {"ID", "NOMBRES", "APELLIDOS"};
		this.tableModelEmpleados = new DefaultTableModel(null, titulos) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setString(1, "%" + value + "%");
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.listUsuarios[0] = this.rs.getString("id");
				this.listUsuarios[1] = this.rs.getString("nombres");
				this.listUsuarios[2] = this.rs.getString("apellidos");
				this.tableModelEmpleados.addRow(this.listUsuarios);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(UsuariosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
