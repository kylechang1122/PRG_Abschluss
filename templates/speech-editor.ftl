<html>
<head>
    <#include "./include/editor-head.ftl">
    <script type="text/javascript" src="/ui/agendaItem-editor.js"></script>
    <script type="text/javascript" src="/ui/protocol-editor.js"></script>
    <script type="text/javascript" src="/ui/speech-editor.js"></script>
    <script type="text/javascript" src="/ui/text-editor.js"></script>
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
    const speechNumber = new URL(location.href).searchParams.get("number");

    function showSpeech() {
        const selector = "#speech";
        $(selector).html('');
        showSpeechEditor(
            {
                selector,
                submitFunction: function() {putSpeech.call(this, protocolId)},
                data: {...window.speech, number: speechNumber},
                id: speechId
            });
    }

    function showTexts() {
        const selector = "#texts";
        $(selector).html('');
        let index = 0;
        window.speech.texts.forEach((text) => {
            const id = speechId + '-' + index;
            $(selector).append('<div id="'+id+'"></div>');
            showTextEditor({
                selector: "#" + id,
                submitFunction: function () {
                    putText.call(this, protocolId, window.speech).then(showTexts)
                },
                data: {...text, index},
                id
            });
            index++;
        });
    }

    function addText() {
        const selector = "#texts";
        $(selector).html('');
        let index = window.speech.texts.length;
        const id = speechId + '-' + index;
        showTextEditor({
            selector,
            submitFunction: function () {
                postText.call(this, protocolId, window.speech).then(showTexts)
            },
            data: {index},
            id
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
