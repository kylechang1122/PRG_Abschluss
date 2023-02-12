function showProtocolEditor($target, data) {
    // schema of Session Edit for Alpaca
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
                type: "select",
                title: "Election Period",
                enum: ["19", "20"], // less than 3 options will be checkbox.
                required: true,
            },
            date: {
                type: "string", // want a picker but "date" not working
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
            electionperiod: {
                type: "select",
                label: "select the election period",

            }

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
