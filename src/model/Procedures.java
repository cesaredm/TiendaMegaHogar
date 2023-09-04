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
public class Procedures extends Conexion {

	public static boolean response = true;
	private static PreparedStatement pst;
	private static ResultSet rs;
	private static Connection cn;

	private Procedures() {

	}

	/*esta funcion no se esta usando*/
	public static boolean venderCodigoBarra(String codigo, float cantidad) {
		cn = conexion();
		try {
			pst = cn.prepareStatement("CALL venderCodigoBarra(?,?)");
			pst.setString(1, codigo);
			pst.setFloat(2, cantidad);
			rs = pst.executeQuery();
			while (rs.next()) {
				if (rs.getString("message").equals("exito")) {
					response = true;
				} else {
					response = false;
					JOptionPane.showMessageDialog(null, rs.getString("message"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return response;
	}

	public static boolean venderId(int id, float cantidad, Connection cn) throws SQLException {
		/* 
			Recibe un id de producto la cantidad y una conexion para deminuir el stock en sincronia
			con la transaccion de la funcion que lo llama
		 */
		pst = cn.prepareStatement("CALL venderId(?,?)");
		pst.setInt(1, id);
		pst.setFloat(2, cantidad);
		rs = pst.executeQuery();
		while (rs.next()) {
			if (rs.getString("message").equals("exito")) {
				response = true;
			} else {
				response = false;
				JOptionPane.showMessageDialog(null, rs.getString("message"));
			}
		}
		return response;
	}

	public static float verificarInventario(int id, String barras){
		String consulta = "";		
		float inventario = 0;
		consulta = id == 0 ? "SELECT stock FROM productos WHERE codigoBarras = ?" : "SELECT stock FROM productos WHERE id = ?";
		try {
			cn = conexion();	
			pst = cn.prepareStatement(consulta);
			if (id==0) {
				pst.setString(1,barras);
			} else {
				pst.setInt(1,id);
			}
			rs = pst.executeQuery();
			if (rs.next()) {
				inventario = rs.getFloat(1);
			}
		} catch(SQLException err){
			err.printStackTrace();
		}
		return inventario;
	}
	public static void agregarInventario(int producto, float cantidad) {
		cn = conexion();
		try {
			pst = cn.prepareStatement("CALL agregarInventario(?,?)");
			pst.setInt(1, producto);
			pst.setFloat(2, cantidad);
			pst.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public static void cambiarEstadoCredito(int credito) {
		cn = conexion();
		String consulta = "";
		float deuda = 0, pagos = 0, saldo = 0;
		try {
			pst = cn.prepareStatement("CALL credito(?)");
			pst.setInt(1, credito);
			rs = pst.executeQuery();
			while (rs.next()) {
				deuda = rs.getFloat("credito");
				pagos = rs.getFloat("pagos");
			}
			saldo = deuda - pagos;
			if (saldo == 0) {
				consulta = "UPDATE creditos SET estado = 'Abierto' WHERE id = ?";
			} else if (saldo > 0) {
				consulta = "UPDATE creditos SET estado = 'Pendiente' WHERE id = ?";
			}
			pst = cn.prepareStatement(consulta);
			pst.setInt(1, credito);
			pst.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

}
