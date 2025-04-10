<?php
$conn = mysqli_connect("localhost", "root", "admin123", "itsrll");
/*$nombre = "IOT";
$u1 = "90";
$u2 = "90";
$u3 = "90";
$u4 = "90";
$u5 = "90";*/
$nombre = $_REQUEST["nombre"];
$u1 = $_REQUEST["u1"];
$u2 = $_REQUEST["u2"];
$u3 = $_REQUEST["u3"];
$u4 = $_REQUEST["u4"];
$u5 = $_REQUEST["u5"];
mysqli_query($conn, "UPDATE materia SET u1='$u1', u2='$u2', u3='$u3', u4='$u4', u5='$u5' WHERE nombre = '$nombre'");
echo json_encode(["status" => "ok"]);
mysqli_close($conn);
?>