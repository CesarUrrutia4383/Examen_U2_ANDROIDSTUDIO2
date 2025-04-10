<?php
$conexion = mysqli_connect("localhost", "root", "admin123", "itsrll");

$matricula = $_REQUEST['matricula'];

// Consulta para obtener los datos del alumno, incluyendo la imagen
$query = "SELECT * FROM alumno WHERE matricula = '$matricula'";
$result = mysqli_query($conexion, $query);

if ($row = mysqli_fetch_assoc($result)) {
    $response = array(
        "nombre" => $row['nombre'],
        "direccion" => $row['direccion'],
        "email" => $row['email'],
        "telefono" => $row['telefono'],
        "carrera" => $row['carrera'],
        "foto" => $row['foto']  // La imagen en Base64
    );
    echo json_encode($response);
} else {
    echo json_encode(array("error" => "Alumno no encontrado"));
}

mysqli_close($conexion);
?>
