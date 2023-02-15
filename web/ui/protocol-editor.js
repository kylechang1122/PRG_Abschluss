function toSQLDate(date) {
    // from https://stackoverflow.com/questions/5129624/convert-js-date-time-to-mysql-datetime
    return date.toISOString().slice(0, 19).replace('T', ' ');
}

function parseDates(data) {
    if(data.date) {
        data.date = new Date(Date.parse(data.date));
        //data.date = toSQLDate(data.date);
    }
    if(data.startTime) {
        data.startTime = new Date(Date.parse("1970-01-01T14:" + data.startTime));
        //data.startTime = toSQLDate(data.startTime);
    }
    if(data.endTime) {
        data.endTime = new Date(Date.parse("1970-01-01T14:" + data.endTime));
        //data.endTime = toSQLDate(data.endTime);
    }
}

function editProtocol(id) {
    const $target = $(`#protocol-${id}`);
    $.getJSON({
        url: `/rest/parliament/protocol/${id}`,
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
function showProtocolEditor($target, submitFunction,  data) {
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
        url: `/rest/parliament/protocol/${protocolId}/agenda`,
        success: function (agenda) {
            $target.html(`<h2>${agenda[0].protocolTitle}</h2>
                <table class='table agenda'>
                    <thead></thead>
                    <tbody></tbody>
                </table>`);
            const $table = $target.find('table.agenda');
            $table.find('thead').append(`<tr><th>Index</th><th>Title</th><th></th></tr>`)
            const $tbody = $table.find('tbody');
            agenda.forEach(p => {
                $tbody.append(`
                <tr>
                    <td>${p._id}</td>
                    <td>${p.title}</td>
                    <td>
                    <button onclick="editAgendaItem('${protocolId}', '${p._id}')">Edit</button>
                    <button onclick="deleteAgenda('${protocolId}', '${p._id}')">Delete</button>
                    </td>
                </tr>`)
                $table.append(`<tr><td data-id="${p._id}" colspan="3"></td></tr>`)
            })
        },
        error: function (xhr) {
            alert("Loading Protocols failed: " + xhr.responseText);
        }
    });
}
