/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class Conexion {
	private static String db = "tiendamegahogar";
	private static String url = "jdbc:mysql://localhost/" + db;
	private static String user = "root";
	private static String pass = "19199697tsoCD";
	public Conexion(){
		
	}

	public static Connection conexion() {
		Connection link = null;
		try {
			//cargamos el Driver a 
			Class.forName("org.gjt.mm.mysql.Driver");

			link = DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
		return link;
	}

}
