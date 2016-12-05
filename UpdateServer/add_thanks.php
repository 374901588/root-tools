<?php

require_once "database.php";

$name = $_REQUEST["name"];
$desc = $_REQUEST["desc"];
$head = $_REQUEST["head"];

$db = openConnection();
$stmt = $db->prepare("insert into thanks (name, description, head_image) values (?, ?, ?)");
$stmt->bind_param("sss", $name, $desc, $head);
$stmt->execute();
$stmt->close();
closeConnection($db);

?>