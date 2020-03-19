$(window).on('load', function () {
    getSurveys();
})
/*
get survey files saved in DB to show in boxes
*/
function getSurveys() {
    $.ajax({
        url: "http://localhost:8080/sendJson.php",
        success: function (data) {
            var arr = data.split('"');
            var fileArr = []; //file array
            for (let i = 1; i < arr.length; i += 2) {
                fileArr.push(arr[i]);
            }
            $.each(fileArr, function (index, value) {
                createBox(value);
            })
        }
    })
}
/*
func:create a box to show a survey
params:the filename of a survey
*/
function createBox(value) {
    var filename = value.split('\\').slice(-1)[0];
    var surveyTitle = filename.split('_')[0];
    var $box = $('<div class="box"></div>');
    var $title = $('<h1>' + surveyTitle + '</h1>');
    $box.append($title);
    var $qrbtn = $('<button class="qrbtn">Scan QRcode</button>');
    $box.append($qrbtn);
    $(".flex-container").append($box);

    //Here is the btn to show QR code
    $qrbtn.click(function () {
        getJsonFile(filename);
    })
}

/*
func:get file of a survey from server
params:filename saved in server
*/
function getJsonFile(filename) {
    $.ajax({
        url: "http://localhost:8080/sendJson.php",
        data: {
            fileName: filename
        },
        success: function (data) {
            createQRspace(data);
        }
    })
}
/*
func:create div to show QR code
params:json string of a survey
*/
function createQRspace(json) {
    $('.qrcode').css({ "display": "block" });
    $('.qrcode').empty();
    var qr_code = new QRCode("qrcode", { text: json, width: 295, heght: 295 });

    qr_code.makeCode(json);
}
