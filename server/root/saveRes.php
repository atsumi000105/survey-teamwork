<?php
header('Content-Type: text/html; charset=utf-8');
if (isset($_POST['survey_id']) && isset($_POST['answers'])) {
    $survey_id = $_POST['survey_id'];
    $answers = $_POST['answers'];
    echo $survey_id . " : " . $answers;

    $conn = mysqli_connect('localhost', 'root', 'usbw', 'survey');
    if (!$conn) {
        echo "cannot connect to database" . mysqli_connect_error();
        exit;
    } else {
        echo "connect";
    }
    //设置中文字符
    mysqli_query($conn, "set character set 'utf8'");
    mysqli_query($conn, 'set name utf8');
    // $sql='insert into survey_table set survey_id=8976577,answers="thank you for doing this for me"';
    $sql = 'insert into survey_table set survey_id=' . $survey_id . ',answers="' . $answers . '"';
    $res = mysqli_query($conn, $sql);
    if ($res) {
        echo "insert";
    } else {
        echo "fail to insert " . mysqli_error($conn);
    }
} else {
    echo "请设置survey_id和answers属性";
}
