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
                    <li class="manager"><a href="#" onclick="addText()"> Add Text </a></li>
                    <li class="manager"><a href="#" onclick="showTexts()"> Show Texts </a></li>
                </ul>
            </nav>
        </div>
        <div class="col-md-10">
            <div id="speech" >
            </div>
            <div id="texts">
            </div>
        </div>
    </div>
</div>


<script>

    const protocolId = new URL(location.href).searchParams.get("id");
    const agendaItemId = new URL(location.href).searchParams.get("item");
    const speechId = new URL(location.href).searchParams.get("speech");
    function showTexts() {
        const selector = "#speeches";
        window.speech.texts.forEach((text) => {
            showTextEditor(selector, () => {putText(protocolId, window.speech.id)}, text)
        });
    }

    function showSpeech() {
        const selector = "#speech";
        showSpeechEditor(
            selector,
            () => putSpeech(protocolId),
            window.speech);
    }

    function addText() {
        const selector = "#texts";
        showTextEditor(selector, () => {postText(protocolId, window.speech)}, data = {})
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

   function loadSpeech(){
        return  $.getJSON({
            url: "/rest/parliament/protocol/"+protocolId+"/speech/"+speechId,
            success: function (response) {
                window.speech = response;
                showMenu();
                showTexts();
                showSpeech();
            },
            error: function (xhr) {
                alert("Loading Agenda-Item failed: " + xhr.responseText);
            }
        });
   }

   loadSpeakers().then(loadSpeech);
</script>

</body>


</html>
