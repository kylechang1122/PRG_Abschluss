// show protocol overview
function showProtocolOverview(targetId) {
    const $target = $(targetId);
    $.getJSON({
        url: "/rest/parliament/protocol/overview",
        success: function (protocols) {
            $target.html(`<h2>Plenaryprotocols</h2>
                <table class='table protocols'>
                    <thead></thead>
                    <tbody></tbody>
                </table>`);
            const $table = $target.find('table.protocols');
            $table.find('thead').append(`<tr><th>Id</th><th>Title</th><th>Date</th><th></th></tr>`)
            const $tbody = $table.find('tbody');
            protocols.forEach(p => {
                $tbody.append(`
                <tr>
                    <td>${p._id}</td>
                    <td>${p.title}</td>
                    <td>${p.date}</td>
                    <td>
                    <button onclick="editProtocol('${p._id}')">Edit</button>
                    <button onclick="deleteProtocol('${p._id}', '${targetId}')">Delete</button>
                    </td>
                </tr>`)
            })
        },
        error: function (xhr) {
            alert("Loading Protocols failed: " + xhr.responseText);
        }
    });
}

// call a protocol to edit
function editProtocol(id) {
    location.href= "/editor/protocol?id=" + id;
};

// delete a protocol
function deleteProtocol(id, targetId) {
    $.ajax({
        method: 'DELETE',
        url: `/rest/parliament/protocol/${id}`,
        success: function () {
            alert(`protocol ${id}  deleted`);
            showProtocolOverview(targetId);
        },
        error: function (xhr) {
            alert("Deleting protocol failed: " + xhr.responseText);
        }
    });
};

