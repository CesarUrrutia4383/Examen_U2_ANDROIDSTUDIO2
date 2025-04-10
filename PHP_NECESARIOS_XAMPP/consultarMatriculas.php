<?php
$conexion = mysqli_connect("localhost", "root", "admin123", "itsrll");

$resultado = mysqli_query($conexion, "SELECT matricula FROM alumno");

$matriculas = array();

while ($fila = mysqli_fetch_assoc($resultado)) {
    $matriculas[] = $fila['matricula'];
}

echo json_encode($matriculas);

mysqli_close($conexion);
?>
