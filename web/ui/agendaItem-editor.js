function getSpeakerNameById(id) {
    const s = window.speakers ? window.speakers.find((s) => s._id === id) : undefined;
    if (s) {
        return getSpeakerName(s);
    }
}

function putAgendaItem(protocolId, agendaItemIndexString) {
    var value = this.getValue();
    var data = value;
    var number = data.number;
    $.ajax({
        dataType: 'json',
        type: 'PUT',
        url: `/rest/parliament/protocol/${protocolId}/agenda-item/${agendaItemIndexString}/${number}`,
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
        url: `/rest/parliament/protocol/${protocolId}/agenda/${number}`,
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

function showAgendaItemEditor(selector, submitFunction, data = {}, canceable = true) {
    const $target = $(selector);
    // schema of Agenda Item Edit for Alpaca
    var schema = {
        title: `${data.index ? data.index : 'New Agenda Item'}`,
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
            title: {
                type: "textarea"
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
    if (canceable) {
        options.form.buttons.cancel = {
            click: () => $target.html(''),
            title: "Cancel"
        };
    }
    $target.alpaca({
        data: data,
        schema: schema,
        options: options
    });
}

function showSpeechOverview(selector, protocolId, agendaItem) {
    const $target = $(selector);
    $target.append(`<h4>Speeches</h4>
                <table class='table agendaItem'>
                    <thead></thead>
                    <tbody></tbody>
                </table>`);
    const $table = $target.find('table.agendaItem');
    $table.find('thead').append(`<tr><th>ID</th><th>Speaker</th><th></th></tr>`)
    const $tbody = $table.find('tbody');
    let number = 0;
    agendaItem.speeches.forEach(speech => {
        $tbody.append(`
                <tr>
                    <td>${speech.id}</td>
                    <td>${speech.speakerRole ? speech.speakerRole + ' ' : ''}${getSpeakerNameById(speech.speakerId)}</td>
                    <td>
                    <button onclick="editSpeech('${selector}', '${protocolId}', '${agendaItem.index}', '${speech.id}', ${number})">Edit</button>
                    <button onclick="deleteSpeech('${selector}', '${protocolId}', '${speech.id}')">Delete</button>
                    </td>
                </tr>`)
        number++;
    })
}

function deleteSpeech(selector, protocolId, speechId) {
    return $.ajax({
        method: 'DELETE',
        url: `/rest/parliament/protocol/${protocolId}/speech/${speechId}`,
        success: function () {
            alert(`speech ${speechId}  deleted`);
            location.reload();
        },
        error: function (xhr) {
            alert("Deleting speech failed: " + xhr.responseText);
        }
    });
};

function editSpeech(selector, protocolId, agendaItemIndexString, speechId, number) {
    const url = `/editor/speech?id=${protocolId}&item=${agendaItemIndexString}&speech=${speechId}&number=${number}`
    location.href = url;
};

