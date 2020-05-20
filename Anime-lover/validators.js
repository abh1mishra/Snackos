function validateEmail(email,errorArray) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if(!(re.test(String(email).toLowerCase()))){
        errorArray.push("emailError");
    }
}
function validateName(name,errorArray){
    var regName = /^[a-zA-Z]+ [a-zA-Z]+$/;
    if(!regName.test(name)){
        errorArray.push("nameError");
    }
}
///

module.exports.validator=function(user){
    var errorArray=[]
    validateEmail(user.email,errorArray);
    validateName(user.name,errorArray);
    //
    return errorArray;
}