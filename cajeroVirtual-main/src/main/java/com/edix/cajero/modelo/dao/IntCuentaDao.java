package com.edix.cajero.modelo.dao;

import java.util.List;

import com.edix.cajero.modelo.entitybeans.Cuenta;

public interface IntCuentaDao {

	List<Cuenta> findAll(); // Método que lista todas las cuentas

	Cuenta findById(int idCuenta); // Método que encuentra una cuenta a través del idCuenta

	int ingresarDinero(Cuenta cuenta, double cantidad); // Método para ingresar dinero en una cuenta

	int extraerDinero(Cuenta cuenta, double cantidad); // Método para retirar dinero de una cuenta

}
