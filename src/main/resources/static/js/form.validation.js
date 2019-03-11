
function checkPassword(pass, repass){

    if(pass.val().length < 8){
        pass[0].setCustomValidity('invalid');
    }
    else {
        pass[0].setCustomValidity('');
    }

    comparePasswords(pass, repass);
}

function comparePasswords(pass, repass){

    if(repass.val().length < 8 || pass.val() !== repass.val()){
        repass[0].setCustomValidity('invalid');
    }
    else {
        repass[0].setCustomValidity('');
    }
}

function validateEmail(email)
{
    var email_regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
    if(!email_regex.test(email.val()))
    {
        email[0].setCustomValidity('invalid');
        /*var div = $("#"+id).closest("div");
        div.removeClass("has-success");
        $("#glypcn"+id).remove();
        div.addClass("has-error has-feedback");
        div.append('<span id="glypcn'+id+'" class="glyphicon glyphicon-remove form-control-feedback"></span>');
        return false;*/
    }
    else{
        email[0].setCustomValidity('');
        /*var div = $("#"+id).closest("div");
        div.removeClass("has-error");
        $("#glypcn"+id).remove();
        div.addClass("has-success has-feedback");
        div.append('<span id="glypcn'+id+'" class="glyphicon glyphicon-ok form-control-feedback"></span>');
        return true;*/
    }

}