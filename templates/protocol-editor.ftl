<html>
<head>
    <#include "./include/editor-head.ftl">
    <script type="text/javascript" src="/ui/agendaItem-editor.js"></script>
    <script type="text/javascript" src="/ui/protocol-editor.js"></script>
    <script type="text/javascript" src="/ui/speech-editor.js"></script>
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
                    <li class="manager"><a href="#" onclick="addAgendaItem()"> Add Agenda Item </a></li>
                    <li class="manager"><a href="#" onclick="showAgenda()"> Show Agenda </a></li>
                </ul>
            </nav>
        </div>
        <div class="col-md-10">
            <div id="protocol">
            </div>
            <div id="agenda">
            </div>
        </div>
    </div>
</div>


<script>
    const protocolId = new URL(location.href).searchParams.get("id");

    function showAgenda() {
        const selector = "#agenda";
        $(selector).html("");
        showAgendaOverview(selector, window.protocol);
    }

    function showProtocol() {
        const selector = "#protocol";
        $(selector).html("");
        showProtocolEditor(
            selector,
            function () {
                putProtocol.call(this).then(() => location.reload())
            },
            window.protocol)
    }

    function addAgendaItem() {
        const selector = "#agenda";
        $(selector).html("");
        const number = window.protocol.agendaItems.length;
        showAgendaItemEditor(
            selector,
            function () {
                postAgendaItem.call(this, protocolId).then(() => location.reload())
            },
            {number}
        );
    }

    function loadProtocol() {
        return $.getJSON({
            url: "/rest/parliament/protocol/" + protocolId,
            success: function (response) {
                window.protocol = response;
                showMenu();
                showProtocol();
                showAgenda();
            },
            error: function (xhr) {
                alert("Loading Agenda-Item failed: " + xhr.responseText);
            }
        });
    }

    loadProtocol();
</script>

</body>


</html>
