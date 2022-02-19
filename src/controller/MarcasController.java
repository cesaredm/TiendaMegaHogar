/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import model.MarcasModel;
import view.PrincipalView;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class MarcasController implements ActionListener, CaretListener, KeyListener {

	PrincipalView menu;
	MarcasModel marcasModel;
	private static MarcasController instancia = null;
	int filaseleccionada;

	private MarcasController(PrincipalView menu, MarcasModel marcasModel) {
		this.menu = menu;
		this.marcasModel = marcasModel;
		this.menu.btnActualizarMarca.setEnabled(false);
		this.mostrar("");
		this.menu.btnGuardarMarca.addActionListener(this);
		this.menu.btnActualizarMarca.addActionListener(this);
		this.menu.optEditarMarca.addActionListener(this);
		this.menu.btnActualizarMarca.addActionListener(this);
		this.menu.txtNombreMarca.addKeyListener(this);
		this.menu.txtBuscarMarca.addCaretListener(instancia);
	}

	public static MarcasController getInsancia(PrincipalView menu, MarcasModel marcasModel) {
		if (instancia == null) {
			instancia = new MarcasController(menu, marcasModel);
		}
		return instancia;
	}

	public void limpiar(boolean limpiar) {
		if (limpiar) {
			this.menu.txtNombreMarca.setText("");
		}
	}

	public void guardar() {
		this.marcasModel.setNombre(this.menu.txtNombreMarca.getText());
		this.marcasModel.guardar();
		this.limpiar(this.marcasModel.validar);
		this.menu.txtNombreMarca.requestFocus();
		this.mostrar("");
		this.mostrarMarcasEnProductos();

	}

	public void editar() {
		this.filaseleccionada = this.menu.tblMarcas.getSelectedRow();
		if (this.filaseleccionada != -1) {
			this.marcasModel.setId(Integer.parseInt(this.menu.tblMarcas.getValueAt(filaseleccionada, 0).toString()));
			this.marcasModel.editar();
			this.menu.txtNombreMarca.setText(this.marcasModel.getNombre());
			this.menu.btnGuardarMarca.setEnabled(false);
			this.menu.btnActualizarMarca.setEnabled(true);
		}
	}

	public void actualizar() {
		this.marcasModel.setNombre(this.menu.txtNombreMarca.getText());
		this.marcasModel.actualizar();
		this.limpiar(this.marcasModel.validar);
		this.menu.txtNombreMarca.requestFocus();
		this.mostrar("");
		this.menu.btnGuardarMarca.setEnabled(true);
		this.menu.btnActualizarMarca.setEnabled(false);
	}

	public void mostrar(String value) {
		EstiloTablas.estilosCabeceras(this.menu.tblMarcas);
		this.marcasModel.mostar(value);
		this.menu.tblMarcas.setModel(this.marcasModel.tableModel);
	}

	public void mostrarMarcasEnProductos(){
		this.marcasModel.getMarcas();
		this.menu.cmbMarcasProducto.setModel(this.marcasModel.comboModel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "btnGuardarMarca": {
				this.guardar();
			}
			break;
			case "optEditarMarca": {
				this.editar();
			}
			break;
			case "btnActualizarMarca": {
				this.actualizar();
			}
			break;
		}
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == this.menu.txtBuscarMarca) {
			this.mostrar(this.menu.txtBuscarMarca.getText());
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getSource() == this.menu.txtNombreMarca && e.getKeyCode() == e.VK_ENTER && this.menu.btnGuardarMarca.isEnabled()) {
			this.guardar();
		}else if(e.getSource() == this.menu.txtNombreMarca && e.getKeyCode() == e.VK_ENTER && this.menu.btnActualizarMarca.isEnabled()){
			this.actualizar();	
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
