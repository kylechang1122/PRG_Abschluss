
function showUserEditor($target, data) {
    var schema = {
        title: "User Edit",
        type: "object",
        properties: {
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
            title: {
                type: "string",
                title: "Title"
            },
            party: {
                type: "string",
                title: "Party"
            },
            fraction: {
                type: "string",
                title: "Fraction"
            },
            role: {
                type: "string",
                title: "Role"
            },
        }
    };
    var options = {
        fields: {
            group: {
                "optionLabels": ["Admistrator", "Manager", "User"]
            }
        },
        form: {
            buttons: {
                submit: {
                    click: function() {
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
                    },
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
