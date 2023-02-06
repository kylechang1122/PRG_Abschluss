function showCommentEditor($target, data) {
    var schema = {
        title: "Comment Edit",
        type: "object",
        properties: {
            protocolId: {
                type: "string",
                title: "Protocol ID",
                required: true
            }, // maybe the Protocol ID can be showed automatically when we gibe TOP ID.
            agendaId: {
                type: "string",
                title: "Agenda Item ID",
                required: true
            },
            speaker: {
                type: "string",
                title: "Speaker",
                required: true
            }, // want to show a list of speaker for choosing
            party: {
                type: "string",
                title: "Speaker's Party",
                required: true
            }, // I want the system bring the party of the speaker automatically when we enter speaker
            content: {
                type: "string",
                title: "Comment",
            },
        },
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
