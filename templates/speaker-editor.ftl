<html>
<head>
    <#include "./include/editor-head.ftl">
    <script type="text/javascript" src="ui/speaker-editor.js"></script>
    <script>
        function clearEditor() {
            $("#editor").html("");
        }
        function showAddSpeaker() {
            clearEditor();
            showSpeakerEditor($("#editor"), {});
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
                    <li class="admin"><a href="#" onclick="showAddSpeaker()"> Add User </a></li>
                </ul>
            </nav>
            <script>
                evaluateUser();
            </script>
        </div>
        <div id="editor" class="col-md-8">
        </div>
    </div>
</div>

</body>

</html>
