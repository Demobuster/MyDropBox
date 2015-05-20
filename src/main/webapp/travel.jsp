<%-- 
    Document   : travel
    Created on : 28.04.2015, 15:27:57
    Author     : Sergey
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="ru">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="favicon.ico">
<title>Files List</title>
<link href="css/bootstrap.min.css" rel="stylesheet" />
<link href="css/style.css" rel="stylesheet">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="carousel.html">MyDropBox</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse"
				aria-expanded="false" style="height: 0.800000011920929px;">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="upload.jsp">UploadFiles</a></li>
					<li><a href="LogoutServlet?logout=true">Exit</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12 main">
				<h1 class="page-header">Explorer</h1>

				<h2 class="sub-header">Files List</h2>
				<div class="table-responsive">
					<table class="table table-striped">
						<thead>
							<tr>
								<th>File</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${ sessionScope.currentFoldersToDisplay }" var="element">
								<tr>
									<td><a href="HierarchyTraversing?elementToFind=${element}">${element}</a>

										<c:forEach items="${element}" var="element">
											<c:if test="${fn:containsIgnoreCase(element, '.')}">
												<a class="btn btn-default" href="Cleaner?urlToDelete=${sessionScope.travelPath}${element}"
													role="button"> <img src="images/delete.png" border="0" alt="Delete"></a>
											</c:if>
										</c:forEach>
										
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- FOOTER -->
	<footer>
		<p class="pull-right"><a href="Service">Back to list</a></p>
		<p>&copy; 2015 Serzh Petukhov</p>
	</footer>

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script type='text/javascript' src='js/bootstrap.min.js'></script>

</body>
</html>