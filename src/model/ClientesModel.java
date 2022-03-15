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
public class ClientesModel extends Conexion {

	private int id;
	private String nombres, apellidos, direccionExacta, lugarTrabajo, municipio, departamento, barrio, telefono, dni;
	public boolean validar;

	Connection cn;
	Statement st;
	PreparedStatement pst;
	ResultSet rs;
	public DefaultTableModel tableModel;
	private String[] datos;
	private String consulta;
	private int banderin;

	public ClientesModel() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDireccionExacta() {
		return direccionExacta;
	}

	public void setDireccionExacta(String direccionExacta) {
		this.direccionExacta = direccionExacta;
	}

	public String getLugarTrabajo() {
		return lugarTrabajo;
	}

	public void setLugarTrabajo(String lugarTrabajo) {
		this.lugarTrabajo = lugarTrabajo;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getMunicipio() {
		return municipio;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getBarrio() {
		return barrio;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public void validar() {
		if (this.nombres.equals("")) {
			this.validar = false;
		} else if (this.apellidos.equals("")) {
			this.validar = false;
		} else if (this.departamento.equals("")) {
			this.validar = false;
		} else if (this.municipio.equals("")) {
			this.validar = false;
		} else if (this.barrio.equals("")) {
			this.validar = false;
		} else {
			this.validar = !this.direccionExacta.equals("");
		}
	}

	public void guardar() {
		this.validar();
		if (this.validar) {
			this.cn = conexion();
			this.consulta = "INSERT INTO clientes(nombres,apellidos,dni,direccion,departamento,municipio,barrio,lugarTrabajo,telefono)"
				+ " VALUES(?,?,?,?,?,?,?,?,?)";
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setString(1, this.nombres);
				this.pst.setString(2, this.apellidos);
				this.pst.setString(3, this.dni);
				this.pst.setString(4, this.direccionExacta);
				this.pst.setString(5, this.departamento);
				this.pst.setString(6, this.municipio);
				this.pst.setString(7, this.barrio);
				this.pst.setString(8, this.lugarTrabajo);
				this.pst.setString(9, this.telefono);
				this.banderin = this.pst.executeUpdate();
				if (this.banderin > 0) {
					JOptionPane.showMessageDialog(null, "Cliente guardado con exito.");
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Oops.. error al intentar guardar el cliente. -> " + e);
				e.printStackTrace();
			} finally {
				try {
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(ClientesModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	//TODO hablar el asunto de avales con lemark si lo hacemos con llave foranea de clientes o asi separado.
	public void guardarAval(){
		this.cn = conexion();
		this.consulta = "";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			
		} catch (Exception e) {
		}
	}

	public void editar() {
		this.cn = conexion();
		this.consulta = "SELECT * FROM clientes WHERE id = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, this.id);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.nombres = this.rs.getString("nombres");
				this.apellidos = this.rs.getString("apellidos");
				this.dni = this.rs.getString("dni");
				this.direccionExacta = this.rs.getString("direccion");
				this.departamento = this.rs.getString("departamento");
				this.municipio = this.rs.getString("municipio");
				this.barrio = this.rs.getString("barrio");
				this.lugarTrabajo = this.rs.getString("lugarTrabajo");
				this.telefono = this.rs.getString("telefono");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Oops.. error al intentar editar cliente.");
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(ClientesModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void actualizar() {
		this.validar();
		if (this.validar) {
			this.cn = conexion();
			this.consulta = "UPDATE clientes SET nombres = ?,apellidos=?,dni=?,direccion=?,departamento=?,municipio=?,barrio=?,"
				+ "lugarTrabajo=?,telefono=? WHERE id = ?";
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setString(1, this.nombres);
				this.pst.setString(2, this.apellidos);
				this.pst.setString(3, this.dni);
				this.pst.setString(4, this.direccionExacta);
				this.pst.setString(5, this.departamento);
				this.pst.setString(6, this.municipio);
				this.pst.setString(7, this.barrio);
				this.pst.setString(8, this.lugarTrabajo);
				this.pst.setString(9, this.telefono);
				this.pst.setInt(10, this.id);
				this.banderin = this.pst.executeUpdate();
				if (this.banderin > 0) {
					JOptionPane.showMessageDialog(null, "Cliente actualizado con exito.");
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Oops.. error al intentar actualizar el cliente. -> " + e);
				e.printStackTrace();
			} finally {
				try {
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(ClientesModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	public void mostrar(String value){
		this.cn = conexion();
		this.consulta = "SELECT * FROM clientes WHERE CONCAT(nombres,apellidos,departamento,municipio,barrio) LIKE ? "
			+ "ORDER BY nombres ASC LIMIT 100";
		String[] titulos = {"ID","NOMBRES","APELLIDOS","DNI","DIRECCION EXACTA","DEPARTAMENTO","MUNICIPIO","BARRIO","LUGAR TRAB.","TELFONO"};
		this.datos = new String[10];
		this.tableModel = new DefaultTableModel(null,titulos){
			@Override
			public boolean isCellEditable(int row,int col){
				return false;
			}
		};
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setString(1, "%"+value+"%");
			this.rs = this.pst.executeQuery();
			while(this.rs.next()){
				this.datos[0] = this.rs.getString("id");
				this.datos[1] = this.rs.getString("nombres");
				this.datos[2] = this.rs.getString("apellidos");
				this.datos[3] = this.rs.getString("dni");
				this.datos[4] = this.rs.getString("direccion");
				this.datos[5] = this.rs.getString("departamento");
				this.datos[6] = this.rs.getString("municipio");
				this.datos[7] = this.rs.getString("barrio");
				this.datos[8] = this.rs.getString("lugarTrabajo");
				this.datos[9] = this.rs.getString("telefono");
				this.tableModel.addRow(datos);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(ClientesModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
