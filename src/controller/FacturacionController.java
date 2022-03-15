package controller;
import view.PrincipalView;
import model.FacturacionModel;
import java.util.Date;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class FacturacionController implements CaretListener {
	private static FacturacionController instancia = null;
	PrincipalView menu;
	FacturacionModel facturacionModel;
	DefaultTableModel modelo;
	
	private FacturacionController(PrincipalView menu, FacturacionModel facturacionModel){
		this.menu = menu;
		this.facturacionModel = facturacionModel;
		start();
		this.menu.txtBuscarProductoFacturacion.addCaretListener(this);
		
	}	

	public static void createInstanceController(PrincipalView menu, FacturacionModel facturacionModel){
		if(instancia == null)
			instancia = new FacturacionController(menu,facturacionModel);
	}

	

	public void start(){
		this.menu.lblEmpleadoFacturacion.setVisible(false);
		this.menu.lblIdCreditoFacturacion.setVisible(false);
		this.menu.btnGuardarFacturacion.setEnabled(false);
		this.menu.btnGuardarImprimirFacturacion.setEnabled(false);
		this.menu.jcFechaFactura.setDate(new Date());
		EstiloTablas.estilosCabeceras(this.menu.tblFacturacion);
	}

	public void guardar(){
		
	}

	public void agregarConCodigoBarra(String codigo){
	}

	public void agregarDesdeVentana(){
		
	}

	public void windowProducts(){
		
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if(e.getSource() == this.menu.txtBuscarProductoFacturacion){
			this.agregarConCodigoBarra(this.menu.txtBuscarProductoFacturacion.getText());
		}
	}

	
}
