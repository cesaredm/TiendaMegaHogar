/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import model.UsuariosModel;
import view.PrincipalView;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class UsuariosController implements ActionListener, CaretListener, MouseListener {

	PrincipalView menu;
	UsuariosModel usuarioModel;
	private static UsuariosController usuariosController = null;
	boolean validar;
	int idEmpleado, filaSeleccionada, idUsuario;
	String usuario, password, permiso, nombreEmpleado;

	private UsuariosController(PrincipalView menu, UsuariosModel usuariosModel) {
		this.menu = menu;
		this.usuarioModel = usuariosModel;
		this.menu.btnLimpiarUsuario.addActionListener(this);
		this.menu.btnGuardarUsuario.addActionListener(this);
		this.menu.btnActualizarUsuario.addActionListener(this);
		this.menu.optEditarUsuario.addActionListener(this);
		this.menu.optEliminarUsuario.addActionListener(this);
		this.menu.txtIdEmpleadoUsuario.addMouseListener(this);
		this.menu.txtEmpleadoUsuario.addMouseListener(this);
		this.menu.btnActualizarUsuario.setEnabled(false);
		this.menu.txtBuscarUsuario.addCaretListener(this);
		this.menu.txtBuscarEmpleadoUsuario.addCaretListener(this);
		this.menu.tblEmpleadosUsuario.addMouseListener(this);
		this.mostrar("");
		this.mostrarEmpleados("");

	}

	/* SINGLENTON */
	public static UsuariosController getInstancia(PrincipalView menu, UsuariosModel usuarioModel) {
		if (usuariosController == null) {
			usuariosController = new UsuariosController(menu, usuarioModel);
		}
		return usuariosController;
	}

	/*CRUD*/
	public void validar() {
		this.idEmpleado = (this.menu.txtIdEmpleadoUsuario.getText().equals("")) ? 0 : Integer.parseInt(this.menu.txtIdEmpleadoUsuario.getText());
		this.usuario = this.menu.txtNombreUsuario.getText();
		this.password = this.menu.txtPasswordUsuario.getText();
		this.permiso = this.menu.cmbPermisoUsuario.getSelectedItem().toString();
		if (this.idEmpleado == 0) {
			this.validar = false;
		} else if (this.usuario.equals("")) {
			this.validar = false;
		} else if (this.password.equals("")) {
			this.validar = false;
		} else {
			this.validar = true;
		}
	}

	public void guardar() {
		this.validar();
		if (this.validar) {
			this.usuarioModel.setEmpleado(idEmpleado);
			this.usuarioModel.setUsuario(usuario);
			this.usuarioModel.setPassword(password);
			this.usuarioModel.setPermiso(permiso);
			this.usuarioModel.guardar();
			this.limpiar();
			this.mostrar("");
		}
	}

	public void mostrar(String value) {
		EstiloTablas.estilosCabeceras(this.menu.tblUsuarios);
		this.usuarioModel.mostrar(value);
		this.menu.tblUsuarios.setModel(this.usuarioModel.getTableMode());
	}

	public void mostrarEmpleados(String value) {
		EstiloTablas.estilosCabeceras(this.menu.tblEmpleadosUsuario);
		this.usuarioModel.empleados(value);
		this.menu.tblEmpleadosUsuario.setModel(this.usuarioModel.getTableModelEmpleados());
	}

	public void editar() {
		this.filaSeleccionada = this.menu.tblUsuarios.getSelectedRow();
		if (this.filaSeleccionada != -1) {
			this.idUsuario = Integer.parseInt(this.menu.tblUsuarios.getValueAt(filaSeleccionada, 0).toString());
			this.usuarioModel.setIdUsuario(idUsuario);
			this.usuarioModel.editar();
			this.menu.txtIdEmpleadoUsuario.setText("" + this.usuarioModel.getEmpleado());
			this.menu.txtEmpleadoUsuario.setText(this.usuarioModel.getNombreEmpleado());
			this.menu.txtNombreUsuario.setText(this.usuarioModel.getUsuario());
			this.menu.txtPasswordUsuario.setText(this.usuarioModel.getPassword());
			this.menu.cmbPermisoUsuario.setSelectedItem(this.usuarioModel.getPermiso());
			this.menu.btnActualizarUsuario.setEnabled(true);
			this.menu.btnGuardarUsuario.setEnabled(false);
		}
	}

	public void actualizar() {
		this.validar();
		if (this.validar) {
			this.usuarioModel.setEmpleado(idEmpleado);
			this.usuarioModel.setUsuario(usuario);
			this.usuarioModel.setPassword(password);
			this.usuarioModel.setPermiso(permiso);
			this.usuarioModel.actualizar();
			this.limpiar();
			this.menu.btnActualizarUsuario.setEnabled(false);
			this.menu.btnGuardarUsuario.setEnabled(true);
			this.mostrar("");
		}
	}

	public void eliminar() {
		this.filaSeleccionada = this.menu.tblUsuarios.getSelectedRow();
		if (this.filaSeleccionada != -1) {
			int confirmar = JOptionPane.showConfirmDialog(
				null,
				"Seguro quieres eliminar este usuario.?",
				"Advertencia",
				JOptionPane.YES_NO_CANCEL_OPTION
			);
			if (confirmar == JOptionPane.YES_OPTION) {
				this.idUsuario = Integer.parseInt(this.menu.tblUsuarios.getValueAt(filaSeleccionada, 0).toString());
				this.usuarioModel.setIdUsuario(idUsuario);
				this.usuarioModel.eliminar();
				this.mostrar("");
			}

		}
	}

	public void limpiar() {
		this.menu.txtIdEmpleadoUsuario.setText("");
		this.menu.txtEmpleadoUsuario.setText("");
		this.menu.txtNombreUsuario.setText("");
		this.menu.txtPasswordUsuario.setText("");
		this.menu.cmbPermisoUsuario.setSelectedIndex(0);
		this.menu.btnActualizarUsuario.setEnabled(false);
		this.menu.btnGuardarUsuario.setEnabled(true);
	}

	public void agregarEmpleado() {
		this.filaSeleccionada = this.menu.tblEmpleadosUsuario.getSelectedRow();
		try {
			if (this.filaSeleccionada != -1) {
				this.idEmpleado = Integer.parseInt(this.menu.tblEmpleadosUsuario.getValueAt(filaSeleccionada, 0).toString());
				this.nombreEmpleado = (String) this.menu.tblEmpleadosUsuario.getValueAt(filaSeleccionada, 1) + " "
					+ (String) this.menu.tblEmpleadosUsuario.getValueAt(filaSeleccionada, 2);
				this.menu.txtIdEmpleadoUsuario.setText("" + idEmpleado);
				this.menu.txtEmpleadoUsuario.setText(this.nombreEmpleado);
				this.menu.jdEmpleadosUsuarios.setVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "btnLimpiarUsuario": {
				this.limpiar();
			}
			break;
			case "btnGuardarUsuario": {
				this.guardar();
			}
			break;
			case "btnActualizarUsuario": {
				this.actualizar();
			}
			break;
			case "optEditarUsuario": {
				this.editar();
			}
			break;
			case "optEliminarUsuario": {
				this.eliminar();
			}
			break;
		}
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == this.menu.txtBuscarUsuario) {
			this.mostrar(this.menu.txtBuscarUsuario.getText());
		} else if (e.getSource() == this.menu.txtBuscarEmpleadoUsuario) {
			this.mostrarEmpleados(this.menu.txtBuscarEmpleadoUsuario.getText());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == this.menu.txtIdEmpleadoUsuario || e.getSource() == this.menu.txtEmpleadoUsuario) {
			this.menu.jdEmpleadosUsuarios.setSize(967, 384);
			this.menu.jdEmpleadosUsuarios.setLocationRelativeTo(null);
			this.menu.jdEmpleadosUsuarios.setVisible(true);
		}else if(e.getSource() == this.menu.tblEmpleadosUsuario){
			if(e.getClickCount() == 2){
				this.agregarEmpleado();
			}
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
}
