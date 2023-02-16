
function putText(protocolId, speech) {
    var value = this.getValue();
    var data = value;
    speech.texts[data.index] = data;
    return $.ajax({
        dataType: 'json',
        type: 'PUT',
        url: `/rest/parliament/protocol/${protocolId}/speech/${speech.id}`,
        data: JSON.stringify(speech),
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
    return $.ajax({
        dataType: 'json',
        type: 'PUT',
        url: `/rest/parliament/protocol/${protocolId}/speech/${speech.id}`,
        data: JSON.stringify(speech),
        success: (response) => {
            alert("Save successful!");
            this.data = response;
        },
        error: function (xhr) {
            alert("Save failed: " + xhr.responseText);
        }
    });
}


function showTextEditor(options) {
    const {selector, submitFunction, data = {}, id = "text"} = options;
    const $target = $(selector);
    // schema of Speech Edit for Alpaca
    var schema = {
        title: "Paragraph",
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
            text: {
                type: "string",
                title: "Content",
                required: true
            },
        },
    };
    var options = {
        fields: {
            text: {
                type: "textarea",
            },
            type: {
                "optionLabels": ["Text", "Comment"],
                type: "select",
                default: 'text',
            },
        },
        form: {
            buttons: {
                submit: {
                    click: submitFunction,
                    title: "Save",
                    id
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

