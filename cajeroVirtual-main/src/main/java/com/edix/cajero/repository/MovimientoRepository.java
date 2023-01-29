package com.edix.cajero.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.edix.cajero.modelo.entitybeans.Movimiento;

// Al extender a JpaRepository, podemos utilizar los metodos y clases integradas dentro de Spring Boot para tratar bases de datos
public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {

	@Query("SELECT m FROM Movimiento m WHERE m.cuenta.idCuenta = ?1")
	public List<Movimiento> findMovimientosByIdCuenta(int idCuenta);
}
