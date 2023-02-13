function showSpeakerOverview(targetId) {
    const $target = $(targetId);
    $.getJSON({
        url: "/rest/parliament/speaker/overview",
        success: function (speakers) {
            $target.html(`<h2>Speaker</h2>
                <table class='table speaker'>
                    <thead></thead>
                    <tbody></tbody>
                </table>`);
            const $table = $target.find('table.speaker');
            $table.find('thead').append(`<tr><th>Id</th><th>Name</th><th></th></tr>`)
            const $tbody = $table.find('tbody');
            speakers.forEach(s => {
                const name = `${s.role? s.role + ' ': ''}${s.title? s.title + ' ': ''}${s.firstName} ${s.name}`
                $tbody.append(`
                <tr>
                    <td>${s._id}</td>
                    <td>${name}</td>
                    <td>
                    <button onclick="editSpeaker('${s._id}')">Edit</button>
                    <button onclick="deleteProtocol('${s._id}', '${targetId}')">Delete</button>
                    </td>
                </tr>`)
                $table.append(`<tr><td id="speaker-${s._id}" colspan="4"></td></tr>`)
            })
        },
        error: function (xhr) {
            alert("Loading speakers failed: " + xhr.responseText);
        }
    });
}

function editSpeaker(id) {
    const $target = $(`#speaker-${id}`);
    $.getJSON({
        url: `/rest/parliament/speaker/${id}`,
        success: function (response) {
            parseSpeakerResponseData(response)
            showSpeakerEditor($target, putSpeaker, response);
        },
        error: function (xhr) {
            alert("Loading speaker failed: " + xhr.responseText);
        }
    });
};

function deleteSpeaker(id, targetId) {
    $.ajax({
        method: 'DELETE',
        url: `/rest/parliament/speaker/${id}`,
        success: function () {
            alert(`speaker ${id}  deleted`);
            showSpeakerOverview(targetId);
        },
        error: function (xhr) {
            alert("Deleting speaker failed: " + xhr.responseText);
        }
    });
};

