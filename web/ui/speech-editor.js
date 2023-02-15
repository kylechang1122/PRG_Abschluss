
function putSpeech(protocolId) {
    var value = this.getValue();
    var data = value;
    parseDates(data);
    $.ajax({
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

function postSpeech(protocolId) {
    var value = this.getValue();
    var data = value;
    parseDates(data);
    $.ajax({
        dataType: 'json',
        type: 'POST',
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

function showSpeechEditor(selector, submitFunction, data = {}) {
    $target = $(selector);
    // schema of Speech Edit for Alpaca
    var schema = {
        title: "Speech Edit",
        type: "object",
        properties: {
            id: {
                type: "string",
                title: "ID",
                required: true
            },
            protocolId: {
                type: "string",
                title: "Protocol ID",
                required: true
            },
            agendaId: {
                type: "string",
                title: "Agenda Item ID",
                required: true
            },
            index: {
                type: "number",
                title: "Index",
                required: true
            },
            speaker: {
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
            speaker: {
                optionLabels: enum: window.speakers ? window.speakers.map(getSpeakerName) : undefined
            }
        },
        form: {
            buttons: {
                submit: submitFunction,
                title: "Save"
            },
            cancel: {
                click: () => $target.html(''),
                title: "Cancel"
            }
        }
    }
    $target.alpaca({
        data: data,
        schema: schema,
        options: options
    });
}

function putText(protocolId, speech) {
    var value = this.getValue();
    var data = value;
    speech.texts[data.index] = data;
    $.ajax({
        dataType: 'json',
        type: 'PUT',
        url: `/rest/parliament/protocol/${protocolId}/speech/${speech.id}`,
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

function postText(protocolId, speech) {
    var value = this.getValue();
    var data = value;
    speech.texts.splice(data.index, 0, data);
    $.ajax({
        dataType: 'json',
        type: 'PUT',
        url: `/rest/parliament/protocol/${protocolId}/speech/${speech.id}`,
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


function showTextEditor(selector, submitFunction, data = {}) {
    $target = $(selector);
    // schema of Speech Edit for Alpaca
    var schema = {
        title: "Text",
        type: "object",
        properties: {
            index: {
                type: "number",
                title: "Index",
                required: true
            },
            type: {
                type: "string",
                title: "Type",
                enum: ['text', 'comment'],
                required: true
            },
            content: {
                type: "string",
                title: "Content",
                required: true
            },
        },
    };
    var options = {
        fields: {
            content: {
                type: "textarea"
            }
        },
        form: {
            buttons: {
                submit: submitFunction,
                title: "Save"
            },
            cancel: {
                click: () => $target.html(''),
                title: "Cancel"
            }
        }
    }
    $target.alpaca({
        data: data,
        schema: schema,
        options: options
    });
}

