function showSpeechEditor($target, data) {
    var schema = {
        title: "Speech Bearbeiten",
        type: "object",
        properties: {
            protocolId: {
                type: "string",
                title: "Protokoll ID",
                required: true
            },
            agendaId: {
                type: "string",
                title: "Tagesordnungspunkt ID",
                required: true
            },
            speaker: {
                type: "string",
                title: "Render",
                required: true
            },
            content: {
                type: "string",
                title: "Inhalt der Rede",
                required: true
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
