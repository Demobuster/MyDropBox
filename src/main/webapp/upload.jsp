<%-- 
    Document   : upload
    Created on : 29.04.2015, 18:07:12
    Author     : Sergey
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="../../favicon.ico">

<title>Upload file page</title>

<link href="css/bootstrap.min.css"rel="stylesheet">
<link href="css/signin.css" rel="stylesheet">
</head>

<body>

	<p>Provide a file to upload</p>
        <form action="Uploading" enctype="multipart/form-data" method="POST">
            <input type="file" name="file1"><br>
            <input type="text" placeholder="Home/Photos" name="subPaths">
            <input type="Submit" value="Upload File"><br>
        </form>
	
	
	<!-- FOOTER -->
      <footer>
        <p class="pull-right"><a href="Service">Back to explorer</a></p>
        <p>&copy; 2015 Serzh Petukhov</p>
      </footer>
</body>
</html>


