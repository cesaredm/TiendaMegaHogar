package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import model.ClientesModel;
import view.PrincipalView;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public final class ClientesController implements ActionListener, CaretListener {

	private static ClientesController instancia = null;
	PrincipalView menu;
	ClientesModel clientesModel;
	int id, filaseleccionada;

	private ClientesController(PrincipalView menu, ClientesModel clientesModel) {
		this.menu = menu;
		this.clientesModel = clientesModel;
		this.menu.btnActualizarCliente.setEnabled(false);
		this.mostrar("");
		this.menu.btnGuardarCliente.addActionListener(this);
		this.menu.btnActualizarCliente.addActionListener(this);
		this.menu.btnLimpiarCliente.addActionListener(this);
		this.menu.optEditarCliente.addActionListener(this);
		this.menu.txtBuscarCliente.addCaretListener(this);
	}

	public static void createInstanceController(PrincipalView menu, ClientesModel clientesModel) {
		if (instancia == null) {
			instancia = new ClientesController(menu, clientesModel);
		}
	}

	public void limpiar(boolean limpiar) {
		if (limpiar) {
			this.menu.txtNombresCliente.setText("");
			this.menu.txtApellidosCliente.setText("");
			this.menu.txtDniCliente.setText("");
			this.menu.txtAreaDireccionExactaCliente.setText("");
			this.menu.txtDepartamentoCliente.setText("");
			this.menu.txtMunicipioCliente.setText("");
			this.menu.txtBarrioCliente.setText("");
			this.menu.txtLugarTrabajoCliente.setText("");
			this.menu.txtTelefonoCliente.setText("");
			this.menu.btnGuardarCliente.setEnabled(true);
			this.menu.btnActualizarCliente.setEnabled(false);
			this.menu.txtNombresCliente.requestFocus();
		}
	}

	public void guardar() {
		this.clientesModel.setNombres(this.menu.txtNombresCliente.getText());
		this.clientesModel.setApellidos(this.menu.txtApellidosCliente.getText());
		this.clientesModel.setDni(this.menu.txtDniCliente.getText());
		this.clientesModel.setDireccionExacta(this.menu.txtAreaDireccionExactaCliente.getText());
		this.clientesModel.setDepartamento(this.menu.txtDepartamentoCliente.getText());
		this.clientesModel.setMunicipio(this.menu.txtMunicipioCliente.getText());
		this.clientesModel.setBarrio(this.menu.txtBarrioCliente.getText());
		this.clientesModel.setLugarTrabajo(this.menu.txtLugarTrabajoCliente.getText());
		this.clientesModel.setTelefono(this.menu.txtTelefonoCliente.getText());
		this.clientesModel.guardar();
		this.limpiar(this.clientesModel.validar);
		this.mostrar("");
	}

	public void editar() {
		this.filaseleccionada = this.menu.tblClientes.getSelectedRow();
		if (this.filaseleccionada != -1) {
			this.id = Integer.parseInt(this.menu.tblClientes.getValueAt(this.filaseleccionada, 0).toString());
			this.clientesModel.setId(this.id);
			this.clientesModel.editar();
			this.menu.txtNombresCliente.setText(this.clientesModel.getNombres());
			this.menu.txtApellidosCliente.setText(this.clientesModel.getApellidos());
			this.menu.txtDniCliente.setText(this.clientesModel.getDni());
			this.menu.txtAreaDireccionExactaCliente.setText(this.clientesModel.getDireccionExacta());
			this.menu.txtDepartamentoCliente.setText(this.clientesModel.getDepartamento());
			this.menu.txtMunicipioCliente.setText(this.clientesModel.getMunicipio());
			this.menu.txtBarrioCliente.setText(this.clientesModel.getBarrio());
			this.menu.txtLugarTrabajoCliente.setText(this.clientesModel.getLugarTrabajo());
			this.menu.txtTelefonoCliente.setText(this.clientesModel.getTelefono());
			this.menu.btnGuardarCliente.setEnabled(false);
			this.menu.btnActualizarCliente.setEnabled(true);
		}
	}

	public void actualizar() {
		this.clientesModel.setNombres(this.menu.txtNombresCliente.getText());
		this.clientesModel.setApellidos(this.menu.txtApellidosCliente.getText());
		this.clientesModel.setDni(this.menu.txtDniCliente.getText());
		this.clientesModel.setDireccionExacta(this.menu.txtAreaDireccionExactaCliente.getText());
		this.clientesModel.setDepartamento(this.menu.txtDepartamentoCliente.getText());
		this.clientesModel.setMunicipio(this.menu.txtMunicipioCliente.getText());
		this.clientesModel.setBarrio(this.menu.txtBarrioCliente.getText());
		this.clientesModel.setLugarTrabajo(this.menu.txtLugarTrabajoCliente.getText());
		this.clientesModel.setTelefono(this.menu.txtTelefonoCliente.getText());
		this.clientesModel.actualizar();
		this.limpiar(this.clientesModel.validar);
		this.mostrar("");
	}

	public void mostrar(String value) {
		EstiloTablas.estilosCabeceras(this.menu.tblClientes);
		this.clientesModel.mostrar(value);
		this.menu.tblClientes.setModel(this.clientesModel.tableModel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "btnGuardarCliente": {
				this.guardar();
			}
			break;
			case "btnActualizarCliente": {
				this.actualizar();
			}
			break;
			case "btnLimpiarCliente": {
				this.limpiar(true);
			}
			break;
			case "optEditarCliente": {
				this.editar();
			}
			break;
		}
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if(e.getSource() == this.menu.txtBuscarCliente){
			this.mostrar(this.menu.txtBuscarCliente.getText());
		}
	}

}
