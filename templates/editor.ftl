<html>
<head>
    <title> Parliament Browser - Login </title>
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
    <style>
        #editor > div{
            display: none;
        }
    </style>
    <script>

        function evaluateUser() {
            var user = sessionStorage.user;
            if (!user) {
                gotToLogin();
            } else {
                switch (user.group) {
                    case "user":
                        $("#user-area").show();
                        break;
                    case "manager":
                        $("#user-area, #manager-area").show();
                        break;
                    case "admin":
                        $("#user-area, #manager-area, #admin-area").show();
                        break;
                }
            }
        }
    </script>
</head>

<body>
<h1>Welcome to Parliament Browser Editor</h1>
<div id="editor" >
    <h2>Editor</h2>
    <div id="user-area" >
        <h3>User Area</h3>
    </div>
    <div id="manager-area" >
        <h3>Managwer Area</h3>
    </div>
    <div id="admin-area">
        <h3>Admin Area</h3>
        <div id="user-editor"></div>
    </div>
</div>
<script>

    // evaluateUser();

    // show user editor for test purpose
    function showFakeUserEditor(){
        $("#admin-area").show();
        showUserEditor($("#user-editor"), {});
        showSpeakerEditor($("#user-editor"), {firstName: "Kyle", lastName: "Chang"});
    }

    showFakeUserEditor();

</script>

</body>


</html>
