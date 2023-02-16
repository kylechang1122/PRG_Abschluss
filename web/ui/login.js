// authenticate login
function authenticate(userId, password) {
    $.getJSON({
        url: "/authenticate",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + btoa(userId + ":" + password));
        },
        success: function (userData) {
            sessionStorage.auth = btoa(userId + ":" + password);
            sessionStorage.user = JSON.stringify(userData);
            redirect = new URL(location.href).searchParams.get("redirect");
            if(redirect) {
                location.href= "./" + redirect;
            }
        },
        error: function () {
            alert("Login failed!");
        }
    });
}

// show login
function showLogin($target) {
    // schema of Login for Alpaca
    var schema = {
        "title": "Login",
        "type": "object",
        "properties": {
            "userId": {
                "type": "string",
                "title": "User ID"
            },
            "password": {
                "type": "string",
                "format": "password",
                "title": "Password"
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
