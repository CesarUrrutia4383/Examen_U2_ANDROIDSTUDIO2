<?php
$conn = mysqli_connect("localhost", "root", "admin123", "itsrll");
$matricula = $_REQUEST["matricula"];
$result = mysqli_query($conn, "SELECT m.nombre FROM materia m 
JOIN alumno_materia am ON m.codigo = am.codigo_materia 
WHERE am.matricula = '$matricula'");
$datos = [];
while ($row = mysqli_fetch_assoc($result)) {
    $datos[] = $row["nombre"];
}
echo json_encode($datos);
mysqli_close($conn);
?>