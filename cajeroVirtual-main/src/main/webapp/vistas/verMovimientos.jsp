<%@page import="com.edix.cajero.modelo.entitybeans.Movimiento"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Movimientos de la cuenta</title>
</head>
<body>
	<% List<Movimiento> listacomplmovimientos = (List<Movimiento>)request.getAttribute("listamovimientos");%>
	<h2>Movimientos de tu cuenta bancaria</h2>
			<h4><i>Saldo actual: ${cuenta.saldo}</i></h4>
	<table class="table" border= 1px solid black>
		<thead>
			<tr>
				<th scope="col">Cantidad</th>
				<th scope="col">Fecha</th>
				<th scope="col">Tipo</th>
			</tr>
		</thead>
		<tbody>
			<% for (Movimiento ele: listacomplmovimientos){ %>
				<tr>
					<td><%= ele.getCantidad() %></td>
					<td><%= ele.getFecha() %></td>
					<td><%= ele.getOperacion() %></td>
				</tr>
			<%}%>
		</tbody>
	</table>
	<button type="button"><a href="/opcionesCajero"><i>Volver</i></a></button>
</body>
</html>