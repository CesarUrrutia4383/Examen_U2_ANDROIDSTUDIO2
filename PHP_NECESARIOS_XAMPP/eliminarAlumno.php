<?php
$conexion = mysqli_connect("localhost", "root", "admin123", "itsrll");

$matricula = $_REQUEST['matricula'];

// Eliminar los datos del alumno (incluyendo la imagen en Base64)
$query = "DELETE FROM alumno WHERE matricula = '$matricula'";
$result = mysqli_query($conexion, $query);

mysqli_close($conexion);

if ($result) {
    echo "Alumno eliminado exitosamente";
} else {
    echo "Error al eliminar alumno";
}
?>
