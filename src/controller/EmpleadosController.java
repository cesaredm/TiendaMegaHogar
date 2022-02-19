/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.cert.CRLSelector;
import javax.swing.JOptionPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import view.PrincipalView;
import model.EmpleadosModel;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class EmpleadosController implements ActionListener, CaretListener {

	PrincipalView menu;
	EmpleadosModel empleadosModel;
	private static EmpleadosController instancia = null;
	int filaseleccionada;

	private EmpleadosController(PrincipalView menu, EmpleadosModel empleadosModel) {
		this.menu = menu;
		this.empleadosModel = empleadosModel;
		this.mostrar("");
		this.menu.btnActualizarEmpleado.setEnabled(false);
		this.menu.btnGuardarEmpleado.addActionListener(this);
		this.menu.btnLimpiarEmpleado.addActionListener(this);
		this.menu.btnActualizarEmpleado.addActionListener(this);
		this.menu.optEditarEmpleado.addActionListener(this);
		this.menu.optEliminarEmpleado.addActionListener(this);
		this.menu.txtBuscarEmpleado.addCaretListener(this);
	}

	public static EmpleadosController getInstancia(PrincipalView menu, EmpleadosModel empleadosModel) {
		if (instancia == null) {
			instancia = new EmpleadosController(menu, empleadosModel);
		}
		return instancia;
	}

	public void limpiar(boolean l) {
		if (l) {
			this.menu.txtNombresEmpleado.setText("");
			this.menu.txtApellidosEmpleado.setText("");
			this.menu.txtTelefonoEmpleado.setText("");
			this.menu.btnGuardarEmpleado.setEnabled(true);
			this.menu.btnActualizarEmpleado.setEnabled(false);
		}
	}

	public void guardar() {
		this.empleadosModel.setNombres(this.menu.txtNombresEmpleado.getText());
		this.empleadosModel.setApellidos(this.menu.txtApellidosEmpleado.getText());
		this.empleadosModel.setTelefono(this.menu.txtTelefonoEmpleado.getText());
		this.empleadosModel.guardar();
		this.limpiar(this.empleadosModel.validar);
		this.mostrar("");
	}

	public void actualizar() {
		this.empleadosModel.setNombres(this.menu.txtNombresEmpleado.getText());
		this.empleadosModel.setApellidos(this.menu.txtApellidosEmpleado.getText());
		this.empleadosModel.setTelefono(this.menu.txtTelefonoEmpleado.getText());
		this.empleadosModel.actualizar();
		this.menu.btnGuardarEmpleado.setEnabled(true);
		this.menu.btnActualizarEmpleado.setEnabled(false);
		this.limpiar(this.empleadosModel.validar);
		this.mostrar("");
	}

	public void editar() {
		this.filaseleccionada = this.menu.tblEmpleados.getSelectedRow();
		if (this.filaseleccionada != -1) {
			this.empleadosModel.setId(
				Integer.parseInt(this.menu.tblEmpleados.getValueAt(filaseleccionada, 0).toString())
			);
			this.empleadosModel.editar();
			this.menu.txtNombresEmpleado.setText(this.empleadosModel.getNombres());
			this.menu.txtApellidosEmpleado.setText(this.empleadosModel.getApellidos());
			this.menu.txtTelefonoEmpleado.setText(this.empleadosModel.getTelefono());
			this.menu.btnActualizarEmpleado.setEnabled(true);
			this.menu.btnGuardarEmpleado.setEnabled(false);
		}
	}

	public void eliminar() {
		this.filaseleccionada = this.menu.tblEmpleados.getSelectedRow();
		if (this.filaseleccionada != -1) {
			int confirmar = JOptionPane.showConfirmDialog(
				null,
				"Seguro que quieres eliminar este empleado",
				"Advertencia",
				JOptionPane.OK_CANCEL_OPTION
			);
			if (confirmar == JOptionPane.YES_OPTION) {
				this.empleadosModel.setId(
					Integer.parseInt(this.menu.tblEmpleados.getValueAt(filaseleccionada, 0).toString())
				);
				this.empleadosModel.eliminar();
				this.mostrar("");
			}

		}
	}

	private void mostrar(String value) {
		EstiloTablas.estilosCabeceras(this.menu.tblEmpleados);
		this.empleadosModel.mostrar(value);
		this.menu.tblEmpleados.setModel(empleadosModel.getTableModel());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "btnGuardarEmpleado": {
				this.guardar();
			}
			break;
			case "btnLimpiarEmpleado": {
				this.limpiar(true);
			}
			break;
			case "optEditarEmpleado": {
				this.editar();
			}
			break;
			case "btnActualizarEmpleado": {
				this.actualizar();
			}
			break;
			case "optEliminarEmpleado": {
				this.eliminar();
			}
			break;
		}
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == this.menu.txtBuscarEmpleado) {
			this.mostrar(this.menu.txtBuscarEmpleado.getText());
		}
	}

}
