function showProtocolOverview($target) {
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
                    <td><button onclick="editProtocol(${p._id})">Edit</button></td>
                </tr>`)
                $table.append(`<tr id="protocol-${p._id}"></tr>`)
            })
        },
        error: function (xhr) {
            alert("Loading Protocols failed: " + xhr.responseText);
        }
    });
}

function editProtocol(id) {
    const $target = $(`#protocol-${id}`);
    $.getJSON({
        url: `/rest/parliament/protocol/${id}`,
        success: function (protocol) {
            showProtocolEditor($target, protocol);
        },
        error: function (xhr) {
            alert("Loading Protocol failed: " + xhr.responseText);
        }
    });
}
