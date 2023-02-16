function getSpeakerNameById(id){
    const s = window.speakers? window.speakers.find((s) => s._id === id): undefined;
    if(s) {
        return getSpeakerName(s);
    }
}

// update an agenda item
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

// create an agenda item
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

// show agenda item editor
function showAgendaItemEditor(selector, submitFunction, data = {}) {
    $target= $(selector);
    // schema of Agenda Item Edit for Alpaca
    var schema = {
        title: `${data.index? data.index: 'New Agenda Item'}`,
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
    agendaItem.speeches.forEach(speech => {
        $tbody.append(`
                <tr>
                    <td>${speech.id}</td>
                    <td>${speech.speakerRole? speech.speakerRole + ' ' : ''}${getSpeakerNameById(speech.speakerId)}</td>
                    <td>
                    <button onclick="editSpeech('${selector}', '${protocolId}', '${agendaItem.index}', '${speech.id}')">Edit</button>
                    <button onclick="deleteSpeech('${selector}', '${protocolId}', '${speech.id}')">Delete</button>
                    </td>
                </tr>`)
        $table.append(`<tr><td data-id="speech-editor-${speech.id}" colspan="3"></td></tr>`);
    })
}

function deleteSpeech(selector, protocolId, speechId) {
    $.ajax({
        method: 'DELETE',
        url: `/rest/parliament/protocol/${protocolId}/speech/${speechId}`,
        success: function () {
            alert(`speech ${speechId}  deleted`);
            showAgendaOverview(selector, protocolId);
        },
        error: function (xhr) {
            alert("Deleting speech failed: " + xhr.responseText);
        }
    });
};

function editSpeech(selector, protocolId, agendaItemIndexString, speechId) {
    const url = `/editor/speech?id=${protocolId}&item=${agendaItemIndexString}&speech=${speechId}`
    location.href = url;
};

