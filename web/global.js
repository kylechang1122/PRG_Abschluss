// global variables
function addAuthHeader(xhr) {
    // set header if authorization is set
    if (sessionStorage.auth) {
        xhr.setRequestHeader("Authorization", "Basic " + sessionStorage.auth);
    }
}

function gotToLogin() {
// Redirect the to the login page.
    location.href = "./login";
}

//global options for ajax requests
var globalAjaxOptions = {
    beforeSend: addAuthHeader,
    statusCode: {
        401: gotToLogin
    }
};

$.ajaxSetup(globalAjaxOptions);
