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
    const agendaItemIndexString = new URL(location.href).searchParams.get("item");
    function showSpeeches() {
        const selector = "#speeches";
        $(selector).html("");
        showSpeechOverview(selector, protocolId, window.agendaItem);
    }

    function showAgendaItem() {
        const selector = "#agenda-item";
        $(selector).html("");
        showAgendaItemEditor(
            selector,
            function() {putAgendaItem.call(this, protocolId, agendaItemIndexString) },
            agendaItem,
            false);
    }

    function addSpeech() {
        const selector = "#speeches";
        $(selector).html("");
        const number = window.agendaItem.speeches.length;
        showSpeechEditor({
            selector,
            submitFunction: function() { postSpeech.call(this, protocolId, agendaItemIndexString).then(loadAgendaItem) },
            data: {number}
        })
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
            url: "/rest/parliament/protocol/"+protocolId+"/agenda-item/"+agendaItemIndexString,
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
