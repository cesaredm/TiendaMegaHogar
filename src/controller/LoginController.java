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
import javax.swing.JOptionPane;
import view.LoginView;
import model.LoginModel;
import view.PrincipalView;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class LoginController implements ActionListener, KeyListener {

	String user, password;
	LoginView login;
	LoginModel loginModel;
	MenuController menuController;
	PrincipalView principalView;

	public LoginController(LoginView login, LoginModel loginModel) {
		this.loginModel = loginModel;
		this.login = login;
		this.login.btnAcceder.addActionListener(this);
		this.login.txtPasswordLogin.addKeyListener(this);
		this.login.txtUsuarioLogin.addKeyListener(this);
	}

	public void start() {
		this.login.setVisible(true);
		this.login.setLocationRelativeTo(null);
	}

	public void validar() {
		this.user = this.login.txtUsuarioLogin.getText();
		this.password = this.login.txtPasswordLogin.getText();
		this.loginModel.setUser(user);
		this.loginModel.setPassword(password);
		this.loginModel.validar();
		if (this.loginModel.validar) {
			this.login.dispose();
			this.principalView = new PrincipalView();
			this.menuController = new MenuController(this.principalView,this.loginModel.getUser(),this.loginModel.getEmpleado());
			this.menuController.start();
		} else {
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.login.btnAcceder) {
			this.validar();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getSource() == this.login.txtPasswordLogin && e.getKeyCode() == e.VK_ENTER) {
			this.validar();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
