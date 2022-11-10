package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.ProveedoresModel;
import view.PrincipalView;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class ProveedoresController implements ActionListener {
	private static ProveedoresController instancia = null;
	PrincipalView menu;
	ProveedoresModel proveedorModel;	
	private ProveedoresController(PrincipalView menu, ProveedoresModel proveedorModel){
		this.menu = menu;
		this.proveedorModel = proveedorModel;
		this.menu.btnActualizarProveedores.setEnabled(false);
		this.menu.btnGuardarProveedores.addActionListener(this);
		this.menu.btnActualizarProveedores.addActionListener(this);
		this.menu.btnLimpiarProveedores.addActionListener(this);
	}

	public static void creteInstanciaController(PrincipalView menu, ProveedoresModel proveedorModel){
		if(instancia==null){
			instancia = new ProveedoresController(menu, proveedorModel);
		}	
	}

	public void guardar(){
		this.proveedorModel.setNombre(this.menu.txtNombreProveedor.getText());
		this.proveedorModel.setTelefono(this.menu.txtTelefonoProveedor.getText());
		this.proveedorModel.setCuentaBancaria(this.menu.txtCuentaBancariaProveddor.getText());
		this.proveedorModel.setVendedor(this.menu.txtVendedorProveedor.getText());
		this.proveedorModel.setTelefonoVendedor(this.menu.txtTelefonoVendedor.getText());
		this.proveedorModel.guardar();
		if (this.proveedorModel.validar) {
			this.limpiar();
		}
	}

	public void limpiar(){
		this.menu.txtNombreProveedor.setText("");
		this.menu.txtTelefonoProveedor.setText("");
		this.menu.txtCuentaBancariaProveddor.setText("");
		this.menu.txtVendedorProveedor.setText("");
		this.menu.txtTelefonoVendedor.setText("");
		this.menu.btnGuardarProveedores.setEnabled(true);
		this.menu.btnActualizarProveedores.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "btnGuardarProveedores":
				this.guardar();
				break;
			case "btnActualizarProveedores":
				break;
			case "btnLimpiarProveedores":
				break;
			default:
				throw new AssertionError();
		}
	}


}
