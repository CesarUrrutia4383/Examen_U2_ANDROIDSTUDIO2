<?php
$conexion = mysqli_connect("localhost", "root", "admin123", "itsrll");

$matricula = $_REQUEST['matricula'];

$sql = "SELECT nombre FROM alumno WHERE matricula = '$matricula'";
$resultado = mysqli_query($conexion, $sql);

if ($fila = mysqli_fetch_assoc($resultado)) {
    echo json_encode($fila);
} else {
    echo json_encode(["nombre" => "Desconocido"]);
}

mysqli_close($conexion);
?>
