function putAgendaItem(protocolId, agendaItemIndexString) {
    var value = this.getValue();
    var data = value;
    var number = data.number;
    $.ajax({
        dataType: 'json',
        type: 'PUT',
        url: `/rest/parliament/protocol/${protocolId}/agenda-item/${agendaItemIndexString}/${number}"`,
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

function postAgendaItem(protocolId) {
    var value = this.getValue();
    var data = value;
    var number = data.number;
    $.ajax({
        dataType: 'json',
        type: 'POST',
        url: `/rest/parliament/protocol/${protocolId}/agenda/${number}"`,
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

function showAgendaItemEditor(selector, submitFunction, data = {}) {
    $target= $(selector);
    // schema of Agenda Item Edit for Alpaca
    var schema = {
        title: `${data._id? data._id: 'New Agenda Item'}`,
        type: "object",
        properties: {
            number: {
                type: "number",
                title: "Number",
                required: true,
                "minimum": 0,
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
