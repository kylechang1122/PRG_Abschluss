function logout() {
    sessionStorage.auth = null;
    sessionStorage.user = null;
}

function authenticate(userId, password) {
    $.getJSON({
        url: "/authenticate",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(userId + ":" + password));
        },
        success: function (userData) {
            sessionStorage.auth = btoa(userId + ":" + password);
            sessionStorage.user = userData;
            evaluateUser();
        },
        error: function () {
            alert("Login fehlgeschlagen!");
        }
    });
}

function showLogin($target) {
    var schema = {
        "title": "Login",
        "type": "object",
        "properties": {
            "userId": {
                "type": "string",
                "title": "Benutzername"
            },
            "password": {
                "type": "string",
                "format": "password",
                "title": "Passwort"
            }
        }
    };
    var options = {
        "form": {
            "buttons": {
                "submit": {
                    "click": function() {
                        var value = this.getValue();
                        authenticate(value.userId, value.password);
                    },
                    "title": "Login"
                }
            }
        }
    };
    $target.alpaca({
        "schema": schema,
        "options": options
    });
}
