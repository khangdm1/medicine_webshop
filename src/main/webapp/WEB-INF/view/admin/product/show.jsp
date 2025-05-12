<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>


            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Table Product</title>
                <link rel="stylesheet" href="/admin/css/style.css">
                <!-- Latest compiled and minified CSS -->
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

                <!-- Latest compiled JavaScript -->
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>


                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                <!-- <link href="/css/demo.css" rel="stylesheet"> -->

            </head>

            <body>
                <jsp:include page="../layout/header.jsp" />
                <div class="main-content">
                    <div class="container mt-5">
                        <div class="row">
                            <div class="col-12 mx-auto">
                                <div class="d-flex justify-content-between">
                                    <h3>Table products</h3>
                                    <a href="/admin/product/create" class="btn btn-primary">Create a product</a>
                                </div>

                                <hr />
                                <table class=" table table-bordered table-hover">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Name</th>
                                            <th>Manufacturer</th>
                                            <th>Price</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="pr" items="${product}">

                                            <tr>
                                                <th>${pr.id}</th>
                                                <td>${pr.name}</td>
                                                <td>${pr.manufacturer}</td>
                                                <td>${pr.price}</td>
                                                <td>
                                                    <a href="/admin/product/${pr.id}" class="btn btn-success">View</a>
                                                    <a href="/admin/product/update/${pr.id}"
                                                        class="btn btn-warning  mx-2">Update</a>
                                                    <a href="/admin/product/delete/${pr.id}"
                                                        class="btn btn-danger">Delete</a>
                                                </td>
                                            </tr>

                                        </c:forEach>

                                    </tbody>
                                </table>
                            </div>

                        </div>

                    </div>
                </div>
                <jsp:include page="../layout/footer.jsp" />

            </body>

            </html>