function showAgendaItemEditor($target, data) {
    var schema = {
        title: "Agenda Item Bearbeiten",
        type: "object",
        properties: {
            protocolId: {
                type: "string",
                title: "Protokoll ID",
                required: true
            },
            index: {
                type: "integer",
                title: "Index",
                required: true,
                "minimum": 1,
                "maximum": 99,
            },
            title: {
                type: "string",
                title: "Titel",
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
