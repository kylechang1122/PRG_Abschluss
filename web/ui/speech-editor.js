// update a speech
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

// create a speech
function postSpeech(protocolId, agendaItemIndexString) {
    var value = this.getValue();
    var data = value;
    parseDates(data);
    $.ajax({
        dataType: 'json',
        type: 'POST',
        url: `/rest/parliament/protocol/${protocolId}/agenda/${agendaItemIndexString}/${data.number}`,
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
function showSpeechEditor(selector, submitFunction, data = {}) {
    const $target = $(selector);
    // schema of Speech Edit for Alpaca
    var schema = {
        title: "Speech Edit",
        type: "object",
        properties: {
            id: {
                type: "string",
                title: "ID",
            },
            number: {
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
            id: {
                type: "hidden",
            },
            speaker: {
                optionLabels: window.speakers ? window.speakers.map(getSpeakerName) : undefined
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

// update speech text
function putText(protocolId, speechId) {
    var value = this.getValue();
    var data = value;
    speech.texts[data.index] = data;
    $.ajax({
        dataType: 'json',
        type: 'PUT',
        url: `/rest/parliament/protocol/${protocolId}/speech/${speechId}`,
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

// create a speech text
function postText(protocolId, speechId) {
    var value = this.getValue();
    var data = value;
    speech.texts.splice(data.index, 0, data);
    $.ajax({
        dataType: 'json',
        type: 'PUT',
        url: `/rest/parliament/protocol/${protocolId}/speech/${speechId}`,
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

// show speech text editor
function showTextEditor(selector, submitFunction, data = {}) {
    const $target = $(selector);
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

