<html>
<head>
    <title> Parliament Browser - Editor </title>
    <script src="jquery-3.6.2.js"></script>
    <script type="text/javascript"
            src="//cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.5/handlebars.min.js"></script>
    <link type="text/css" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" rel="stylesheet"/>
    <script type="text/javascript" src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>

    <!-- alpaca -->
    <link type="text/css" href="//cdn.jsdelivr.net/npm/alpaca@1.5.27/dist/alpaca/bootstrap/alpaca.min.css"
          rel="stylesheet"/>
    <script type="text/javascript"
            src="//cdn.jsdelivr.net/npm/alpaca@1.5.27/dist/alpaca/bootstrap/alpaca.min.js"></script>
    <script type="text/javascript" src="global.js"></script>
    <script type="text/javascript" src="ui/user-editor.js"></script>
    <script type="text/javascript" src="ui/speaker-editor.js"></script>
    <script type="text/javascript" src="ui/agendaItem-editor.js"></script>
    <script type="text/javascript" src="ui/protocol-editor.js"></script>
    <script type="text/javascript" src="ui/speech-editor.js"></script>
    <script type="text/javascript" src="ui/comment-editor.js"></script>
    <style>
        #menu > li {
            display: none;
        }
    </style>
    <script>

        function clearEditor() {
            $("#editor").html("");
        }

        function showAddUser() {
            clearEditor();
            showUserEditor($("#editor"), postUser, {});
        }

        function showAddProtocol() {
            clearEditor();
            showProtocolEditor($("#editor"), {});
        }

        function showAddSpeaker() {
            clearEditor();
            showSpeakerEditor($("#editor"), {});
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

        function showUserMenu() {
            $("#menu li.user").show();
        }

        function showManagerMenu() {
            $("#menu li.manager").show();
        }

        function showAdminMenu() {
            $("#menu li.admin").show();
        }

        function evaluateUser() {
            var user = getCurrentUser();
            if (!user || !user.group) {
                gotToLogin("editor");
            } else {
                switch (user.group) {
                    case "user":
                        showUserMenu();
                        break;
                    case "manager":
                        showUserMenu();
                        showManagerMenu();
                        break;
                    case "admin":
                        showUserMenu();
                        showManagerMenu();
                        showAdminMenu();
                        break;
                }
            }
        }

        evaluateUser();
    </script>

</head>

<body>
<div class="container">
    <h1>Welcome to Parliament Browser Editor</h1>
    <div class="row">
        <div class="col-md-4">
            <button onclick="logout()">Logout</button>
            <nav id="menu">
                <ul>
                    <li class="admin"><a href="#" onclick="showAddUser()"> Add User </a></li>
                    <li class="manager"><a href="#" onclick="showAddProtocol()"> Add Protocol </a></li>
                    <li class="manager"><a href="#" onclick="showAddAgendaItem()"> Add Agenda Item </a></li>
                    <li class="manager"><a href="#" onclick="showAddSpeaker()"> Add Speaker </a></li>
                    <li class="manager"><a href="#" onclick="showAddSpeech()"> Add Speech </a></li>
                    <li class="manager"><a href="#" onclick="showAddComment()"> Add Comment </a></li>
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
