// global variables
function addAuthHeader(xhr) {
    // set header if authorization is set
    if (sessionStorage.auth) {
        xhr.setRequestHeader("Authorization", "Basic " + sessionStorage.auth);
    }
}

function gotToLogin(redirect) {
// Redirect the to the login page.
    location.href = "./login?redirect=" + redirect;
}

//global options for ajax requests
var globalAjaxOptions = {
    beforeSend: addAuthHeader,
    statusCode: {
        401: gotToLogin
    },
};

$.ajaxSetup(globalAjaxOptions);

function getCurrentUser(){
    return sessionStorage.user && JSON.parse(sessionStorage.user)
}

function logout() {
    sessionStorage.auth = null;
    sessionStorage.user = null;
    location.href = "./";
}

