<?php
header("content-type:text/html;charset=utf-8");
$filepath=realpath('jsonfile/');

if(isset($_GET['fileName'])){
	$filename=$_GET['fileName'];
	getFileContent($filepath,$filename);
}else{
	$fileArr=getFiles($filepath);
	echo json_encode($fileArr);
}
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

//返回文件内容
function getFileContent($filepath,$filename){
	$str="read file error";
	 if(!file_exists($filepath)){
        echo "not exist";
    }
	$file=$filepath . DIRECTORY_SEPARATOR . $filename;
	if(is_file($file)){
		$str=file_get_contents($file);	
	}
	echo $str;
}
	
?>