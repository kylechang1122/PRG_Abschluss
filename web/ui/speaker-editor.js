
function showSpeakerEditor($target, data) {
    var schema = {
        title: "Redner Bearbeiten",
        type: "object",
        properties: {
            firstName: {
                type: "string",
                title: "Vorname",
                required: true
            },
            lastName: {
                type: "string",
                title: "Nachname",
                required: true
            },
            title: {
                type: "string",
                title: "Titel"
            },
        }
    };
    var options = {
        fields: {
        },
        form: {
            buttons: {
                submit: {
                    click: function() {
                        var value = this.getValue();
                        console.log(value)
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
