<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" href="../../favicon.ico">

        <title>REGISTRATION PAGE</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/signin.css" rel="stylesheet">
    </head>

    <body>

        <div class="container">
            <form class="form-signin" action="RegistrationServlet" method="post">
                <!-- <h2 class="form-signin-heading">You don't have an account<br>Please sign up</h2> -->
                <h2 class="featurette-heading">You don't have an account <span class="text-muted">Please sign up</span></h2>
                <label for="inputEmail" class="sr-only">Username</label>
                <input type="text" name="username" id="inputEmail" class="form-control" placeholder="Username" required autofocus>
                <label for="inputPassword" class="sr-only">Password</label>
                <input type="password" name="password" id="inputPassword" class="form-control" placeholder="Password" required>
                <button class="btn btn-lg btn-primary btn-block" type="submit">Sign up</button>
            </form>
        </div> 
        
        <c:if test="${ param.error != null }">
            <script type="text/javascript">
                alert("Wrong input. Re-try with another input data. Remember, username and password can have 0-9, latin symbols, underscore, hyphen with length at least 3 characters and maximum length of 15");
            </script>
        </c:if>

    </body>
</html>
