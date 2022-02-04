/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiendamegahogar;
import controller.LoginController;
import view.LoginView;
import model.LoginModel;
/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class TiendaMegaHogar {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		LoginView loginView = new LoginView();
		LoginModel loginModel = new LoginModel();
		LoginController loginController = new LoginController(loginView,loginModel);
		loginController.start();
	}
	
}
