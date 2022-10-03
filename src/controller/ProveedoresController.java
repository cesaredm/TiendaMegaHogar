package controller;

import model.ProveedoresModel;
import view.PrincipalView;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class ProveedoresController {
	private static ProveedoresController instancia = null;
	PrincipalView menu;
	ProveedoresModel proveedorModel;	
	private ProveedoresController(PrincipalView menu, ProveedoresModel proveedorModel){
		this.menu = menu;
		this.proveedorModel = proveedorModel;
	}

	public static void creteInstanciaController(PrincipalView menu, ProveedoresModel proveedorModel){
		if(instancia==null){
			instancia = new ProveedoresController(menu, proveedorModel);
		}	
	}

	public void guardar(){
		this.proveedorModel.setNombre("");
	}


}
