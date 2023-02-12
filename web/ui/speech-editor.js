function showSpeechEditor($target, data) {
    // schema of Speech Edit for Alpaca
    var schema = {
        title: "Speech Edit",
        type: "object",
        properties: {
            protocolId: {
                type: "string",
                title: "Protocol ID",
                required: true
            },
            agendaId: {
                type: "string",
                title: "Agenda Item ID",
                required: true
            },
            index: {
                type: "number",
                title: "Index",
                required: true
            },
            speaker: {
                type: "string",
                title: "Speaker",
                required: true
            },
            content: {
                type: "string",
                title: "Content of Speech",
                required: true
            },
        },
    };
    var options = {
        fields: {
            content: {
                type: "textarea"
            }
        },
        form: {
            buttons: {
                submit: {
                    click: function() {
                        var value = this.getValue();
                        $.ajax({
                            type: 'PUT',
                            url: "/rest/parliament/speech/" + value.userId, //wrong not finished
                            success: function (userData) {
                                this.data = userData;
                            },
                            error: function () {
                                alert("Save failed");
                            }
                        });
                        console.log(value)
                    },
                    title: "Save"
                },
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
