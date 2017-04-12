$(document).ready(function()
{
     $("#show_login").click(function(){
      showpopup();
     });

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
    var url = basePath+"/swagger-login";
    $.ajax({
        type: 'POST',
        url: url,
        data: {
         "login":loginName,
         "pw":pw
        },
        dataType: "json",
        success: function(testData){
         if(testData.content==true){
             $("#message").css({"visibility":"hidden","display":"none"});
             hidepopup();
             $("#swagger-ui-container").css({"visibility":"visible","display":"block"});
             $("#login-info").html('Welcome[ '+getCurrentUser()+'] <input type="button" value="Logout" id="logout-button" onclick="doLogout()"/>');
         }
        },
        error:function(testData){
             //hidepopup();
             $("#swagger-ui-container").css({"visibility":"hidden","display":"none"});
             //$("#show_login").css({"visibility":"visible","display":"block"});
             $("#message").css({"visibility":"visible","display":"block"});
             //alert(testData.responseText)
        }
    });
}
function doLogout()
{

    var url = basePath+"/swagger-logout";
    $.ajax({
        type: 'POST',
        url: url ,
        success: function(testData){
            if(testData.content==true){
                window.location.href=basePath;
            }
        },
        error:function(){
            $("#logout-button").val('Logout-Retry');
        }
    });
}