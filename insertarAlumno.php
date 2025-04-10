<?php
$conexion = mysqli_connect("localhost", "root", "admin123", "itsrll");

$par1 = $_REQUEST['matricula'];
$par2 = $_REQUEST['nombre'];
$par3 = $_REQUEST['direccion'];
$par4 = $_REQUEST['email'];
$par5 = $_REQUEST['telefono'];
$par6 = $_REQUEST['carrera'];
$par7 = $_REQUEST['foto'];

// Realizar la consulta para insertar los datos
$query = mysqli_query($conexion, "INSERT INTO alumno (matricula, nombre, direccion, email, telefono, carrera, foto) 
VALUES ('$par1', '$par2', '$par3', '$par4', '$par5', '$par6', '$par7')");

if ($query) {
    echo json_encode(["message" => "Alumno registrado exitosamente"]);
} else {
    echo json_encode(["message" => "Error al registrar el alumno"]);
}

mysqli_close($conexion);
?>
