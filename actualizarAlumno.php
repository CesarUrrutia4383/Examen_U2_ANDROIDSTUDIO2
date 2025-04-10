<?php
$conexion = mysqli_connect("localhost", "root", "admin123", "itsrll");

if (!$conexion) {
    echo json_encode(["error" => "No se pudo conectar a la base de datos"]);
    exit();
}

$matricula = $_REQUEST['matricula'];
$nombre = $_REQUEST['nombre'];
$direccion = $_REQUEST['direccion'];
$email = $_REQUEST['email'];
$telefono = $_REQUEST['telefono'];
$carrera = $_REQUEST['carrera'];
$foto = $_REQUEST['foto'];

// Actualizar el alumno
$query = "UPDATE alumno SET nombre='$nombre', direccion='$direccion', email='$email', telefono='$telefono', carrera='$carrera', foto='$foto' WHERE matricula='$matricula'";
if (mysqli_query($conexion, $query)) {
    echo "Alumno actualizado correctamente";
} else {
    echo "Error al actualizar el alumno: " . mysqli_error($conexion);
}

mysqli_close($conexion);
?>
