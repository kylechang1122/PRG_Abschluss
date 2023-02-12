function putUser() {
    var value = this.getValue();
    var credential = btoa(value.userId + ":" + value.password)
    var data = {
        userId: value.userId,
        credential: credential,
        group: value.group,
        firstName: value.firstName,
        lastName: value.lastName
    }
    $.ajax({
        dataType: 'json',
        type: 'PUT',
        url: "/rest/admin/users/" + data.userId,
        data: JSON.stringify(data),
        success: (userData) => {
            alert("Save successful!");
            this.data = userData;
        },
        error: function (xhr) {
            alert("Save failed: " + xhr.responseText);
        }
    });
}

function postUser() {
    var value = this.getValue();
    var credential = btoa(value.userId + ":" + value.password)
    var data = {
        userId: value.userId,
        credential: credential,
        group: value.group,
        firstName: value.firstName,
        lastName: value.lastName
    }
    $.ajax({
        dataType: 'json',
        type: 'POST',
        url: "/rest/admin/users/",
        data: JSON.stringify(data),
        success: (userData) => {
            alert("Save successful!");
            this.data = userData;
        },
        error: function (xhr) {
            alert("Save failed: " + xhr.responseText);
        }
    });
}

function showUserEditor($target, submitFunction, data = {}) {
    data.password = data.credential && data.userId && atob(data.credential).replace(data.userId + ':', '');
    var schema = {
        title: "User Edit",
        type: "object",
        properties: {
            userId: {
                type: "string",
                title: "User ID",
                required: true
            },
            password: {
                type: "string",
                title: "Password",
                required: true
            },
            group: {
                type: "string",
                title: "Group",
                enum: ['user', 'manager', 'admin'],
                required: true
            },
            firstName: {
                type: "string",
                title: "First Name"
            },
            lastName: {
                type: "string",
                title: "Last Name"
            },
        }
    };
    var options = {
        fields: {
            group: {
                "optionLabels": ["User", "Manager", "Administrator"],
                type: "select",
                default: 'user',
            },
        },
        form: {
            buttons: {
                submit: {
                    click: submitFunction,
                    title: "Save"
                },
                canel: {
                    click: () => $target.html(''),
                    title: "Cancel"
                }
            }
        }
    };
    $target.alpaca({
        data: data,
        schema: schema,
        options: options
    });
}
