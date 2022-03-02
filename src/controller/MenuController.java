/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import view.PrincipalView;
import java.awt.event.MouseListener;
import model.*;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class MenuController implements MouseListener, KeyListener {

	PrincipalView menu;
	UsuariosModel usuarioModel;
	EmpleadosModel empleadosModel;
	ProductosModel productosModel;
	KardexModel kardex;
	MarcasModel marcasModel;
	ClientesModel clientesModel;
	public static int empleadoSistema;

	public MenuController(PrincipalView menu, String usuario, int empleado) {
		this.menu = menu;
		MenuController.empleadoSistema = empleado;
		this.menu.lblUsuarioSistema.setText(usuario);
		this.usuarioModel = new UsuariosModel();
		this.empleadosModel = new EmpleadosModel();
		this.productosModel = new ProductosModel();
		this.kardex = new KardexModel();
		this.marcasModel = new MarcasModel();
		this.clientesModel = new ClientesModel();
		this.ocultarPaneles();
		this.menu.pnlOpcionUsuario.addMouseListener(this);
		this.menu.pnlOpcionEmpleados.addMouseListener(this);
		this.menu.pnlOpcionInventario.addMouseListener(this);
		this.menu.pnlOpcionClientes.addMouseListener(this);

	}

	private void ocultarPaneles() {
		this.menu.pnlUsuarios.setVisible(false);
		this.menu.pnlEmpleados.setVisible(false);
		this.menu.pnlInventario.setVisible(false);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == this.menu.pnlOpcionUsuario) {
			UsuariosController.createInstanceController(this.menu, this.usuarioModel);
			this.menu.pnlOpcionUsuario.setBackground(new java.awt.Color(0, 102, 255));
			this.menu.pnlOpcionEmpleados.setBackground(new java.awt.Color(0, 102, 204));
			this.menu.pnlOpcionInventario.setBackground(new java.awt.Color(0, 102, 204));
			this.menu.pnlOpcionClientes.setBackground(new java.awt.Color(0, 102, 204));
			this.menu.pnlUsuarios.setVisible(true);
			this.menu.pnlEmpleados.setVisible(false);
			this.menu.pnlInventario.setVisible(false);
			this.menu.pnlClientesCreditos.setVisible(false);
		} else if (e.getSource() == this.menu.pnlOpcionEmpleados) {
			EmpleadosController.createInstanceController(menu, empleadosModel);
			this.menu.pnlOpcionEmpleados.setBackground(new java.awt.Color(0, 102, 255));
			this.menu.pnlOpcionUsuario.setBackground(new java.awt.Color(0, 102, 204));
			this.menu.pnlOpcionInventario.setBackground(new java.awt.Color(0, 102, 204));
			this.menu.pnlOpcionClientes.setBackground(new java.awt.Color(0, 102, 204));
			this.menu.pnlEmpleados.setVisible(true);
			this.menu.pnlUsuarios.setVisible(false);
			this.menu.pnlInventario.setVisible(false);
			this.menu.pnlClientesCreditos.setVisible(false);
		} else if (e.getSource() == this.menu.pnlOpcionInventario) {
			ProductosController.createInstanceController(menu, productosModel, kardex);
			MarcasController.createInstanceController(menu, marcasModel);
			this.menu.pnlOpcionInventario.setBackground(new java.awt.Color(0, 102, 255));
			this.menu.pnlOpcionEmpleados.setBackground(new java.awt.Color(0, 102, 204));
			this.menu.pnlOpcionUsuario.setBackground(new java.awt.Color(0, 102, 204));
			this.menu.pnlOpcionClientes.setBackground(new java.awt.Color(0, 102, 204));
			this.menu.pnlInventario.setVisible(true);
			this.menu.pnlEmpleados.setVisible(false);
			this.menu.pnlUsuarios.setVisible(false);
			this.menu.pnlClientesCreditos.setVisible(false);
		} else if (e.getSource() == this.menu.pnlOpcionClientes) {
			ClientesController.createInstanceController(menu, clientesModel);
			this.menu.pnlOpcionClientes.setBackground(new java.awt.Color(0, 102, 255));
			this.menu.pnlOpcionInventario.setBackground(new java.awt.Color(0, 102, 204));
			this.menu.pnlOpcionEmpleados.setBackground(new java.awt.Color(0, 102, 204));
			this.menu.pnlOpcionUsuario.setBackground(new java.awt.Color(0, 102, 204));
			this.menu.pnlClientesCreditos.setVisible(true);
			this.menu.pnlInventario.setVisible(false);
			this.menu.pnlEmpleados.setVisible(false);
			this.menu.pnlUsuarios.setVisible(false);
		}
	}

	public void start() {
		this.menu.setVisible(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
