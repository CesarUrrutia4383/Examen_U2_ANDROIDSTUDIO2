<?php
$conexion = mysqli_connect("localhost", "root", "admin123", "itsrll");

// Obtener la matrícula del alumno desde la solicitud GET
$matricula = $_REQUEST['matricula'];

// Consultar las materias que han sido asignadas al alumno
$query = "
    SELECT m.nombre
    FROM materia m
    JOIN alumno_materia am ON am.codigo_materia = m.codigo
    WHERE am.matricula = '$matricula'
";

// Ejecutar la consulta
$result = mysqli_query($conexion, $query);

// Crear un array para almacenar los resultados
$materias = array();

while ($row = mysqli_fetch_assoc($result)) {
    $materias[] = $row['nombre'];  // Agregar el nombre de la materia al array
}

// Cerrar la conexión
mysqli_close($conexion);
echo json_encode($materias);
?>
