
function showUserEditor($target, data) {
    var schema = {
        title: "Benutzer Bearbeiten",
        type: "object",
        properties: {
            userId: {
                type: "string",
                title: "Benutzername",
                required: true
            },
            group: {
                type: "string",
                title: "Gruppe",
                enum: ['admin', 'manager', 'user'],
                required: true
            }
        }
    };
    var options = {
        fields: {
            group: {
                "optionLabels": ["AdmistratorIn", "ManagerIn", "BenutzerIn"]
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
                                alert("Senden fehlgeschlagen");
                            }
                        });
                    },
                    title: "Abschicken"
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
