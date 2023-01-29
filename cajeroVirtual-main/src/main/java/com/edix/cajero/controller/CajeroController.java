package com.edix.cajero.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.edix.cajero.modelo.dao.IntCuentaDao;
import com.edix.cajero.modelo.dao.IntMovimientoDao;
import com.edix.cajero.modelo.entitybeans.Cuenta;
import com.edix.cajero.modelo.entitybeans.Movimiento;

import jakarta.servlet.http.HttpSession;

@Controller
public class CajeroController {
	@Autowired
	private HttpSession misesion;
	@Autowired
	private IntCuentaDao icuen;
	@Autowired
	private IntMovimientoDao imov;
	
	// Esta es la direccion a la que nos envia despues del login si la cuenta logueada se encuentra en la BBDD.
	@GetMapping ("/opcionesCajero")
	public String verPrincipal (Model model ) {
		//Recuperamos la cuenta que tenemos en la sesion guardada.
		Cuenta cuenta=(Cuenta) misesion.getAttribute("cuenta");
		model.addAttribute("Cuenta", cuenta);
		return "opcionesCajero";
	}
	
	// JSP correspondiente a ingresar, que es un formulario.
	@GetMapping ("/ingresar")
	public String ingresarDinero(Model model) {
		return "ingresar";
	}
	
	// Traemos los datos del formulario con el PostMapping, en este caso traemos solo la cantidad, ya que es el unico campo.
	@PostMapping("/ingresar")
	public String ingresarDinero(Model model, double cantidad ) {
		//Recuperamos la cuenta
		Cuenta cuenta=(Cuenta) misesion.getAttribute("cuenta");
		//Añadimos movimiento ingreso 
		imov.movimientoIngreso(cuenta, cantidad);
		//Realizamos operacion ingreso de dinero
		icuen.ingresarDinero(cuenta,cantidad);
		model.addAttribute("cuenta", cuenta);
		return "redirect:/opcionesCajero";
	}
	
	// JSP correspondiente a extraer que es un formulario.
	@GetMapping("/extraer")
	public String extraerDinero(Model model) {
		//Recuperamos la cuenta
		Cuenta cuenta=(Cuenta) misesion.getAttribute("cuenta");
		//Establecemos que si la cuenta es de ahorro, no se puede sacar dinero por el cajero, nos presente un mensaje y nos devuelva a cajero.
		if (cuenta.getTipoCuenta().equals("ahorro")) {
			List<Movimiento> lista=new ArrayList<Movimiento>();
			model.addAttribute("listamovimientos", lista);
			model.addAttribute("error", "Cuenta de AHORRO, no es posible sacar dinero, solo ingresos y transferencias.");
			model.addAttribute("Cuenta", cuenta);
			return "opcionesCajero";
		}else {
			return "extraer";
		}
	}
	
	// Con PostMapping recuperamos los valores del formulario de extraccion, en este caso la cantidad introducida.
	@PostMapping("/extraer")
	public String extraerDinero(Model model,double cantidad) {
		// Recuperamos la cuenta
		Cuenta cuenta=(Cuenta)misesion.getAttribute("cuenta");
		// Si la cuenta no tiene saldo suficiente, presentamos un mensaje y seguimos en la mismo jsp de extraccion.
		if (cuenta.getSaldo()<cantidad) {
			model.addAttribute("fallo", "Donde vas, que no hay dinero pobre.");
			return "extraer";
		// Si la cuenta tiene saldo disponible para ese importe de extracción gestionamos el movimiento
		}else {
		// Añadimos movimiento de extraccion
		imov.movimientoExtraccion(cuenta,cantidad);
		// Realizamos operacion de extraccion de dinero en la cuenta
		icuen.extraerDinero(cuenta, cantidad);
		model.addAttribute("cuenta", cuenta);
		return "redirect:/opcionesCajero";
		}
	}
	
	// JSP correspondiente a la transferencia que es un formulario
	@GetMapping("/transferencia")
	public String  transferirDinero(Model model) {
		return "transferencia";
	}
	
	// Con PostMapping recuperamos los valores del formulario de transferencia
	@PostMapping("/transferencia")
	public String transferirDinero(Model model, double cantidad, int idCuenta) {
		//Recuperamos la cuenta con la que estamos trabajando
		Cuenta cuenta=(Cuenta)misesion.getAttribute("cuenta");
		//Creamos una nueva cuenta que es la cuenta de destino del dinero
		Cuenta cuentaDestino=icuen.findById(idCuenta);
		//Si la cuenta de destino es la misma que la de origen o la cuenta de destino no existe, mostramos un mensaje y seguimos en transferencia.
		if (cuenta.equals(cuentaDestino)||cuentaDestino==null) {
			model.addAttribute("fallo", "No se puede hacer una transferencia a la misma cuenta o a otra inexistente");
			return "transferencia";
		//Si la cuenta existe y no es la misma de origen, gestionamos la transferencia
		}else {
			//Si el saldo es insuficiente en la cuenta de origen para realizar la transferencia, presentamos mensaje y seguimos en transferencia
			if (cuenta.getSaldo()<cantidad) {
				model.addAttribute("fallo", "No hay suficiente dinero en la cuenta");
				return "transferencia";
			//Si existe saldo suficiente, gestionamos la transferencia
			}else {
				//Realizamos operacion extraccion en cuenta de origen
				icuen.extraerDinero(cuenta, cantidad);
				//Realizamos operacion ingreso en la cuenta de destino
				icuen.ingresarDinero(cuentaDestino, cantidad);
				//Añadimos movimiento en la cuenta de origen
				imov.movimientoExtraccion(cuenta, cantidad);
				//Añadimos movimiento en la cuenta de destino
				imov.movimientoIngreso(cuentaDestino, cantidad);
				model.addAttribute("Cuenta", cuenta);
				return "redirect:/opcionesCajero";
			}
		}
	}
	
	//Aqui gestionaremos la visualizacion de TODOS los movimientos existentes en una cuenta
	@GetMapping("/verMovimientos")
	public String verMovimientos(Model model) {
		Cuenta cuenta=(Cuenta)misesion.getAttribute("cuenta");
		model.addAttribute("listamovimientos", imov.findMovimientosByIdCuenta(cuenta.getIdCuenta()));
		model.addAttribute("Cuenta", cuenta);
		return "verMovimientos";
	}
}
