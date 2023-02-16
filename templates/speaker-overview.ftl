<html>
<head>
    <#include "./include/editor-head.ftl">
    <script type="text/javascript" src="/ui/speaker-editor.js"></script>
    <script type="text/javascript" src="/ui/speaker-overview.js"></script>
    <style>
        #menu > li {
            display: none;
        }
    </style>
    <script>
        function clearEditor() {
            $("#editor").html("");
        }
        function addSpeaker() {
            clearEditor();
            showSpeakerEditor("#editor", {});
        }
        function showSpeakers() {
            clearEditor();
            showSpeakerOverview("#editor", {});
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
                    <li class="manager"><a href="#" onclick="showSpeakers()"> Show Speakers </a></li>
                    <li class="manager"><a href="#" onclick="addSpeaker()"> Add Speaker </a></li>
                </ul>
            </nav>
        </div>
        <div id="editor" class="col-md-8">
        </div>
    </div>
</div>

<script>

    showMenu();
    showSpeakers();

</script>
</body>

</html>
