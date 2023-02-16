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

// submit-function for speaker editor that PUTs speaker data to the backend
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

// submit-function for speaker editor that POSTs speaker data to the backend
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
// show speaker editor
function showSpeakerEditor($target, submitFunction, data = {}) {
    // schema of Speaker Edit for Alpaca
    var schema = {
        title: `${data.name? getSpeakerName(data): 'New Speaker'}`,
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
            title: {
                type: "string",
                title: "Title"
            },
            birthdate: {
                type: "string",
                title: "Birthdate"
            },
            birthplace: {
                type: "string",
                title: "Place of Birth"
            },
            deathdate: {
                type: "string",
                title: "Date of Death"
            },
            gender: {
                type: "string",
                title: "Gender"
            },
            profession: {
                type: "string",
                title: "Profession"
            },
            academicTitle: {
                type: "string",
                title: "Academic Title"
            },
            maritalStatus: {
                type: "string",
                title: "Marital Status"
            },
            religion: {
                type: "string",
                title: "Religion"
            },
            image: {
                type: "string",
                format: "uri",
                title: "Photo"
            },
            party: {
                type: "string",
                title: "Party"
            },
            fraction: {
                type: "string",
                title: "Fraction"
            },
        }
    };
    var options = {
        focus: "",
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
