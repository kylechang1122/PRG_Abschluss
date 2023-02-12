function showAgendaItemEditor($target, data) {
    // schema of Agenda Item Edit for Alpaca
    var schema = {
        title: "Agenda Item Edit",
        type: "object",
        properties: {
            protocolId: {
                type: "string",
                title: "Protocol ID",
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
                title: "Title",
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
