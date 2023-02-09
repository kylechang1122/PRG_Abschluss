function putUser() {
    var value = this.getValue();
    $.ajax({
        type: 'PUT',
        url: "/rest/admin/users/" + value.userId,
        success: function (userData) {
            this.data = userData;
        },
        error: function () {
            alert("Save failed");
        }
    });
}
function postUser() {
    var value = this.getValue();
    $.ajax({
        type: 'POST',
        url: "/rest/admin/users/" + value.userId,
        success: function (userData) {
            this.data = userData;
        },
        error: function () {
            alert("Save failed");
        }
    });
}
function showUserEditor($target, submitFunction, data = {}) {
    var schema = {
        title: "User Edit",
        type: "object",
        properties: {
            id: {
                type: "string",
            },
            userId: {
                type: "string",
                title: "User ID",
                required: true
            },
            group: {
                type: "string",
                title: "Gruppe",
                enum: ['admin', 'manager', 'user'],
                required: true
            },
            firstName: {
                type: "string",
                title: "First Name",
                required: true
            },
            lastName: {
                type: "string",
                title: "Last Name",
                required: true
            },
        }
    };
    var options = {
        fields: {
            group: {
                "optionLabels": ["Admistrator", "Manager", "User"]
            },
            id: {
                type: "hidden"
            },
        },
        form: {
            buttons: {
                submit: {
                    click: submitFunction,
                    title: "Save"
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
