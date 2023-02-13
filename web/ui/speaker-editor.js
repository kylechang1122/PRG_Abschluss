function parseSpeakerRequestData(data) {
    // party is object in backend
    if (data.party) {
        data.party = {name: data.party};
    }
    // fraction is object in backend
    if (data.fraction) {
        data.fraction = {name: data.fraction};
    }
}

function parseSpeakerResponseData(data) {
    // party is object in backend
    if (data.party && data.party.name) {
        data.party = data.party.name;
    }
    // fraction is object in backend
    if (data.fraction && data.fraction.name) {
        data.fraction = data.fraction.name;
    }
}

function putSpeaker() {
    var value = this.getValue();
    var data = value;
    parseSpeakerRequestData(data);
    $.ajax({
        dataType: 'json',
        type: 'PUT',
        url: "/rest/parliament/speaker/" + data.id,
        data: JSON.stringify(data),
        success: (response) => {
            alert("Save successful!");
            this.data = parseSpeakerResponseData(response);
        },
        error: function (xhr) {
            alert("Save failed: " + xhr.responseText);
        }
    });
}

function postSpeaker() {
    var value = this.getValue();
    var data = value;
    parseSpeakerRequestData(data)
    $.ajax({
        dataType: 'json',
        type: 'POST',
        url: "/rest/parliament/speaker",
        data: JSON.stringify(data),
        success: (response) => {
            alert("Save successful!");
            this.data = parseSpeakerResponseData(response);
        },
        error: function (xhr) {
            alert("Save failed: " + xhr.responseText);
        }
    });
}
function showSpeakerEditor($target, submitFunction, data = {}) {
    // schema of Speaker Edit for Alpaca
    var schema = {
        title: "Speaker Eidt",
        type: "object",
        properties: {
            id: {
                type: "string"
            },
            firstName: {
                type: "string",
                title: "First Name",
                required: true
            },
            name: {
                type: "string",
                title: "Last Name",
                required: true
            },
            academicTitle: {
                type: "string",
                title: "Title"
            },
            party: {
                type: "string",
                title: "Party"
            },
            fraction: {
                type: "string",
                title: "Fraction"
            },
            role: {
                type: "string",
                title: "Role"
            },
        }
    };
    var options = {
        fields: {
            id: {
                type: "hidden"
            },
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
