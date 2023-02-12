<html>
<head>
    <#include "./include/editor-head.ftl">
    <script type="text/javascript" src="ui/speaker-editor.js"></script>
    <style>
        #menu > li {
            display: none;
        }
    </style>
    <script>
        function clearEditor() {
            $("#editor").html("");
        }
        function showAddSpeaker() {
            clearEditor();
            showSpeakerEditor($("#editor"), {});
        }
        function showEditSpeaker() {
            clearEditor();
            showSpeakerEditor($("#editor"), {});
        }
        function showSpeaker() {
            clearEditor();
            showSpeakerOverview($("#editor"), {});
        }
        function showDeleteSpeaker() {
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
                    <li class="manager"><a href="#" onclick="showAddSpeaker()"> Add Speaker </a></li>
                    <li class="manager"><a href="#" onclick="showEditSpeaker()"> Edit Speaker </a></li>
                    <li class="manager"><a href="#" onclick="showSpeaker()"> Show Speaker </a></li>
                    <li class="manager"><a href="#" onclick="showDeleteSpeaker()"> Delete Speaker </a></li>
                </ul>
            </nav>
        </div>
        <div id="editor" class="col-md-8">
        </div>
    </div>
</div>

<script>

    evaluateUser();

</script>
</body>

</html>
