function logSubmit(){
    document.forms.auth.passwordHash.value = CryptoJS.MD5(document.forms.auth.passwordHash.value).toString().toUpperCase();
}
