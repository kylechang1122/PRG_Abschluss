function showCommentEditor($target, data) {
    var schema = {
        title: "Comment Bearbeiten",
        type: "object",
        properties: {
            protocolId: {
                type: "string",
                title: "Protokoll ID",
                required: true
            }, // maybe the Protocol ID can be showed automatically when we gibe TOP ID.
            agendaId: {
                type: "string",
                title: "Tagesordnungspunkt ID",
                required: true
            },
            speaker: {
                type: "string",
                title: "Render",
                required: true
            }, // want to show a list of speaker for choosing
            party: {
                type: "string",
                title: "Partei der Render",
                required: true
            }, // I want the system bring the party of the speaker automatically when we enter speaker
            content: {
                type: "string",
                title: "Kommentar",
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
