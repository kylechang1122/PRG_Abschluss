function showProtocolEditor($target, data) {
    var schema = {
        title: "Sitzung Protocol Bearbeiten",
        type: "object",
        properties: {
            id: {
                type: "string",
                title: "ID",
                required: true
            },
            electionperiod: {
                type: "string",
                title: "Wahlperiode",
                required: true,
            },
            date: {
                type: "string",
                title: "Datum der Sitzung",
                required: true,
            },
            startTime: {
                type: "string",
                title: "Anfangsziet",
                required: true,
            },
            endTime: {
                type: "string",
                title: "Endzeit",
                required: true,
            },
            place: {
                type: "string",
                title: "Ort der Sitzung",
                required: true,
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
