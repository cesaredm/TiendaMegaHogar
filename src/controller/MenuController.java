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
	CreditosModel creditosModel;
	FacturacionModel facturacionModel;
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
		this.creditosModel = new CreditosModel();
		this.facturacionModel = new FacturacionModel();
		this.ocultarPaneles();
		this.menu.pnlOpcionUsuario.addMouseListener(this);
		this.menu.pnlOpcionEmpleados.addMouseListener(this);
		this.menu.pnlOpcionInventario.addMouseListener(this);
		this.menu.pnlOpcionClientes.addMouseListener(this);
		this.menu.pnlOpcionFacturacion.addMouseListener(this);

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
			this.menu.pnlOpcionUsuario.setBackground(new java.awt.Color(28,31,37));
			this.menu.pnlOpcionEmpleados.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlOpcionInventario.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlOpcionClientes.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlOpcionFacturacion.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlUsuarios.setVisible(true);
			this.menu.pnlEmpleados.setVisible(false);
			this.menu.pnlInventario.setVisible(false);
			this.menu.pnlClientesCreditos.setVisible(false);
			this.menu.pnlFacturacion.setVisible(false);
		} else if (e.getSource() == this.menu.pnlOpcionEmpleados) {
			EmpleadosController.createInstanceController(menu, empleadosModel);
			this.menu.pnlOpcionEmpleados.setBackground(new java.awt.Color(28,31,37));
			this.menu.pnlOpcionUsuario.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlOpcionInventario.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlOpcionClientes.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlOpcionFacturacion.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlEmpleados.setVisible(true);
			this.menu.pnlUsuarios.setVisible(false);
			this.menu.pnlInventario.setVisible(false);
			this.menu.pnlClientesCreditos.setVisible(false);
			this.menu.pnlFacturacion.setVisible(false);
		} else if (e.getSource() == this.menu.pnlOpcionInventario) {
			ProductosController.createInstanceController(menu, productosModel, kardex);
			MarcasController.createInstanceController(menu, marcasModel);
			this.menu.pnlOpcionInventario.setBackground(new java.awt.Color(28,31,37));
			this.menu.pnlOpcionEmpleados.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlOpcionUsuario.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlOpcionClientes.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlOpcionFacturacion.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlInventario.setVisible(true);
			this.menu.pnlEmpleados.setVisible(false);
			this.menu.pnlUsuarios.setVisible(false);
			this.menu.pnlClientesCreditos.setVisible(false);
			this.menu.pnlFacturacion.setVisible(false);
		} else if (e.getSource() == this.menu.pnlOpcionClientes) {
			ClientesController.createInstanceController(menu, clientesModel);
			CreditosController.createInstanceController(menu, creditosModel);
			this.menu.pnlOpcionClientes.setBackground(new java.awt.Color(28,31,37));
			this.menu.pnlOpcionInventario.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlOpcionEmpleados.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlOpcionUsuario.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlOpcionFacturacion.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlClientesCreditos.setVisible(true);
			this.menu.pnlInventario.setVisible(false);
			this.menu.pnlEmpleados.setVisible(false);
			this.menu.pnlUsuarios.setVisible(false);
			this.menu.pnlFacturacion.setVisible(false);
		} else if (e.getSource() == this.menu.pnlOpcionFacturacion) {
			FacturacionController.createInstanceController(menu, facturacionModel);
			this.menu.pnlOpcionFacturacion.setBackground(new java.awt.Color(28,31,37));
			this.menu.pnlOpcionClientes.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlOpcionInventario.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlOpcionEmpleados.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlOpcionUsuario.setBackground(new java.awt.Color(32,36,42));
			this.menu.pnlFacturacion.setVisible(true);
			this.menu.pnlClientesCreditos.setVisible(false);
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
