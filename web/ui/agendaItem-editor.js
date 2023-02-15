function putText(protocol) {
    var value = this.getValue();
    var data = value;
    protocol.agendaItems[data.number] = data;
    $.ajax({
        dataType: 'json',
        type: 'PUT',
        url: `/rest/parliament/protocol/${protocol.id}`,
        data: JSON.stringify(data),
        success: (response) => {
            alert("Save successful!");
            this.data = response;
        },
        error: function (xhr) {
            alert("Save failed: " + xhr.responseText);
        }
    });
}

function postText(protocol) {
    var value = this.getValue();
    var data = value;
    protocol.agendaItems.splice(data.number, 0, data);
    $.ajax({
        dataType: 'json',
        type: 'POST',
        url: `/rest/parliament/protocol/${protocol.id}`,
        data: JSON.stringify(data),
        success: (response) => {
            alert("Save successful!");
            this.data = response;
        },
        error: function (xhr) {
            alert("Save failed: " + xhr.responseText);
        }
    });
}

function showAgendaItemEditor($target, submitFunction, data = {}) {
    // schema of Agenda Item Edit for Alpaca
    var schema = {
        title: "Agenda Item Edit",
        type: "object",
        properties: {
            number: {
                type: "number",
                title: "Number",
                required: true,
                "minimum": 1,
            },
            index: {
                type: "string",
                title: "Index",
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
