function toSQLDate(date) {
    // from https://stackoverflow.com/questions/5129624/convert-js-date-time-to-mysql-datetime
    return date.toISOString().slice(0, 19).replace('T', ' ');
}

function parseDates(data) {
    if (data.date) {
        data.date = new Date(Date.parse(data.date));
        //data.date = toSQLDate(data.date);
    }
    if (data.startTime) {
        data.startTime = new Date(Date.parse("1970-01-01T14:" + data.startTime));
        //data.startTime = toSQLDate(data.startTime);
    }
    if (data.endTime) {
        data.endTime = new Date(Date.parse("1970-01-01T14:" + data.endTime));
        //data.endTime = toSQLDate(data.endTime);
    }
}

function editProtocol(targetId, protocolId) {
    const $target = $(targetId);
    $.getJSON({
        url: `/rest/parliament/protocol/${protocolId}`,
        success: function (protocol) {
            showProtocolEditor($target, putProtocol, protocol);
        },
        error: function (xhr) {
            alert("Loading Protocol failed: " + xhr.responseText);
        }
    });
};

function putProtocol() {
    var value = this.getValue();
    var data = value;
    parseDates(data);
    $.ajax({
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

function postProtocol() {
    var value = this.getValue();
    var data = value;
    parseDates(data);
    $.ajax({
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

function showProtocolEditor(targetId, submitFunction, data) {
    $target = $(targetId);
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
                enum: ["19", "20"], // less than 3 options will be checkbox.
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

function showAgendaOverview(targetId, protocolId) {
    const $target = $(targetId);
    $.getJSON({
        url: `/rest/parliament/protocol/${protocolId}/agenda/overview`,
        success: function (agenda) {
            $target.html(`
                <table class='table agenda'>
                    <thead></thead>
                    <tbody></tbody>
                </table>`);
            const $table = $target.find('table.agenda');
            $table.find('thead').append(`<tr><th>Agenda</th><th>Title</th><th></th></tr>`)
            const $tbody = $table.find('tbody');
            agenda.forEach(agendaItem => {
                $tbody.append(`
                <tr>
                    <td>${agendaItem.index}</td>
                    <td>${agendaItem.title}</td>
                    <td>
                    <button onclick="editAgendaItem('${protocolId}', '${agendaItem.index}')">Edit</button>
                    <button onclick="deleteAgendaItem('${protocolId}', '${agendaItem.index}')">Delete</button>
                    </td>
                </tr>`)
                $table.append(`<tr><td data-id="${agendaItem._id}" colspan="3"></td></tr>`);
                $(`[data-id="editAgendaItem${agendaItem.index}"]`).click(() => {
                    editAgendaItem(`[data-id="${agendaItem.index}"]`, protocolId, agendaItem)
                })
            })
        },
        error: function (xhr) {
            alert("Loading Protocols failed: " + xhr.responseText);
        }
    });
}
function editAgendaItem(protocolId, agendaItemId) {
    //const url = new URL("/editor/agenda-item").searchParams.append("agendaItem", agendaItemId);
    const url = `/editor/agenda-item?id=${protocolId}&item=${agendaItemId}`
    location.href = url;
};


function deleteAgendaItem(targetId, protocolId, agendaItemIndexString) {
    $.ajax({
        method: 'DELETE',
        url: `/rest/parliament/protocol/${protocolId}/agenda-item/${agendaItemIndexString}`,
        success: function () {
            alert(`agenda Item ${agendaItemIndexString}  deleted`);
            showAgendaOverview(targetId, protocolId);
        },
        error: function (xhr) {
            alert("Deleting agenda item failed: " + xhr.responseText);
        }
    });
};
