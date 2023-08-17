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
public class CreditosModel extends Conexion {

	private Timestamp fecha;
	private int cliente, aval, id, banderin;
	private float pagos, credito, saldo;
	private String estado, consulta, nombreCliente, nombreAval;
	public boolean validar;
	private String[] datos;

	private Statement st;
	private PreparedStatement pst;
	private ResultSet rs;
	private Connection cn;
	public DefaultTableModel tableModel;

	public CreditosModel() {

	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public int getCliente() {
		return cliente;
	}

	public void setCliente(int cliente) {
		this.cliente = cliente;
	}

	public int getAval() {
		return aval;
	}

	public void setAval(int aval) {
		this.aval = aval;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getNombreAval() {
		return nombreAval;
	}

	public void setNombreAval(String nombreAval) {
		this.nombreAval = nombreAval;
	}

	public float getPagos() {
		return pagos;
	}

	public float getCredito() {
		return credito;
	}

	public float getSaldo() {
		return saldo;
	}

	public void validar() {
		if (fecha.equals("")) {
			this.validar = false;
		} else if (cliente == 0) {
			this.validar = false;
		} else {
			this.validar = aval != 0;
		}
	}

	public void guardar() {
		this.validar();
		if (this.validar) {
			this.cn = conexion();
			this.consulta = "INSERT INTO creditos(fecha,cliente,aval,estado) VALUES(?,?,?,?)";
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setTimestamp(1, this.fecha);
				this.pst.setInt(2, this.cliente);
				this.pst.setInt(3, this.aval);
				this.pst.setString(4, this.estado);
				this.banderin = this.pst.executeUpdate();
				if (this.banderin > 0) {
					JOptionPane.showMessageDialog(null, "Cuenta de credito creada con exito.");
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Oops.. error al intentar crear la cuenta de credito. -> " + e);
				e.printStackTrace();
			} finally {
				try {
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(CreditosModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	public void nombreAval() {
		this.cn = conexion();
		this.consulta = "SELECT cl.nombres,cl.apellidos FROM avales AS a INNER JOIN clientes AS cl ON(cl.id = a.cliente) INNER JOIN "
			+ "creditos AS cr ON(cr.aval = a.id) WHERE cr.id = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, this.id);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.nombreAval = this.rs.getString("nombres") + " " + this.rs.getString("apellidos");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Oops.. error al intentar editar cuenta. -> " + e);
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(CreditosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void editar() {
		this.cn = conexion();
		this.consulta = "SELECT c.*,cl.nombres,apellidos FROM creditos AS c INNER JOIN clientes AS cl ON(c.cliente=cl.id) WHERE c.id = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, this.id);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.fecha = this.rs.getTimestamp("fecha");
				this.cliente = this.rs.getInt("cliente");
				this.aval = this.rs.getInt("aval");
				this.estado = this.rs.getString("estado");
				this.nombreCliente = this.rs.getString("nombres") + " " + this.rs.getString("apellidos");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Oops.. error al intentar editar cuenta. -> " + e);
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(CreditosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void actualizar() {
		this.validar();
		if (this.validar) {
			this.cn = conexion();
			this.consulta = "UPDATE creditos SET fecha=?,cliente=?,aval=?,estado=? WHERE id = ?";
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setTimestamp(1, this.fecha);
				this.pst.setInt(2, this.cliente);
				this.pst.setInt(3, this.aval);
				this.pst.setString(4, this.estado);
				this.pst.setInt(5, this.id);
				this.banderin = this.pst.executeUpdate();
				if (this.banderin > 0) {
					JOptionPane.showMessageDialog(null, "Cuenta de credito actualizada con exito.");
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Oops.. error al intentar actualizar la cuenta de credito. -> " + e);
				e.printStackTrace();
			} finally {
				try {
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(CreditosModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	public void isPendiente() {
		this.cn = conexion();
		this.consulta = "SELECT estado FROM creditos WHERE id = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, this.id);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				if (this.rs.getString("estado").equals("Abierto")) {
					this.eliminar();
				} else {
					JOptionPane.showMessageDialog(
						null,
						"Oops.. la cuenta de credito no se puede eliminar porque aun se encuentra pendiente."
					);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(CreditosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void eliminar() {
		this.cn = conexion();
		this.consulta = "DELETE FROM creditos WHERE id = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, this.id);
			this.banderin = this.pst.executeUpdate();
			if (this.banderin > 0) {
				JOptionPane.showMessageDialog(null, "Cuenta eliminada con exito.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(CreditosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void mostrar(String value) {
		this.cn = conexion();
		this.consulta = "SELECT c.id,cl.nombres,cl.apellidos,c.fecha,c.estado FROM creditos AS c INNER JOIN clientes AS cl ON(c.cliente=cl.id)"
			+ " WHERE CONCAT(c.id,cl.nombres) LIKE ? ORDER BY c.id DESC LIMIT 30";
		String[] titulos = {"N° CREDITO", "CLIENTE", "FECHA DE APERTURA", "ESTADO"};
		this.datos = new String[4];
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
				this.datos[0] = this.rs.getString("id");
				this.datos[1] = this.rs.getString("nombres") + " " + this.rs.getString("apellidos");
				this.datos[2] = this.rs.getString("fecha");
				this.datos[3] = this.rs.getString("estado");
				this.tableModel.addRow(datos);
			}
		} catch (Exception e) {
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(CreditosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}

	public void getAvales(String value) {
		this.cn = conexion();
		this.consulta = "SELECT a.id,a.nombres,a.apellidos, a.direccion FROM avales AS a WHERE "
			+ "CONCAT(a.id,a.nombres,a.apellidos) LIKE ? LIMIT 20";
		String[] titulos = {"ID AVAL", "NOMBRE COMPLETO", "Direccion"};
		this.datos = new String[3];
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
				this.datos[0] = this.rs.getString("id");
				this.datos[1] = this.rs.getString("nombres") + " " + this.rs.getString("apellidos");
				this.datos[2] = this.rs.getString("direccion");
				this.tableModel.addRow(datos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(CreditosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void getClientes(String value) {
		this.cn = conexion();
		this.consulta = "SELECT cl.id,nombres,apellidos,dni FROM clientes AS cl LEFT OUTER JOIN creditos as c ON(cl.id=c.cliente)"
			+ " WHERE cl.nombres LIKE ? AND c.cliente IS NULL ORDER BY cl.id DESC LIMIT 20";
		String[] titulos = {"ID", "NOMBRE COMPLETO", "DNI"};
		this.datos = new String[3];
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
				this.datos[0] = this.rs.getString("id");
				this.datos[1] = this.rs.getString("nombres") + " " + this.rs.getString("apellidos");
				this.datos[2] = this.rs.getString("dni");
				this.tableModel.addRow(datos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(CreditosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void datosCredito(int id) {
		this.cn = conexion();
		this.consulta = "CALL credito(?)";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, id);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.pagos = this.rs.getFloat("pagos");
				this.credito = this.rs.getFloat("credito");
				this.saldo = this.credito - this.pagos;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

	}

	public void creditosPendientes(String value) {
		this.cn = conexion();
		int id = 0;
		this.consulta = "SELECT c.id,cl.nombres,apellidos FROM clientes AS cl INNER JOIN creditos AS c ON(cl.id=c.cliente) WHERE CONCAT(c.id,cl.nombres) LIKE ? AND c.estado = 'Pendiente'";
		String[] titulos = {"N. CREDITO", "CLIENTE", "SALDO"};
		this.datos = new String[3];
		this.tableModel = new DefaultTableModel(null, titulos) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setString(1, "%" + value + "%");
			ResultSet rs = this.pst.executeQuery();
			while (rs.next()) {
				id = rs.getInt("id");
				this.datosCredito(id);
				this.datos[0] = String.valueOf(id);
				this.datos[1] = rs.getString("nombres") + " " + rs.getString("apellidos");
				this.datos[2] = String.valueOf(this.saldo);
				this.tableModel.addRow(this.datos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(CreditosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void getHistorialCredito() {
		this.cn = conexion();
		this.consulta = "SELECT"
			+ "	p.id,"
			+ "    p.descripcion,"
			+ "    p.modelo,"
			+ "    d.cantidad,"
			+ "    d.precio,"
			+ "    d.importe,"
			+ "    date_format(dg.fecha,'%d-%m-%Y , %r') AS fechaSalida,"
			+ "    f.id AS factura,"
			+ "    c.id AS comprobante "
			+ "FROM"
			+ "	productos AS p "
			+ "LEFT JOIN"
			+ "	detalles AS d ON(p.id=d.producto) "
			+ "LEFT JOIN"
			+ "	datosgenerales AS dg ON(d.datos = dg.id) "
			+ "LEFT JOIN"
			+ "	factura AS f ON(dg.id=f.datos) "
			+ "LEFT JOIN"
			+ "	comprobante AS c ON(dg.id=c.datos) "
			+ "WHERE dg.credito = ?";
		String[] titulos = {
			"ID. PROD",
			"DESCRIPCION",
			"MODELO",
			"CANT.",
			"PRECIO",
			"IMPORTE",
			"FECHA SALIDA",
			"N. FACT",
			"N. COMP"
		};
		this.tableModel = new DefaultTableModel(null,titulos){
			@Override
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};

		this.datos = new String[9];
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1,this.id);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {				
				System.out.println(this.rs.getString("descripcion"));
				this.datos[0] = this.rs.getString("id");
				this.datos[1] = this.rs.getString("descripcion");
				this.datos[2] = this.rs.getString("modelo");
				this.datos[3] = this.rs.getString("cantidad");
				this.datos[4] = this.rs.getString("precio");
				this.datos[5] = this.rs.getString("importe");
				this.datos[6] = this.rs.getString("fechaSalida");
				this.datos[7] = this.rs.getString("factura");
				this.datos[8] = this.rs.getString("comprobante");
				this.tableModel.addRow(datos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(CreditosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
