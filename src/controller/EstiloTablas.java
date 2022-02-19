/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Color;
import javax.swing.JTable;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */

public class EstiloTablas {

	private EstiloTablas() {

	}

	public static void estilosCabeceras(JTable table) {
		table.getTableHeader().setOpaque(true);
		table.getTableHeader().setBackground(new java.awt.Color(111,95,144));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 30));
	}
}
