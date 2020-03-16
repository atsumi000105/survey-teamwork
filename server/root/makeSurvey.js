$(window).on('load', function () {
    //选择题按钮点击事件
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

    //发布按钮点击事件
    $('.postbtn').on('click', function () {
        generageJson();
    })
    // //扫描二维码事件
    // $('.qrbtn').on('click',function(){
    //     //jsonfile是json文件的保存地址
    //     var jsonfile="";
    //     if(sessionStorage.getItem('jsonfile')!=null){
    //         jsonfile=sessionStorage.getItem('jsonfile');
    //     }else{
    //         jsonfile="http://localhost:8080/saveError.php";
    //     }
        
    //     // window.location.href=jsonfile;
    // })
})
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
function addEdt($quesans) {
    var $edt = $('<div class="ques-name sur-edit">This is the answer area</div>');
    $quesans.append($edt);
}
//删除一个选项，即一个label
function delChoice($this) {
    $this.parent('label').remove();
}
//删除一个问题，即一个sur-box
function delQuestion($this){
    $this.parents('.sur-box').remove();
}
//将问卷生成json格式保存
function generageJson() {
    var obj={};
    var date=new Date();
    var timestamp=date.getTime();
    var survey={id:timestamp+"",len:0,questions:[]};
    
    var questions = new Array();
    $('.sur-box').each(function () {
        var aQues = { type: "no", question: "no", options: [] };

        //获取问题类型，找到input[type='']
        var $inputtype = $(this).children('.ques-ans').find('input');
        var $edittype = $(this).children('.ques-ans').find('.sur-edit');
        //判断是否为edit元素
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


        //问题名称
        aQues.question = $(this).children('.ques-name').text();
        //问题选项
        var id = 1;

        //单选和多选
        var $quesopts = $(this).children('.ques-ans').find('.ans-opt');
        if ($quesopts.length > 0) {
            $quesopts.each(function () {
                var aOpt = {};
                aOpt[id++] = $(this).val();
                aQues.options.push(aOpt);
            })
        }
        /*到此完成了一个问题的内容   */

        questions.push(aQues);

    })

    survey.len=questions.length;
    survey.questions=questions;
    //保存title
    var title=$('.tit').val();
    obj.survey=survey;
    var json = JSON.stringify(obj);

    saveToServer(json,title);
    
}
//把问卷以json格式保存到服务端
function saveToServer(json,title){
    $.ajax({
        url:"http://localhost:8080/saveJson.php",
        type:"post",    
        data:{
            json:json,
            title:title
        },
        success:function(data){
            console.log(data);
            alert("Success:",data);
        },
        error:function(error){
            console.log("error");
        }
    })
}