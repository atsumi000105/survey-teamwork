<?php
header("Access-Control-Allow-Origin:*");
header('Access-Control-Allow-Methods:POST');
$str=$_POST['json'];
//save to file
$obj=json_decode($str,true);
$id=$obj['survey']['id'];
file_put_contents($id . ".json",$str);
$filepath="http://localhost:8080/" . $id . ".json";
echo $filepath;
// header("Location: http://localhost:8080/hihi.json");
exit();
?>