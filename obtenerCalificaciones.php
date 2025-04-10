<?php
$conn = mysqli_connect("localhost", "root", "admin123", "itsrll");
//$nombre = "IOT";
$nombre = $_REQUEST["nombre"];
$result = mysqli_query($conn, "SELECT u1, u2, u3, u4, u5 FROM materia WHERE nombre = '$nombre'");
$row = mysqli_fetch_assoc($result);
echo json_encode($row);
mysqli_close($conn);
?>