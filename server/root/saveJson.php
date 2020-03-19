<?php
header("Access-Control-Allow-Origin:*");
header('Access-Control-Allow-Methods:POST');
header("content-type:text/html;charset=utf-8");
$str=$_POST['json'];
$title=$_POST['title'];
$arr=json_decode($str,true);
$id=$arr['survey']['id'];
//rid the spaces and Capitalize(the filename can't have spaces)
$title=camel_case($title);
$jsonfile="jsonfile/" . $title . "_" . $id . ".json";//filename:surveyTitle_surveyId.json
file_put_contents($jsonfile,$str);
// $fileurl="http://localhost:8080/" . $jsonfile;
// echo "The server saved " . $fileurl;
exit();
?>
<?php
/*
func:rid the spaces and Capitalize
*/
function camel_case($s) 
{
    $upper_case = ucwords($s);
    return str_replace(' ', '', $upper_case);
}
?>