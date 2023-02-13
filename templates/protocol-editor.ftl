<html>
<head>
    <#include "./include/editor-head.ftl">
    <script type="text/javascript" src="/ui/agendaItem-editor.js"></script>
    <script type="text/javascript" src="/ui/protocol-editor.js"></script>
    <script type="text/javascript" src="/ui/speech-editor.js"></script>
    <script type="text/javascript" src="/ui/comment-editor.js"></script>
    <script type="text/javascript" src="/ui/protocol-overview.js"></script>
    <style>
        #menu > li {
            display: none;
        }
    </style>
    <script>

        function clearEditor() {
            $("#editor").html("");
        }

        function showAddProtocol() {
            clearEditor();
            showProtocolEditor($("#editor"), postProtocol, {
                date: "2020-12-25",
                startTime: "13:00",
                endTime: "14:00",
                place: "Berlin",
                title: "Sitzungsprotokoll"
            });
        }

        function showProtocols() {
            clearEditor();
            showProtocolOverview("#editor", {});
        }

        function showAddSpeech() {
            clearEditor();
            showSpeechEditor($("#editor"), {});
        }

        function showAddComment() {
            clearEditor();
            showCommentEditor($("#editor"), {});
        }

        function showAddAgendaItem() {
            clearEditor();
            showAgendaItemEditor($("#editor"), {});
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
                    <li class="manager"><a href="#" onclick="showAddProtocol()"> Add Protocol </a></li>
                    <li class="user"><a href="#" onclick="showProtocols()"> Show Protocols </a></li>
                    <li class="user"><a href="#" onclick="showAddAgendaItem()"> Add Agenda Item </a></li>
                    <li class="user"><a href="#" onclick="showAddSpeech()"> Add Speech </a></li>
                    <li class="user"><a href="#" onclick="showAddComment()"> Add Comment </a></li>
                </ul>
            </nav>
        </div>
        <div id="editor" class="col-md-8">
        </div>
    </div>
</div>


<script>

    showProtocols();
    evaluateUser();

</script>

</body>


</html>
