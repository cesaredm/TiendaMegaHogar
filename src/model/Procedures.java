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
		} finally {
			try {
				cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return response;
	}

	public static boolean venderId(int id, float cantidad) {
		cn = conexion();
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(Procedures.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return response;
	}
}
