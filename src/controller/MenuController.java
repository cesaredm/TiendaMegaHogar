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
	MarcasModel marcasModel;
	UsuariosController usuarioController = null;
	EmpleadosController empleadosController = null;
	ProductosController productosController = null;
	MarcasController marcasController = null;

	public MenuController(PrincipalView menu, String usuario, int empleado) {
		this.menu = menu;
		this.menu.lblUsuarioSistema.setText(usuario);
		this.usuarioModel = new UsuariosModel();
		this.empleadosModel = new EmpleadosModel();
		this.productosModel = new ProductosModel();
		this.marcasModel = new MarcasModel();
		this.ocultarPaneles();
		this.menu.pnlOpcionUsuario.addMouseListener(this);
		this.menu.pnlOpcionEmpleados.addMouseListener(this);
		this.menu.pnlOpcionInventario.addMouseListener(this);

	}

	private void ocultarPaneles() {
		this.menu.pnlUsuarios.setVisible(false);
		this.menu.pnlEmpleados.setVisible(false);
		this.menu.pnlInventario.setVisible(false);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == this.menu.pnlOpcionUsuario) {
			this.usuarioController = UsuariosController.getInstancia(this.menu, this.usuarioModel);
			this.menu.pnlOpcionUsuario.setBackground(new java.awt.Color(0, 102, 255));
			this.menu.pnlOpcionEmpleados.setBackground(new java.awt.Color(0, 102, 204));
			this.menu.pnlOpcionInventario.setBackground(new java.awt.Color(0,102,204));
			this.menu.pnlUsuarios.setVisible(true);
			this.menu.pnlEmpleados.setVisible(false);
			this.menu.pnlInventario.setVisible(false);
		} else if (e.getSource() == this.menu.pnlOpcionEmpleados) {
			this.empleadosController = EmpleadosController.getInstancia(menu, empleadosModel);
			this.menu.pnlOpcionEmpleados.setBackground(new java.awt.Color(0, 102, 255));
			this.menu.pnlOpcionUsuario.setBackground(new java.awt.Color(0, 102, 204));
			this.menu.pnlOpcionInventario.setBackground(new java.awt.Color(0,102,204));
			this.menu.pnlEmpleados.setVisible(true);
			this.menu.pnlUsuarios.setVisible(false);
			this.menu.pnlInventario.setVisible(false);
		}else if(e.getSource() == this.menu.pnlOpcionInventario){
			this.productosController = ProductosController.getInstancia(menu, productosModel);
			this.marcasController = MarcasController.getInsancia(menu, marcasModel);
			this.menu.pnlOpcionInventario.setBackground(new java.awt.Color(0,102,255));
			this.menu.pnlOpcionEmpleados.setBackground(new java.awt.Color(0, 102, 204));
			this.menu.pnlOpcionUsuario.setBackground(new java.awt.Color(0, 102, 204));
			this.menu.pnlInventario.setVisible(true);
			this.menu.pnlEmpleados.setVisible(false);
			this.menu.pnlUsuarios.setVisible(false);
		}
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

	public void start() {
		this.menu.setVisible(true);
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
