<html>
<head>
    <#include "./include/editor-head.ftl">
</head>
<script>
    function clearEditor() {
        $("#editor").html("");
    }
    function showAddUser() {
        clearEditor();
        showUserEditor($("#editor"), postUser, {});
    }
</script>

<body>
<div class="container">
    <h1>Welcome to Parliament Browser Editor</h1>
    <div class="row">
        <div class="col-md-4">
            <#include "./include/menu.ftl">
            <script>
                showMenu();
            </script>
        </div>
        <div id="editor" class="col-md-8">
        </div>
    </div>
</div>

</body>


</html>
