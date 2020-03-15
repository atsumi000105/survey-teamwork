$(window).on('load',function(){
    getSurveys();
})
//向后端请求获取文件
function getSurveys(){
    $.ajax({
        url:"http://localhost:8080/sendJson.php",
        success:function(data){
            console.log(data);
            //data为String类型
            var arr=data.split('"');
            var fileArr=[];//文件名
            for(let i=1;i<arr.length;i+=2){
                fileArr.push(arr[i]);
            }
            $.each(fileArr,function(index,value){
                createBox(value);
            })
        }
    })
}
//动态创建
function createBox(value){
    console.log(value);
    var tmp=value.split('\\').slice(-1)[0];
    var fileurl="http://localhost:8080/jsonfile/"+tmp;
    var surveyTitle=tmp.split('_')[0];
    var $box=$('<div class="box"></div>');
    var $title=$('<h1>'+surveyTitle+'</h1>');
    $box.append($title);
    var $qrbtn=$('<button class="qrbtn">Scan QRcode</button>');
    $box.append($qrbtn);
    $(".flex-container").append($box);
    //二维码扫描事件
    $qrbtn.click(function(){
        console.log(fileurl);
    })
}