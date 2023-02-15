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

        const protocolId = redirect = new URL(location.href).searchParams.get("id");

        function showAgenda() {
            $("#agenda").html("");
            showAgendaOverview("#agenda", protocolId);
        }

        function showProtocol() {
            $("#protocol").html("");
            editProtocol("protocol", protocolId);
        }

    </script>

</head>

<body>
<div class="container">
    <h1>Welcome to Parliament Browser Editor</h1>
    <div class="row">
        <div class="col-md-2">
            <#include "./include/menu.ftl">
        </div>
        <div id="protocol" class="col-md-10">
        </div>
        <div id="agenda" class="col-md-10">
        </div>
    </div>
</div>


<script>
    showMenu();
    showAgenda();
    showProtocol();

</script>

</body>


</html>
