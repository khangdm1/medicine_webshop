<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Medicine Shop</title>
                <!-- <link rel="stylesheet" href="task3_lab2.css"> -->
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
                    integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
                    crossorigin="anonymous">
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
                    integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
                    crossorigin="anonymous"></script>
                <style>
                    .navbar-dark {
                        background-color: #333;
                    }

                    .btn-custom {
                        margin: 5px;
                    }

                    .card-img-top {
                        height: 100%;
                        object-fit: cover;
                    }

                    .discount-badge {
                        position: absolute;
                        top: 10px;
                        left: 10px;
                        background-color: red;
                        color: white;
                        padding: 3px 6px;
                        border-radius: 5px;
                    }

                    .product-card:hover {
                        transform: scale(1.05);
                        transition: 0.3s;
                    }

                    .footer-links a {
                        color: white;
                        margin: 0 10px;
                        text-decoration: none;
                    }
                </style>
            </head>

            <body>

                <nav class="navbar navbar-expand-lg navbar-dark">
                    <div class="container">
                        <a class="navbar-brand" href="#">Medicine Shop</a>
                        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                            data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false"
                            aria-label="Toggle navigation">
                            <span class="navbar-toggler-icon"></span>
                        </button>
                        <div class="collapse navbar-collapse" id="navbarNav">
                            <ul class="navbar-nav me-auto">
                                <li class="nav-item"><a class="nav-link" href="#">Trang chủ</a></li>
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="#" id="languageDropdown" role="button"
                                        data-bs-toggle="dropdown" aria-expanded="false">
                                        Tiếng Việt
                                    </a>
                                    <ul class="dropdown-menu" aria-labelledby="languageDropdown">
                                        <li><a class="dropdown-item" href="#">Tiếng Việt</a></li>
                                        <li><a class="dropdown-item" href="#">Tiếng Anh</a></li>
                                    </ul>
                                </li>
                                <li class="nav-item"><a class="nav-link" href="#">Liên hệ</a></li>
                            </ul>
                            <button class="btn btn-warning">&#128722 Giỏ hàng <span
                                    class="badge bg-danger">2</span></button>
                        </div>
                    </div>
                </nav>

                <div class="container text-center mt-3">
                    <button class="btn btn-outline-primary btn-custom">&#9989 Hàng chọn giá hời</button>
                    <button class="btn btn-outline-warning btn-custom">&#127994 Mã giảm giá</button>
                    <button class="btn btn-outline-success btn-custom">&#128666 Miễn phí ship</button>
                    <button class="btn btn-outline-info btn-custom">&#127756 Giờ Săn Sale</button>
                    <button class="btn btn-outline-secondary btn-custom">&#127760 Hàng Quốc Tế</button>
                    <button class="btn btn-outline-dark btn-custom">&#128187 Nạp Thẻ & Dịch Vụ</button>
                </div>

                <div class="container text-center">
                    <div class="row row-cols-4">
                        <c:forEach var="product" items="${products}">
                            <div class="col mt-3">
                                <div class="card h-100">
                                    <div class="position-relative">
                                        <span class="discount-badge">-10%</span>
                                        <img src="/images/product/${product.img}" class="card-img-top"
                                            alt="Product Image">
                                    </div>
                                    <div class="card-body">
                                        <h5 class="card-title">${product.name}</h5>
                                        <p class="card-text">${product.price}</p>
                                        <button class="btn btn-primary">Mua ngay</button>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <footer class="bg-dark text-white text-center py-3 mt-4">
                    <div class="container">
                        <div class="footer-links">
                            <a href="#">Chính sách bảo hành</a> |
                            <a href="#">Chính sách đổi trả</a> |
                            <a href="#">Chính sách giao hàng</a> |
                            <a href="#">Chính sách bảo mật</a>
                        </div>
                        <p> Địa chỉ: 123 Đường ABC, Quận 1, TP.HCM</p>
                        <p>&copy; 2025 Shop Capybara - All rights reserved.</p>
                    </div>
                </footer>
            </body>

            </html>