<?php
header("Access-Control-Allow-Origin:*");
header('Access-Control-Allow-Methods:POST');
header("content-type:text/html;charset=utf-8");
$str=$_POST['json'];
$title=$_POST['title'];
//保存文件
$arr=json_decode($str,true);
$id=$arr['survey']['id'];
//去掉title的空格且首字母大写
$title=camel_case($title);
$jsonfile="jsonfile/" . $title . "_" . $id . ".json";
file_put_contents($jsonfile,$str);
$fileurl="http://localhost:8080/" . $jsonfile;
echo "The server saved " . $fileurl;
exit();
?>
<?php
function camel_case($s) 
{
    $upper_case = ucwords($s);
    return str_replace(' ', '', $upper_case);
}
?>