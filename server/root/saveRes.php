 <?php
 /* to save the results of the survey from survey app*/
    header('Content-Type: text/html; charset=utf-8');
    if (isset($_POST['survey_id']) && isset($_POST['answers'])) {
        $survey_id = $_POST['survey_id'];
        $answers = $_POST['answers'];
        $location = $_POST['location'];
        $time = $_POST['time'];
        $imei = $_POST['imei'];

        //create database and table
        $conn = createDB("root", "usbw");
        createTable($conn);
        insertData($conn, $survey_id, $answers, $location, $time, $imei);
    } else {
        echo "please set the survey_id and answers";
    }

/*create database*/
function createDB($username, $password, $server = "localhost", $DBname = "surveyDB")
{
    $conn = mysqli_connect($server, $username, $password);
    if ($conn) {
        $sql = "create database if not exists " . $DBname . " default charset utf8 collate utf8_general_ci";
        $res = mysqli_query($conn, $sql);
        if (!$res) {
            echo "Create database failed!" . mysqli_error($conn);
            return null;
        } else {
            return $conn;
        }
    } else {
        echo "Connect to server failed!" . mysqli_connect_error();
        return null;
    }
}

/*create survey table */
function createTable($conn, $tablename = "Survey_Result", $DBname = "surveyDB")
{

    if ($conn != null) {
        $sql = "use " . $DBname . ";";
        if (mysqli_query($conn, $sql)) {
            $sql = "create table if not exists " . $tablename . " (id INT PRIMARY KEY  AUTO_INCREMENT,survey_id  varchar(20) not null ,survey_data longtext not null,location varchar(50), time varchar(30),imei varchar(30) DEFAULT null)";
            $res = mysqli_query($conn, $sql);
            if (!$res) {
                echo "create table failed!" . mysqli_error($conn);
            }
        } else {
            echo "use database failed!" . mysqli_error($conn);
        }
    } else {
        echo "create table:connection is null";
    }
}

/* insert data into table*/
function insertData($conn, $survey_id, $answers, $location, $time, $imei, $tablename = "Survey_Result")
{
    $sql = 'insert into ' . $tablename . ' set survey_id="' . $survey_id . '",survey_data="' . $answers . '",location="' . $location . '",time="' . $time . '",imei="' . $imei . '"';
    if ($conn != null) {
        //set Chinese char
        mysqli_query($conn, "set character set 'utf8'");
        mysqli_query($conn, 'set name utf8');
        $res = mysqli_query($conn, $sql);
        if (!$res) {
            echo "insert failed! " . mysqli_error($conn);
        }
    } else {
        echo "insert:connection is null";
    }
}
?>