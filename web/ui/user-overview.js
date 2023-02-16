// show user overview
function showUserOverview($target) {
    $.getJSON({
        url: "/rest/admin/users/overview",
        success: function (users) {
            $target.html(`<h2>Users</h2>
                <table class='table users'>
                    <thead></thead>
                    <tbody></tbody>
                </table>`);
            const $table = $target.find('table.users');
            $table.find('thead').append(`<tr><th>Id</th><th>Vorname</th><th>Nachname</th><th></th></tr>`)
            const $tbody = $table.find('tbody');
            users.forEach(user => {
                $tbody.append(`
                <tr>
                    <td>${user.userId}</td>
                    <td>${user.firstName}</td>
                    <td>${user.lastName}</td>
                    <td>
                    <button onclick="editUser('${user.userId}')">Edit</button>
                    <button onclick="deleteUser('${user.userId}')">Delete</button>
                    </td>
                </tr>`)
                $table.append(`<tr><td id="user-${user._id}" colspan="4"></td></tr>`)
            })
        },
        error: function (xhr) {
            alert("Loading Protocols failed: " + xhr.responseText);
        }
    });
}

// edit a user
function editUser(id) {
    const $target = $(`#user-${id}`);
    $.getJSON({
        url: `/rest/admin/users/${id}`,
        success: function (user) {
            showUserEditor($target, putUser, user);
        },
        error: function (xhr) {
            alert("Loading user failed: " + xhr.responseText);
        }
    });
}

// delete a user
function deleteUser(id) {
    $.ajax({
        method: 'DELETE',
        url: `/rest/admin/users/${id}`,
        success: function () {
            alert(`user ${id}  deleted`)
        },
        error: function (xhr) {
            alert("Deleting user failed: " + xhr.responseText);
        }
    });
}
