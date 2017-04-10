$(document).ready(function()
{
 $("#show_login").click(function(){
  showpopup();
 });
 // $("#close_login").click(function(){
 //  hidepopup();
 // });

$("#dologin").click(function(){
    doLogin();
});
});

function showpopup()
{
 $("#show_login").css({"visibility":"hidden","display":"none"});
 $("#loginform").fadeIn();
 $("#loginform").css({"visibility":"visible","display":"block","position": "absolute","top": "100px","left":"40%", "align-content": "center"});
}

function hidepopup()
{
 $("#loginform").fadeOut();
 $("#loginform").css({"visibility":"hidden","display":"none"});
}

function doLogin()
{
    var loginName = $("#login").val();
    var pw = $("#password").val();
    $.ajax({
        type: 'POST',
        url: "/swagger",
        data: {
         "login":loginName,
         "pw":pw
        },
        dataType: "json",
        success: function(testData){
         if(testData.content==true){
             hidepopup();
             $("#swagger-ui-container").css({"visibility":"visible","display":"block"});
         }
        },
        error:function(testData){
             hidepopup();
             $("#swagger-ui-container").css({"visibility":"hidden","display":"none"});
             $("#show_login").css({"visibility":"visible","display":"block"});
             alert(testData.responseText)
        }
    });
}