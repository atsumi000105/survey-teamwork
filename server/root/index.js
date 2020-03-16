$(window).on('load', function () {
    getSurveys();
})
//向后端请求获取文件
function getSurveys() {
    $.ajax({
        url: "http://localhost:8080/sendJson.php",
        success: function (data) {
            //data为String类型
            var arr = data.split('"');
            var fileArr = []; //文件名
            for (let i = 1; i < arr.length; i += 2) {
                fileArr.push(arr[i]);
            }
            $.each(fileArr, function (index, value) {
                createBox(value);
            })
        }
    })
}
//动态创建
function createBox(value) {
    var filename = value.split('\\').slice(-1)[0];
    //var fileurl = "http://localhost:8080/jsonfile/" + filename;
    var surveyTitle = filename.split('_')[0];
    var $box = $('<div class="box"></div>');
    var $title = $('<h1>' + surveyTitle + '</h1>');
    $box.append($title);
    var $qrbtn = $('<button class="qrbtn">Scan QRcode</button>');
    $box.append($qrbtn);
    $(".flex-container").append($box);
    //二维码扫描事件
    $qrbtn.click(function () {
        // console.log(fileurl);
        getJsonFile(filename);
        //createQRspace(fileurl);
    })
}
//获取文件内容
function getJsonFile(filename) {
    $.ajax({
        url: "http://localhost:8080/sendJson.php",
        data: {
            fileName: filename
        },
        success: function (data) {
            console.log(data);
            createQRspace(data);
        }
    })
}
//生成二维码显示区
function createQRspace(json) {
    $('.qrcode').css({ "display": "block" });
    $('.qrcode').empty();
    var qr_code = new QRCode("qrcode", { text: json, width: 295, heght: 295 });

    qr_code.makeCode(json);
}
