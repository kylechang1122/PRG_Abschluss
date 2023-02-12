
function showSpeakerEditor($target, data) {
    // schema of Speaker Edit for Alpaca
    var schema = {
        title: "Speaker Eidt",
        type: "object",
        properties: {
            id: {
                type: "string"
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
            id: {
                type: "hidden"
            },
        },
        form: {
            buttons: {
                submit: {
                    click: function() {
                        var value = this.getValue();
                        console.log(value)
                    },
                    title: "Save"
                }
                cancel: {
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
