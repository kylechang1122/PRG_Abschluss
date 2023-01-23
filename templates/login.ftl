<html>
<head>
<title> Parliament Browser - Login </title>
    <script src="jquery-3.6.2.js"></script>
    <style>
        .login-form {
            font-size: 1.5em;
            background: green;
            color: white;
        }
        #login-button {
            font-size: 1.5em;
            background: pink;
            color: green;
        }
        form.login-form button{
            background: blue!important;
        }
    </style>
</head>

<body>
<h1>Welcome to Parliament Browser</h1>
<h3> Please enter your User ID and Password to log in. </h3>
<form class="login-form">
<label for="userid">User ID:</label><br>
<input type="text" id="userid" name="userid"/><br>
<label for="password">Password:</label><br>
<input type="password" id="password" name="password"/><br>
<br>
    <button id="login-button" value="Submit to log in">
        Login
    </button>

</form>

<script>
    var hash = null;
    function authenticate() {
        var userId = $("#userid").val();
        var password = $("#password").val();
        var authString = userId + ":" + password;
        hash = btoa(unescape(encodeURIComponent(authString)));

        alert("Your hash is " + hash);
    }
    $("form.login-form button").click(authenticate);

</script>

</body>


</html>
