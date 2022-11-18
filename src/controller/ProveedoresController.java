package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import model.ProveedoresModel;
import view.PrincipalView;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class ProveedoresController implements ActionListener,CaretListener {

	private static ProveedoresController instancia = null;
	PrincipalView menu;
	ProveedoresModel proveedorModel;
	int filaseleccionada;

	private ProveedoresController(PrincipalView menu, ProveedoresModel proveedorModel) {
		this.menu = menu;
		this.proveedorModel = proveedorModel;
		//actionListener
		this.menu.btnActualizarProveedores.setEnabled(false);
		this.menu.btnGuardarProveedores.addActionListener(this);
		this.menu.btnActualizarProveedores.addActionListener(this);
		this.menu.btnLimpiarProveedores.addActionListener(this);
		this.menu.optEditarProveedor.addActionListener(this);
		this.menu.optEliminarProveedor.addActionListener(this);
		//CaretListener
		this.menu.txtBuscarProveedor.addCaretListener(this);
		//funciones al arrancar
		this.mostrar("");
	}

	public static void creteInstanciaController(PrincipalView menu, ProveedoresModel proveedorModel) {
		if (instancia == null) {
			instancia = new ProveedoresController(menu, proveedorModel);
		}
	}

	public void guardar() {
		this.proveedorModel.setNombre(this.menu.txtNombreProveedor.getText());
		this.proveedorModel.setTelefono(this.menu.txtTelefonoProveedor.getText());
		this.proveedorModel.setCuentaBancaria(this.menu.txtCuentaBancariaProveddor.getText());
		this.proveedorModel.setVendedor(this.menu.txtVendedorProveedor.getText());
		this.proveedorModel.setTelefonoVendedor(this.menu.txtTelefonoVendedor.getText());
		this.proveedorModel.guardar();
		if (this.proveedorModel.validar) {
			this.limpiar();
			this.mostrar("");
		}
	}

	public void editar() {
		this.filaseleccionada = this.menu.tblProveedores.getSelectedRow();
		if (this.filaseleccionada != -1) {
			this.proveedorModel.setId(Integer.parseInt(this.menu.tblProveedores.getValueAt(this.filaseleccionada, 0).toString()));
			this.proveedorModel.editar();
			this.menu.txtNombreProveedor.setText(this.proveedorModel.getNombre());
			this.menu.txtCuentaBancariaProveddor.setText(this.proveedorModel.getCuentaBancaria());
			this.menu.txtTelefonoProveedor.setText(this.proveedorModel.getTelefono());
			this.menu.txtTelefonoVendedor.setText(this.proveedorModel.getTelefonoVendedor());
			this.menu.txtVendedorProveedor.setText(this.proveedorModel.getVendedor());
			this.menu.btnGuardarProveedores.setEnabled(false);
			this.menu.btnActualizarProveedores.setEnabled(true);
		}
	}

	public void eliminar() {
		int confirmar = JOptionPane.showConfirmDialog(null, "Seguro quieres eliminar este proveedor de forma permanente?", "Advertencia", JOptionPane.YES_NO_OPTION);
		if (confirmar == JOptionPane.YES_OPTION) {
			this.proveedorModel.setId(Integer.parseInt(this.menu.tblProveedores.getValueAt(this.filaseleccionada, 0).toString()));
			this.proveedorModel.eliminar();
		}
	}

	public void actualizar() {
		this.proveedorModel.setNombre(this.menu.txtNombreProveedor.getText());
		this.proveedorModel.setTelefono(this.menu.txtTelefonoProveedor.getText());
		this.proveedorModel.setCuentaBancaria(this.menu.txtCuentaBancariaProveddor.getText());
		this.proveedorModel.setVendedor(this.menu.txtVendedorProveedor.getText());
		this.proveedorModel.setTelefonoVendedor(this.menu.txtTelefonoVendedor.getText());
		this.proveedorModel.actualizar();
		if (this.proveedorModel.validar) {
			this.limpiar();
			this.mostrar("");
		}
	}

	public void limpiar() {
		this.menu.txtNombreProveedor.setText("");
		this.menu.txtTelefonoProveedor.setText("");
		this.menu.txtCuentaBancariaProveddor.setText("");
		this.menu.txtVendedorProveedor.setText("");
		this.menu.txtTelefonoVendedor.setText("");
		this.menu.btnGuardarProveedores.setEnabled(true);
		this.menu.btnActualizarProveedores.setEnabled(false);
	}

	public void mostrar(String value){
		EstiloTablas.estilosCabeceras(this.menu.tblProveedores);
		this.proveedorModel.getProveedores(value);
		this.menu.tblProveedores.setModel(this.proveedorModel.tableModel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "btnGuardarProveedores":
				this.guardar();
				break;
			case "btnActualizarProveedores":
				this.actualizar();
				break;
			case "btnLimpiarProveedores":
				this.limpiar();
				break;
			case "optEditarProveedor":
				this.editar();
				break;
			case "optEliminarProveedor":
				this.eliminar();
				break;
			default:
				throw new AssertionError();
		}
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == this.menu.txtBuscarProveedor) {
			this.mostrar(this.menu.txtBuscarProveedor.getText());
		}
	}

}
