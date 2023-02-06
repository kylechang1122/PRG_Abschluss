function showProtocolEditor($target, data) {
    var schema = {
        title: "Sesseion Edit",
        type: "object",
        properties: {
            id: {
                type: "string",
                title: "ID",
                required: true
            },
            electionperiod: {
                type: "string",
                title: "Election Period",
                required: true,
            },
            date: {
                type: "string",
                title: "Date of Sesseion",
                required: true,
            },
            startTime: {
                type: "string",
                title: "Start Time",
                required: true,
            },
            endTime: {
                type: "string",
                title: "End Time",
                required: true,
            },
            place: {
                type: "string",
                title: "Location of Session",
                required: true,
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
