/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiendamegahogar;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMonokaiProIJTheme;
import controller.LoginController;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
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
		FlatAtomOneDarkIJTheme.install();
		UIManager.put("Button.arc", 999);
		UIManager.put("Component.arc", 999);
		UIManager.put("ProgressBar.arc", 999);
		UIManager.put("TextComponent.arc", 999);
		LoginView loginView = new LoginView();
		LoginModel loginModel = new LoginModel();
		LoginController loginController = new LoginController(loginView, loginModel);
		loginController.start();
		try {
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel("com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme");
		} catch (Exception e) {
		}
	}

}
