//Существует много способов выбрать DOM узел; здесь мы получаем форму и электронную почту
//поле ввода, а также элемент span, в который мы поместим сообщение об ошибке.
var mainFrame = document.getElementsByTagName('div');
console.log(mainFrame)
var form = document.getElementsByTagName('form');
console.log(form)
var sum = mainFrame.getElementById('input-field1');
console.log(sum)
var toCard = document.getElementsByClassName('input-field')[1];
var fromCard = document.getElementsByClassName('input-field')[2];
var validthru = document.getElementsByClassName('input-field')[3];
var cvv = document.getElementsByClassName('input-field')[4];
var name = document.getElementsByClassName('input-field')[5];
var error = document.querySelector('.error');

sum.addEventListener("input", function (event) {
    // Каждый раз, когда пользователь вводит что-либо, мы проверяем,
     // является ли корректным поле электронной почты.
     if (sum.validity.valid) {
       // В случае появления сообщения об ошибке, если поле
       // является корректным, мы удаляем сообщение об ошибке.
       error.innerHTML = ""; // Сбросить содержимое сообщения
       error.className = "error"; // Сбросить визуальное состояние сообщения
     }
   }, false);


toCard.addEventListener("input", function (event) {

     if (toCard.validity.valid) {
       error.innerHTML = "";
       error.className = "error"; 
     }
   }, false);


   fromCard.addEventListener("input", function (event) {

    if (fromCard.validity.valid) {
      error.innerHTML = "";
      error.className = "error"; 
    }
  }, false);


  validthru.addEventListener("input", function (event) {

    if (validthru.validity.valid) {
      error.innerHTML = "";
      error.className = "error"; 
    }
  }, false);


  cvv.addEventListener("input", function (event) {

    if (cvv.validity.valid) {
      error.innerHTML = "";
      error.className = "error"; 
    }
  }, false);


  name.addEventListener("input", function (event) {

    if (name.validity.valid) {
      error.innerHTML = "";
      error.className = "error"; 
    }
  }, false);



form.addEventListener("submit", function (event) {
  // Каждый раз, когда пользователь пытается отправить данные, мы проверяем
   // валидность поля электронной почты.
  if (!sum.validity.valid) {
    
    // Если поле невалидно, отображается пользовательское
    // сообщение об ошибке.
    error.innerHTML = "I expect an e-mail, darling!";
    error.className = "error active";
    // И мы предотвращаем отправку формы путем отмены события
    event.preventDefault();
  }
}, false);


// const form  = document.getElementsByTagName('form')[0];

// const email = document.getElementById('mail');
// const emailError = document.querySelector('#mail + span.error');

// email.addEventListener('input', function (event) {
//   // Each time the user types something, we check if the
//   // form fields are valid.

//   if (email.validity.valid) {
//     // In case there is an error message visible, if the field
//     // is valid, we remove the error message.
//     emailError.innerHTML = ''; // Reset the content of the message
//     emailError.className = 'error'; // Reset the visual state of the message
//   } else {
//     // If there is still an error, show the correct error
//     showError();
//   }
// });

// form.addEventListener('submit', function (event) {
//   // if the email field is valid, we let the form submit

//   if(!email.validity.valid) {
//     // If it isn't, we display an appropriate error message
//     showError();
//     // Then we prevent the form from being sent by canceling the event
//     event.preventDefault();
//   }
// });

// function showError() {
//   if(email.validity.valueMissing) {
//     // If the field is empty
//     // display the following error message.
//     emailError.textContent = 'You need to enter an e-mail address.';
//   } else if(email.validity.typeMismatch) {
//     // If the field doesn't contain an email address
//     // display the following error message.
//     emailError.textContent = 'Entered value needs to be an e-mail address.';
//   } else if(email.validity.tooShort) {
//     // If the data is too short
//     // display the following error message.
//     emailError.textContent = `Email should be at least ${ email.minLength } characters; you entered ${ email.value.length }.`;
//   }

//   // Set the styling appropriately
//   emailError.className = 'error active';
// }