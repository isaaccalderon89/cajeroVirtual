package com.edix.cajero.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.edix.cajero.modelo.dao.IntCuentaDao;
import com.edix.cajero.modelo.entitybeans.Cuenta;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private IntCuentaDao icuen;

	@GetMapping("/")
	public String Login() {
		return "login";
	}

	@PostMapping("/login")
	public String empleadoLogueado(Model model, HttpSession sesionCuenta, @RequestParam("idCuenta") int idCuenta) {
		// Buscamos la cuenta existente por su idCuenta
		Cuenta cuenta = icuen.findById(idCuenta);
		// si existe, avanzamos al panel del cajero y llevamos el atributo de sesion que es la cuenta
		if (cuenta != null) {
			sesionCuenta.setAttribute("cuenta", cuenta);
			return "redirect:/opcionesCajero";
			// Si no existe seguimos en la pagina de login y mostramos un mensaje de error.
		} else {
			model.addAttribute("fallo", "El numero de cuenta no existe");
			return "login";
		}
	}

	// Método para cerrar la sesion
	@GetMapping("/logout")
	public String logout(Model model, HttpSession sesionCuenta) {
		sesionCuenta.removeAttribute("cuenta");
		sesionCuenta.invalidate();
		return "redirect:/";
	}
}
