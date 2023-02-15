<html>
<head>
    <#include "./include/editor-head.ftl">
    <script type="text/javascript" src="/ui/user-editor.js"></script>
    <script type="text/javascript" src="/ui/user-overview.js"></script>
    <script>

        function addUser() {
            const $target = $("#editor");
            $target.html('');
            showUserEditor($target, postUser, {});
        }
        function showUsers() {
            const $target = $("#editor");
            $target.html("");
            showUserOverview($target, postUser, {});
        }
    </script>
</head>

<body>
<div class="container">
    <h1>Welcome to Parliament Browser Editor</h1>
    <div class="row">
        <div class="col-md-4">
            <#include "./include/menu.ftl">
            <nav class="menu">
                <ul>
                    <li class="admin"><a href="#" onclick="showUsers()"> Show Users </a></li>
                    <li class="admin"><a href="#" onclick="addUser()"> Add User </a></li>
                </ul>
            </nav>
            <script>
                showMenu();
            </script>
        </div>
        <div id="editor" class="col-md-8">
        </div>
    </div>
</div>
<script>showUsers();</script>
</body>

</html>
