
// submit-function for protocol editor that PUTs protocol data to the backend
function putProtocol() {
    var value = this.getValue();
    var data = value;
    const {agendaItems} = protocol;
    return $.ajax({
        dataType: 'json',
        type: 'PUT',
        url: "/rest/parliament/protocol/" + data.id,
        data: JSON.stringify(data),
        success: (userData) => {
            alert("Save successful!");
            this.data = userData;
        },
        error: function (xhr) {
            alert("Save failed: " + xhr.responseText);
        }
    });
}

// submit-function for protocol editor that POSTs protocol data to the backend
function postProtocol() {
    var value = this.getValue();
    var data = value;
    parseDatesOut(data);
    return $.ajax({
        dataType: 'json',
        type: 'POST',
        url: "/rest/parliament/protocol",
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

// show protocol editor
function showProtocolEditor(selector, submitFunction, data = {}) {
    $target = $(selector);
    var schema = {
        title: `${data.title ? data.title : 'New Protocol'}`,
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
                enum: ["19", "20"],
                required: true,
            },
            date: {
                type: "string", // want a picker but "date" not working
                title: "Date of Session (yyyy-mm-dd)",
                required: true,

            },
            startTime: {
                type: "string",
                title: "Start Time (hh:mm)",
                required: true,
            },
            endTime: {
                type: "string",
                title: "End Time (hh:mm)",
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
        focus: "",
        fields: {
            electionperiod: {
                type: "select",
                label: "select the election period",

            },
            id: {
                type: "hidden"
            }

        },
        form: {
            buttons: {
                submit: {
                    click: submitFunction,
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

// show agenda overview
function showAgendaOverview(selector, protocol) {
    const $target = $(selector);
    $target.html(`
                <table class='table agenda'>
                    <thead></thead>
                    <tbody></tbody>
                </table>`);
    const $table = $target.find('table.agenda');
    $table.find('thead').append(`<tr><th>Agenda</th><th>Title</th><th></th></tr>`)
    const $tbody = $table.find('tbody');
    protocol.agendaItems.forEach(agendaItem => {
        $tbody.append(`
                <tr>
                    <td>${agendaItem.index}</td>
                    <td>${agendaItem.title}</td>
                    <td>
                    <button onclick="editAgendaItem('${protocolId}', '${agendaItem.id}')">Edit</button>
                    <button onclick="deleteAgendaItem('${protocolId}', '${agendaItem.id}')">Delete</button>
                    </td>
                </tr>`)
    })
}

// call agenda item to edit
function editAgendaItem(protocolId, agendaItemId) {
    const url = `/editor/agenda-item?id=${protocolId}&item=${agendaItemId}`
    location.href = url;
};

// delete an agenda item
function deleteAgendaItem(protocolId, agendaItemId) {
    $.ajax({
        method: 'DELETE',
        url: `/rest/parliament/protocol/${protocolId}/agenda-item/${agendaItemId}`,
        success: function () {
            alert(`agenda Item ${agendaItemId}  deleted`);
            location.reload();
        },
        error: function (xhr) {
            alert("Deleting agenda item failed: " + xhr.responseText);
        }
    });
};
