var g,p,a;
var xhr = new XMLHttpRequest();

keyGen();



xhr.onreadystatechange = function() {
    if(xhr.readyState == 4){
        generateSecretKey(xhr.responseText);
    }
}

function keyGen(){
    getConstants();
    sendPublicKey();
}
function getConstants(){
    g = prime();
    p = prime();
    a = getRandomInt(0,100);
}
function getPublicKey(){
    return Math.pow(g,a)%p;
}
function generateSecretKey(otherPublicKey){
    sessionStorage["privateKey"] = Math.pow(otherPublicKey,a)%p;
}
function sendPublicKey(){
    var params = 'g=' + g + "&p=" + p + "&publicKey=" + getPublicKey()
    console.log(params)
    xhr.open('GET', '/getKey?'+params, true);
    xhr.send();    
}

function encrypted(message, key, iv){
    key = CryptoJS.enc.Utf8.parse(key);
    iv = CryptoJS.enc.Utf8.parse(iv);
    console.log("Encrypted data source:\t" + message);
    console.log("Encrypted data result:\t" + CryptoJS.AES.encrypt(message, key, { iv: iv }).ciphertext.toString(CryptoJS.enc.Base64));
    console.log("---------------------------------------------------------------------------------------------------------------------")
    return CryptoJS.AES.encrypt(message, key, { iv: iv }).ciphertext.toString(CryptoJS.enc.Base64)
}
function decrypted(message){
    key = CryptoJS.enc.Utf8.parse(key);
    iv = CryptoJS.enc.Utf8.parse(iv);
    console.log("Decrypted data source:\t" + message);
    console.log("Decrypted data result:\t" + CryptoJS.enc.Utf8.stringify(CryptoJS.AES.decrypt(message, key, { iv: iv })));
    console.log("---------------------------------------------------------------------------------------------------------------------")
}



function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min)) + min;
}
function prime() {
    check = getRandomInt(0,100);
    while (!isPrime(check)) {
        check = getRandomInt(0,100);
    }
    return check
}
function isPrime(num) {
    for (var i = 2; i <= Math.sqrt(num); i++) {
      if (num % i === 0) {
        return false;
      }
    }
    return true;
}



