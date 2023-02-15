<html>
<head>
    <#include "./include/editor-head.ftl">
    <script type="text/javascript" src="/ui/agendaItem-editor.js"></script>
    <script type="text/javascript" src="/ui/protocol-editor.js"></script>
    <script type="text/javascript" src="/ui/speech-editor.js"></script>
    <script type="text/javascript" src="/ui/comment-editor.js"></script>
    <style>
        #menu > li {
            display: none;
        }
    </style>
    <script>

    </script>

</head>

<body>
<div class="container">
    <h1>Welcome to Parliament Browser Editor</h1>
    <div class="row">
        <div class="col-md-2">
            <#include "./include/menu.ftl">
            <nav class="menu">
                <ul>
                    <li class="manager"><a href="#" onclick="addSpeech()"> Add Speech </a></li>
                    <li class="manager"><a href="#" onclick="showSpeeches()"> Show Speeches </a></li>
                </ul>
            </nav>
        </div>
        <div class="col-md-10">
            <div id="agenda-item" >
            </div>
            <div id="speeches">
            </div>
        </div>
    </div>
</div>


<script>

    const protocolId = new URL(location.href).searchParams.get("id");
    const agendaItemId = new URL(location.href).searchParams.get("item");
    function showSpeeches() {
        const selector = "#speeches";
        showSpeechOverview(selector, protocolId, window.agendaItem);
    }

    function showAgendaItem() {
        const selector = "#agenda-item";
        showAgendaItemEditor(
            selector,
            () => putAgendaItem(protocolId, window.agendaItem._id),
            agendaItem);
    }

    function addSpeech() {
        const selector = "#speeches";
        showSpeechEditor(selector, () => {postSpeech(protocolId, window.agendaItem._id)}, data = {})
    }

    function loadSpeakers(){
        return $.getJSON({
            url: "/rest/parliament/speaker/overview",
            success: function (response) {
                window.speakers = response;
            },
            error: function (xhr) {
                alert("Loading speakers failed: " + xhr.responseText);
            }
        });
    }

   function loadAgendaItem(){
        return  $.getJSON({
            url: "/rest/parliament/protocol/"+protocolId+"/agenda-item/"+agendaItemId,
            success: function (response) {
                window.agendaItem = response;
                showMenu();
                showAgendaItem();
                showSpeeches();
            },
            error: function (xhr) {
                alert("Loading Agenda-Item failed: " + xhr.responseText);
            }
        });
   }

   loadSpeakers().then(loadAgendaItem);
</script>

</body>


</html>
