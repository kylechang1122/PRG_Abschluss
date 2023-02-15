// global variables
function addAuthHeader(xhr) {
    // set header if authorization is set
    if (sessionStorage.auth) {
        xhr.setRequestHeader("Authorization", "Basic " + sessionStorage.auth);
    }
}

function gotToLogin(redirect = "home") {
// Redirect the to the login page.
    location.href = "/login?redirect=" + redirect;
}

//global options for ajax requests
var globalAjaxOptions = {
    beforeSend: addAuthHeader,
    statusCode: {
        401: () => {
            redirect = new URL(location.href).searchParams.get("redirect");
            gotToLogin(redirect)
        }
    },
};

$.ajaxSetup(globalAjaxOptions);

function getCurrentUser(){
    return sessionStorage.user && JSON.parse(sessionStorage.user)
}

function logout() {
    sessionStorage.auth = null;
    sessionStorage.user = null;
    location.href = "/";
}

function showUserMenu() {
    $(".menu li.user").show();
}

function showManagerMenu() {
    $(".menu li.manager").show();
}

function showAdminMenu() {
    $(".menu li.admin").show();
}

function showMenu() {
    var user = getCurrentUser();
    if (!user || !user.group) {
        gotToLogin("editor");
    } else {
        switch (user.group) {
            case "user":
                showUserMenu();
                break;
            case "manager":
                showUserMenu();
                showManagerMenu();
                break;
            case "admin":
                showUserMenu();
                showManagerMenu();
                showAdminMenu();
                break;
        }
    }
}

function getSpeakerName(s){
    return `${s.title? s.title + ' ': ''}${s.firstName} ${s.name}`;
};
// taken from https://stackoverflow.com/questions/5525071/how-to-wait-until-an-element-exists
function waitForElm(selector) {
    return new Promise(resolve => {
        if (document.querySelector(selector)) {
            return resolve(document.querySelector(selector));
        }

        const observer = new MutationObserver(mutations => {
            if (document.querySelector(selector)) {
                resolve(document.querySelector(selector));
                observer.disconnect();
            }
        });

        observer.observe(document.body, {
            childList: true,
            subtree: true
        });
    });
}
