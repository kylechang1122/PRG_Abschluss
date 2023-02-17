<html>
<head>
    <#include "./include/editor-head.ftl">
    <script type="text/javascript" src="/ui/speaker-editor.js"></script>
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
        <div class="col-md-4">
            <#include "./include/menu.ftl">
        </div>
        <div id="speaker" class="col-md-8">
        </div>
    </div>
</div>

<script>
    const id  = new URL(location.href).searchParams.get("id");

    function showSpeaker() {
        const selector = "#speaker";
        $(selector).html("");
        showSpeakerEditor(
            selector,
            function () {
                putSpeaker.call(this)
            },
            window.speaker)
    }


    function loadSpeaker() {
        return $.getJSON({
            url: "/rest/parliament/speaker/" + id,
            success: function (response) {
                window.speaker = response;
                showMenu();
                showSpeaker();
            },
            error: function (xhr) {
                alert("Loading Agenda-Item failed: " + xhr.responseText);
            }
        });
    }

    loadSpeaker();

</script>
</body>

</html>
