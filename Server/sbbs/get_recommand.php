<?php
include "../database/database.php";
// get_recommand.php

$max_count = 10;

$sql = "select * from yugioh_recommand order by id desc limit 0, $max_count";
$db = openConnection();
$result = query($db, $sql);
closeConnection($db);

$str = "{\"data\":[";

while (list($id, $name, $jump_mode, $jump_url, $jump_text, $image_name,$big_qr)=mysql_fetch_row($result)) {
	$str = $str."{\"id\":\"$id\",\"name\":".json_encode($name).",\"jump_mode\":\"$jump_mode\",\"jump_url\":".json_encode($jump_url).",\"jump_text\":".json_encode($jump_text).",\"image_name\":\"$image_name\",\"big_qr\":\"$big_qr\"},";
}
$str = rtrim($str, ",");
$str = $str."]}";

echo $str;

?>
