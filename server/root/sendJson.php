<?php
/*to send the survey json string to index */
header("content-type:text/html;charset=utf-8");
$filepath=realpath('jsonfile/');

if(isset($_GET['fileName'])){//for Qr code
	$filename=$_GET['fileName'];
	getFileContent($filepath,$filename);
}else{//for home page
	$fileArr=getFiles($filepath);
	echo json_encode($fileArr);
}

/*
get files saved on server as an Array
return: array
*/
function getFiles($filepath){
    if(!file_exists($filepath)){
        return [];
    }
    $fileArr=[];
    //change to the dir
    chdir($filepath);
    foreach(glob('*.json') as $file){
        $path=$filepath . DIRECTORY_SEPARATOR . $file;
        if(is_file($path)){
            $fileArr[]=$path;
        }
    }
    return $fileArr;
}

/*
get file contents as string
return:string
*/
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