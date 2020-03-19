$(window).on('load', function () {

    //questionType buttons event
    $('.sinbtn').on('click', function () {
        var $quesans = createQuesName();
        $quesans.siblings('.add').click(function () {
            addSingleOpt($quesans);
        })
    })
    $('.mulbtn').on('click', function () {
        var $quesans = createQuesName();
        $quesans.siblings('.add').click(function () {
            addMultiOpt($quesans);
        })
    })
    $('.edtbtn').on('click', function () {
        var $quesans = createQuesName(0);
        addEdt($quesans);
    })

    //the event for POST button
    $('.postbtn').on('click', function () {
        generageJson();
    })
})
/*
func:create question names
params:question type:choose questions=1,filling questions=others; DEFAULT=1
*/
function createQuesName(type = 1) {
    var $surbox = $('<div class="sur-box">');
    var $quesname = $('<div class="ques-name" contenteditable="true">Please Write Your Question</div>');
    $surbox.append($quesname);
    var $quesans = $('<div class="ques-ans"></div>');
    $surbox.append($quesans);
    var $delitem=$('<button class="delitem">Delete Question</button>');
    $surbox.append($delitem);
    $delitem.click(function(){
        delQuestion($(this));
    })
    if (type == 1) {
        var $addbtn = $('<button class="add">Add Choice</button>');
        $surbox.append($addbtn);
    }
    var $clear = $('<div class="clear"></div>');
    $surbox.append($clear);
    $('.sur-form').append($surbox);
    return $quesans;
}
/*
func: dynamically add single options
params:the div to add options
*/
function addSingleOpt($quesans) {
    var $label = $('<label></label>');
    var $delbtn = $('<a class="delbtn" href="javascript:void(0)">delete</a>');
    $label.append($delbtn);
    var $radio = $('<input type="radio">');
    $label.append($radio);
    var $choice = $('<textarea class="ans-opt" type="text" placeholder="choice 1" cols="30" rows="3"></textarea>');
    $label.append($choice);
    $quesans.append($label);
    $delbtn.click(function () {
        delChoice($(this));
    })
}
/*
func: dynamically add multiple options
params:the div to add options
*/
function addMultiOpt($quesans) {
    var $label = $('<label></label>');
    var $delbtn = $('<a class="delbtn" href="javascript:void(0)">delete</a>');
    $label.append($delbtn);
    var $checkbox = $('<input type="checkbox">');
    $label.append($checkbox);
    var $choice = $('<textarea class="ans-opt" type="text" placeholder="choice 1" cols="30" rows="3"></textarea>');
    $label.append($choice);
    $quesans.append($label);
    $delbtn.click(function () {
        delChoice($(this));
    })
}
/*
func: dynamically add edittext
params:the div to add options
*/
function addEdt($quesans) {
    var $edt = $('<div class="ques-name sur-edit">This is the answer area</div>');
    $quesans.append($edt);
}
/*
func:delete a option:label
params:the obj to delete
*/
function delChoice($this) {
    $this.parent('label').remove();
}
/*
func:delete a question:sur-box
params:the obj to delete
*/
function delQuestion($this){
    $this.parents('.sur-box').remove();
}
/*
func:generate json from the survey and send to server
*/
function generageJson() {
    var obj={};
    var date=new Date();
    var timestamp=date.getTime();
    var survey={id:timestamp+"",len:0,questions:[]};
    
    var questions = new Array();
    $('.sur-box').each(function () {
        var aQues = { type: "no", question: "no", options: [] };

        //get question type
        var $inputtype = $(this).children('.ques-ans').find('input');
        var $edittype = $(this).children('.ques-ans').find('.sur-edit');

        /*add question type to AQues based on diff type*/
        if ($edittype.length > 0) {
            aQues.type = "edittext";
        } else {
            if ($inputtype.attr('type') == undefined) {
                console.log("no options");
            } else {
                if ($inputtype.attr("type") == "radio") {
                    aQues.type = "single";
                } else if ($inputtype.attr("type") == "checkbox") {
                    aQues.type = "multiple";
                }else{
                    console.log("type",$inputtype.attr("type"));
                }
            }
        }


        /*add question name to AQues*/
        aQues.question = $(this).children('.ques-name').text();
        
        /*add options to AQues based on diff type(only choose type)*/
        var id = 1;
        var $quesopts = $(this).children('.ques-ans').find('.ans-opt');
        if ($quesopts.length > 0) {
            $quesopts.each(function () {
                var aOpt = {};
                aOpt[id++] = $(this).val();
                aQues.options.push(aOpt);
            })
        }
        /*a survey json is done: aQues */

        questions.push(aQues);

    })

    survey.len=questions.length;
    survey.questions=questions;
    
    var title=$('.tit').val();//save the title to save as filename and show in home page
    obj.survey=survey;
    var json = JSON.stringify(obj);

    saveToServer(json,title);
    
}
/*
func:save the file to server
params:json string of a survey,title of a survey
*/
function saveToServer(json,title){
    $.ajax({
        url:"http://localhost:8080/saveJson.php",
        type:"post",    
        data:{
            json:json,
            title:title
        },
        success:function(data){
            if(confirm("Saved Successful! Do you want to leave for Home Page?")){
                window.location.href="index.html";
            }
        },
        error:function(error){
            console.log("error");
        }
    })
}