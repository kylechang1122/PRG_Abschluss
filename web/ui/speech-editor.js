// submit-function for speech editor that PUTs speech data to the backend
function putSpeech(protocolId) {
    var value = this.getValue();
    var data = value;
    parseDates(data);
    return $.ajax({
        dataType: 'json',
        type: 'PUT',
        url: `/rest/parliament/protocol/${protocolId}/speech/${data.id}`,
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

// submit-function for speech editor that POSTs speech data to the backend
function postSpeech(protocolId, agendaItemIndexString) {
    var value = this.getValue();
    var data = value;
    parseDates(data);
    return $.ajax({
        dataType: 'json',
        type: 'POST',
        url: `/rest/parliament/protocol/${protocolId}/agenda-item/${agendaItemIndexString}/speeches/${data.number}`,
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

// show speech editor
function showSpeechEditor(options) {
    const {selector, submitFunction, data = {}, id} = options;
    const $target = $(selector);
    // schema of Speech Edit for Alpaca
    var schema = {
        title: `${data.id? 'Speech ' + data.id: 'New Speech'}`,
        type: "object",
        properties: {
            id: {
                type: "string",
                title: "ID",
            },
            number: {
                type: "number",
                title: "Number",
                required: true
            },
            speakerId: {
                type: "string",
                title: "Speaker",
                required: true,
                enum: window.speakers ? window.speakers.map(s => s._id) : undefined
            }
        },
    };
    var options = {
        fields: {
            content: {
                type: "textarea"
            },
            id: {
                type: "hidden",
            },
            speakerId: {
                optionLabels: window.speakers ? window.speakers.map(getSpeakerName) : undefined
            }
        },
        form: {
            buttons: {
                submit: {
                    click: submitFunction,
                    title: "Save",
                    id
                },
                cancel: {
                    click: () => $target.html(''),
                    title: "Cancel"
                }
            }
        }
    }
    $target.alpaca({
        data: data,
        schema: schema,
        options: options
    });
}
