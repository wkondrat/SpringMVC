<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title>Books</title>
</head>
<body>
	<section>
		<div class="jumbotron">
			<div class="container">
				<h1>Books</h1>
				<p>This page contains all informations about books</p>
			</div>
		</div>
	</section>

	<section class="container">
		<div class="row">
			<form:form modelAttribute="searchBook" class="form-horizontal">
				<fieldset>
					<legend>Search book</legend>

					<!-- Sample template for some fields in Book Entity -->
					<div class="form-group">
						<label class="control-label col-lg-2" for="name">Title</label>
						<div class="col-lg-10">
							<form:input id="title" path="title" type="text"
								class="form:input-large" />
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-2" for="name">Author</label>
						<div class="col-lg-10">
							<form:input id="authors" path="authors" type="text"
								class="form:input-large" />
						</div>
					</div>
				</fieldset>
				<div class="form-group">
					<div class="col-lg-offset-2 col-lg-10">
						<input type="submit" id="btnAdd" class="btn btn-primary"
							value="Search" 
							/>
					</div>
				</div>
			</form:form>
			<c:forEach items="${bookList}" var="book">
				<div class="col-sm-6 col-md-3" style="padding-bottom: 15px">
					<div class="thumbnail">
						<div class="caption">
							<h3>${book.id}</h3>
							<p>${book.title}</p>
							<p>${book.authors}</p>
							<p>Status: ${book.status}</p>
							<p>
								<a href=" <spring:url value="/books/book?id=${book.id}" /> "
									class="btn btn-primary"> <span
									class="glyphicon-info-sign glyphicon" /></span> Details
								</a>
							</p>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
		<p>
			<a href="<spring:url value="/" />" class="btn btn-default"> <span
				class="glyphicon-hand-left glyphicon"></span> back
			</a>
		</p>
	</section>
</body>
</html>
