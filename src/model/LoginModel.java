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
			} else {
				this.validar = true;
				this.getDatosUsuario();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void getDatosUsuario(){
		this.cn = conexion();
		this.consulta = "SELECT * FROM usuarios WHERE usuario = ? AND password = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setString(1, this.usuario);
			this.pst.setString(2, this.password);
			this.rs = this.pst.executeQuery();
			while(this.rs.next()){
				this.usuario = this.rs.getString("usuario");
				this.empleado = this.rs.getInt("empleado");
				this.permiso = this.rs.getString("permiso");
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "ERROR : en el metodo getDatosUsuario en el modelo login -> " + e);
			e.printStackTrace();
		}finally{
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
