<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                <title>Dashboard</title>
                <link href="/css/styles.css" rel="stylesheet" />
                <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

                <script>
                    $(document).ready(() => {
                        const avatarFile = $("#imageFile");
                        avatarFile.change(function (e) {
                            const imgURL = URL.createObjectURL(e.target.files[0]);
                            $("#imagePreview").attr("src", imgURL);
                            $("#imagePreview").css({ "display": "block" });
                        });
                    });
                </script>

            </head>

            <body class="sb-nav-fixed">
                <jsp:include page="../layout/header.jsp" />
                <div id="layoutSidenav">
                    <jsp:include page="../layout/sidebar.jsp" />
                    <div id="layoutSidenav_content">
                        <main>
                            <div class="container-fluid px-4">
                                <h1 class="mt-4">Create Product</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Create</li>
                                </ol>
                                <div class="container mt-5">
                                    <div class="row">
                                        <div class="col-md-6 col-12 mx-auto">
                                            <h2>Create a product</h2>
                                            <hr />
                                            <form:form method="post" action="/admin/product/create"
                                                modelAttribute="newProduct" enctype="multipart/form-data">
                                                <div class="mb-3">
                                                    <c:set var="errorName">
                                                        <form:errors path="name" cssClass="invalid-feedback" />
                                                    </c:set>
                                                    <label class="form-label">Name:</label>
                                                    <form:input type="text"
                                                        class="form-control ${not empty errorName ? 'is-invalid' : ''}"
                                                        path="name" />
                                                    ${errorName}
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label">Description:</label>
                                                    <form:input type="text" class="form-control" path="description" />
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label">Manufacturer:</label>
                                                    <form:input type="text" class="form-control" path="manufacturer" />
                                                </div>
                                                <div class="mb-3">
                                                    <c:set var="errorPrice">
                                                        <form:errors path="price" cssClass="invalid-feedback" />
                                                    </c:set>
                                                    <label class="form-label">Price:</label>
                                                    <form:input type="Price"
                                                        class="form-control ${not empty errorPrice ? 'is-invalid' : ''}"
                                                        path="price" />
                                                    ${errorPrice}
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label">Amount:</label>
                                                    <form:input type="number" class="form-control" path="stock" />
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label">Create at:</label>
                                                    <jsp:useBean id="now" class="java.util.Date" scope="page" />
                                                    <fmt:formatDate var="today" value="${now}" pattern="yyyy-MM-dd" />

                                                    <form:input type="text" class="form-control" path="create_at"
                                                        value="${today}" />
                                                </div>
                                                <div class="mb-3 col-12 col-md-6">
                                                    <c:set var="errorImg">
                                                        <form:errors path="img" cssClass="invalid-feedback" />
                                                    </c:set>
                                                    <label for="imageFile" class="form-label">Image:</label>
                                                    <input
                                                        class="form-control ${not empty errorImg ? 'is-invalid' : ''}"
                                                        type="file" id="imageFile" accept=".png, .jpg, .jpeg, .webp"
                                                        name="imageFile" />
                                                    ${errorImg}
                                                </div>
                                                <div class="col-12 mb-3">
                                                    <img style="max-height: 250px; display: none;" alt="image preview"
                                                        id="imagePreview" />
                                                </div>

                                                <button type="submit" class="btn btn-primary">Create</button>
                                            </form:form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </main>
                        <jsp:include page="../layout/footer.jsp" />
                    </div>
                </div>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                    crossorigin="anonymous"></script>
                <script src="js/scripts.js"></script>

            </body>

            </html>