<?php


$conexion = mysqli_connect("localhost", "root", "admin123", "itsrll");

$codigo = $_REQUEST['codigo'];
$nombre = $_REQUEST['nombre'];
$matricula = $_REQUEST['matricula'];

mysqli_query($conexion, "INSERT INTO materia (codigo, nombre) VALUES ('$codigo', '$nombre')");
mysqli_query($conexion, "INSERT INTO alumno_materia (matricula, codigo_materia) VALUES ('$matricula', '$codigo')");

mysqli_close($conexion);

// Respuesta simple en formato JSON
echo json_encode(["message" => "Materia asignada correctamente"]);
?>
