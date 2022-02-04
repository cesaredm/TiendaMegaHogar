/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class LoginModel extends Conexion {

	private String usuario, password, permiso;
	private int empleado;
	public boolean validar;
	PreparedStatement pst;
	Statement st;
	ResultSet rs;
	Connection cn;
	String consulta;

	public LoginModel() {

	}

	public String getUser() {
		return usuario;
	}

	public void setUser(String user) {
		this.usuario = user;
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

	public int getEmpleado() {
		return empleado;
	}

	public void setEmpleado(int empleado) {
		this.empleado = empleado;
	}

	public void validar() {
		this.cn = conexion();
		this.consulta = "SELECT * FROM usuarios WHERE usuario = ? AND password = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setString(1, this.usuario);
			this.pst.setString(2, this.password);
			this.rs = this.pst.executeQuery();
			if (!this.rs.isBeforeFirst()) {
				this.validar = false;
				JOptionPane.showMessageDialog(null, "El usuario no existe");
			} else {
				this.validar = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
