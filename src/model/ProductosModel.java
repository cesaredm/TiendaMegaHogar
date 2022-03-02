/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class ProductosModel extends Conexion {

	private int id, marca, banderin;
	private float precioCosto,
		precioVenta,
		stock;
	private String descripcion,
		nombreMarca,
		codigoBarra,
		modelo,
		consulta;
	private String[] datos;
	public boolean validar;

	ResultSet rs;
	Connection cn;
	PreparedStatement pst;
	Statement st;
	public DefaultTableModel tableModelProductos, tableModelMarcas;
	public DefaultComboBoxModel comboModel;

	public void ProductosModel() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMarca() {
		return marca;
	}

	public void setMarca(int marca) {
		this.marca = marca;
	}

	public float getPrecioCosto() {
		return precioCosto;
	}

	public void setPrecioCosto(float precioCosto) {
		this.precioCosto = precioCosto;
	}

	public float getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(float precioVenta) {
		this.precioVenta = precioVenta;
	}

	public float getStock() {
		return stock;
	}

	public void setStock(float stock) {
		this.stock = stock;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombreMarca() {
		return nombreMarca;
	}

	public void setNombreMarca(String nombreMarca) {
		this.nombreMarca = nombreMarca;
	}

	public String getCodigoBarra() {
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public void validar() {
		if (this.descripcion.equals("")) {
			this.validar = false;
		} else {
			this.validar = true;
		}
	}

	public void guardar() {
		this.validar();
		if (this.validar) {
			this.cn = conexion();
			this.consulta = "INSERT INTO productos(descripcion,codigoBarra,modelo,marca,precioCosto,precioVenta,stock)"
				+ " VALUES(?,?,?,?,?,?,?)";
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setString(1, this.descripcion);
				this.pst.setString(2, this.codigoBarra);
				this.pst.setString(3, this.modelo);
				this.pst.setInt(4, this.marca);
				this.pst.setFloat(5, this.precioCosto);
				this.pst.setFloat(6, this.precioVenta);
				this.pst.setFloat(7, this.stock);
				this.banderin = this.pst.executeUpdate();
				if (this.banderin > 0) {
					JOptionPane.showMessageDialog(null, "Producto " + this.descripcion + " guardado con esxito.");
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Oops.. error al intentar guardar producto. -> " + e);
				e.printStackTrace();
			} finally {
				try {
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(ProductosModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	public void editar() {
		this.cn = conexion();
		this.consulta = "SELECT * FROM productos WHERE id = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, this.id);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.descripcion = this.rs.getString("descripcion");
				this.codigoBarra = this.rs.getString("codigoBarra");
				this.modelo = this.rs.getString("modelo");
				this.precioCosto = this.rs.getFloat("precioCosto");
				this.precioVenta = this.rs.getFloat("precioVenta");
				this.marca = this.rs.getInt("marca");
				this.stock = this.rs.getFloat("stock");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Oops.. error al intentar editar producto. -> " + e);
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(ProductosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void actualizar() {
		this.validar();
		if (this.validar) {
			this.cn = conexion();
			this.consulta = "UPDATE productos SET descripcion = ?, codigoBarra = ?, modelo = ?, marca = ?, precioCosto = ?, precioVenta = ?,"
				+ "stock = ? WHERE id = ?";
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setString(1, this.descripcion);
				this.pst.setString(2, this.codigoBarra);
				this.pst.setString(3, this.modelo);
				this.pst.setInt(4, this.marca);
				this.pst.setFloat(5, this.precioCosto);
				this.pst.setFloat(6, this.precioVenta);
				this.pst.setFloat(7, this.stock);
				this.pst.setInt(8, this.id);
				this.banderin = this.pst.executeUpdate();
				if (this.banderin > 0) {
					JOptionPane.showMessageDialog(null, "Producto " + this.descripcion + " actualizado con esxito.");
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Oops.. error al intentar Actualizar producto. -> " + e);
				e.printStackTrace();
			} finally {
				try {
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(ProductosModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	public void eliminar() {
		this.cn = conexion();
		this.consulta = "UPDATE productos SET estado = 0 WHERE id = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, this.id);
			this.banderin = this.pst.executeUpdate();
			if(this.banderin > 0){
				JOptionPane.showMessageDialog(null, "Producto eliminado con exito.");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Oops.. error al intentar eliminar producto. -> "+ e);
			e.printStackTrace();
		}finally{
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(ProductosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void mostrar(String value) {
		this.cn = conexion();
		this.consulta = "SELECT p.*, m.nombre FROM productos AS p INNER JOIN marca AS m ON(p.marca=m.id) WHERE "
			+ "CONCAT(p.descripcion,p.codigoBarra,m.nombre) LIKE ? AND p.estado = 1 ORDER BY p.id DESC LIMIT 30";
		this.datos = new String[8];
		String[] titulos = {"ID", "COD. BARRA", "DESCRIPCION", "MARCA", "MODELO", "P. COSTO", "P. VENTA", "INVENTARIO"};
		this.tableModelProductos = new DefaultTableModel(null, titulos) {
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
				this.datos[0] = this.rs.getString("id");
				this.datos[1] = this.rs.getString("codigoBarra");
				this.datos[2] = this.rs.getString("descripcion");
				this.datos[3] = this.rs.getString("nombre");
				this.datos[4] = this.rs.getString("modelo");
				this.datos[5] = this.rs.getString("precioCosto");
				this.datos[6] = this.rs.getString("precioVenta");
				this.datos[7] = this.rs.getString("stock");
				this.tableModelProductos.addRow(datos);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(ProductosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void getMarcas() {
		this.cn = conexion();
		this.consulta = "SELECT * FROM marca ORDER BY nombre ASC";
		this.comboModel = new DefaultComboBoxModel();
		try {
			this.st = this.cn.createStatement();
			this.rs = this.st.executeQuery(this.consulta);
			while (this.rs.next()) {
				this.comboModel.addElement(new CmbMarcas(this.rs.getInt("id"), this.rs.getString("nombre")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(ProductosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void agregarInventario(float cantidad){
		this.cn = conexion();
		this.consulta = "UPDATE productos SET stock = stock + ? WHERE id = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setFloat(1, cantidad);
			this.pst.setInt(2, this.id);
			this.banderin = this.pst.executeUpdate();
			if(this.banderin > 0){
				JOptionPane.showMessageDialog(null, "Inventario agrgado con exito.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(ProductosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
