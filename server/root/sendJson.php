<?php
header("content-type:text/html;charset=utf-8");
$filepath=realpath('jsonfile/');
$fileArr=getFiles($filepath);
echo json_encode($fileArr);
?>
<?php
//返回文件数组
function getFiles($filepath){
    if(!file_exists($filepath)){
        return [];
    }
    $fileArr=[];
    //切换到目录
    chdir($filepath);
    foreach(glob('*.json') as $file){
        // $file=mb_convert_encoding($file,"GBK","auto");
        // echo $file;
        //json文件的路径 
        $path=$filepath . DIRECTORY_SEPARATOR . $file;
        if(is_file($path)){
            $fileArr[]=$path;
        }
    }
    return $fileArr;
}
?>