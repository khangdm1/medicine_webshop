<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <title>Nhà thuốc DKK</title>
                <link rel="stylesheet" href="/client/css/style.css">
                <link rel="preconnect" href="https://fonts.googleapis.com">
                <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
                <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
            </head>

            <body>
                <!-- HEADER -->
                <header class="header">
                    <div class="header__top">
                        <div class="header__logo">
                            <img src="https://static.pharmacity.io/logo/Pharmacity_logo_white.svg" alt="Pharmacity Logo"
                                height="50">
                        </div>
                        <div class="header__search">
                            <span class="header__search-icon">🔍</span>
                            <input type="text" placeholder="Bạn đang tìm gì hôm nay...">
                        </div>
                        <nav class="header__nav">
                            <a href="#" title="Thông báo" class="header__icon">🔔</a>
                            <a href="#" title="Giỏ hàng" class="header__icon">🛒</a>
                            <a href="#" class="header__user">
                                <span class="header__user-icon">👤</span>
                                <span>Đăng nhập/ Đăng ký</span>
                            </a>
                        </nav>
                    </div>
                    <!-- Đã loại bỏ header__menu cho đơn giản -->
                </header>
                <!-- BREADCRUMB -->
                <nav class="breadcrumb">
                    <a href="#">Trang chủ</a>
                </nav>
                <!-- MAIN CONTAINER -->
                <main>
                    <div class="container">
                        <h1>Danh mục thuốc</h1>
                        <!-- GRID DANH MỤC SẢN PHẨM -->
                        <div class="product-grid">
                            <!-- Sản phẩm 1 -->
                            <div class="product-card">
                                <img src="https://cdn.nhathuoclongchau.com.vn/unsafe/400x400/https://cms-prod.s3-sgn09.fptcloud.com/postinor_postinor_1_1_nhathuoclongchau_85b9617ad0.png"
                                    alt="Postinor 1">
                                <span class="product-name">Thuốc ngừa thai</span>
                            </div>
                            <!-- Sản phẩm 2 -->
                            <div class="product-card">
                                <img src="https://cdn.nhathuoclongchau.com.vn/unsafe/400x400/https://cms-prod.s3-sgn09.fptcloud.com/tefast_5510_6181_thumbnail_554ec3e7d4.png"
                                    alt="Telfast BD">
                                <span class="product-name">Thuốc kháng dị ứng</span>
                            </div>
                            <!-- Sản phẩm 3 -->
                            <div class="product-card">
                                <img src="https://cdn.nhathuoclongchau.com.vn/unsafe/400x400/https://cms-prod.s3-sgn09.fptcloud.com/voltarol_emulgel_16b5fcaa4f.png"
                                    alt="Voltaren">
                                <span class="product-name">Thuốc kháng viêm</span>
                            </div>
                            <!-- Sản phẩm 4 -->
                            <div class="product-card">
                                <img src="https://file.hstatic.net/200000524283/file/strepsils-1_d175b1bc991c4560b59ead43062307e2_grande.png"
                                    alt="Strepsils">
                                <span class="product-name">Thuốc cảm lạnh</span>
                            </div>
                            <!-- Sản phẩm 5 -->
                            <div class="product-card">
                                <img src="https://nhathuocphuongchinh.com/wp-content/uploads/2021/05/odistad-120.png"
                                    alt="Odistad120">
                                <span class="product-name">Thuốc giảm cân</span>
                            </div>
                            <!-- Sản phẩm 6 -->
                            <div class="product-card">
                                <img src="https://static.pharmacity.io/product/8983/rohto-mat.jpg" alt="Rohto">
                                <span class="product-name">Thuốc Mắt/Tai/Mũi</span>
                            </div>
                            <!-- Sản phẩm 7 -->
                            <div class="product-card">
                                <img src="https://cdn.nhathuoclongchau.com.vn/unsafe/400x400/https://cms-prod.s3-sgn09.fptcloud.com/enterogermina_0008_s1_09.png"
                                    alt="Enterogermina">
                                <span class="product-name">Thuốc tiêu hóa</span>
                            </div>
                            <!-- Sản phẩm 8 -->
                            <div class="product-card">
                                <img src="https://cdn.nhathuoclongchau.com.vn/unsafe/400x400/https://cms-prod.s3-sgn09.fptcloud.com/nam_k1g7q_7cfbb1e463.png"
                                    alt="Nam">
                                <span class="product-name">Thuốc dành cho nam</span>
                            </div>
                            <!-- Sản phẩm 9 -->
                            <div class="product-card">
                                <img src="https://cdn.nhathuoclongchau.com.vn/unsafe/400x400/https://cms-prod.s3-sgn09.fptcloud.com/efferalgan-500mghop-16-vien_05e14d6e43.png"
                                    alt="Efferalgan">
                                <span class="product-name">Giảm đau, hạ sốt</span>
                            </div>
                            <!-- Sản phẩm 10 -->
                            <div class="product-card">
                                <img src="https://cdn.nhathuoclongchau.com.vn/unsafe/400x400/https://cms-prod.s3-sgn09.fptcloud.com/lamisil_creme_tuyt_fdd1ed98e2.png"
                                    alt="Lamisil">
                                <span class="product-name">Thuốc da liễu</span>
                            </div>
                            <!-- Sản phẩm 11 -->
                            <div class="product-card">
                                <img src="https://cdn.nhathuoclongchau.com.vn/unsafe/400x400/https://cms-prod.s3-sgn09.fptcloud.com/nabifar-nabifar-1_nhathuoclongchau_34d58b2d95.png"
                                    alt="Nabifar">
                                <span class="product-name">Thuốc dành cho phụ nữ</span>
                            </div>
                            <!-- Sản phẩm 12 -->
                            <div class="product-card">
                                <img src="https://file.hstatic.net/200000230725/file/hoat-huyet-duong-nao-cua-traphaco-62d7c7d48012440e9b6d518403961681_1024x1024.png"
                                    alt="Hoạt huyết dưỡng não">
                                <span class="product-name">Thuốc thần kinh</span>
                            </div>
                            <!-- Sản phẩm 13 -->
                            <div class="product-card">
                                <img src="https://cdn.nhathuoclongchau.com.vn/unsafe/400x400/https://cms-prod.s3-sgn09.fptcloud.com/salonpas_salonpas_1_nhathuoclongchau_86e6e1910d.png"
                                    alt="Salonpas">
                                <span class="product-name">Thuốc cơ xương khớp</span>
                            </div>
                            <!-- Sản phẩm 14 -->
                            <div class="product-card">
                                <img src="https://cdn.nhathuoclongchau.com.vn/unsafe/400x400/https://cms-prod.s3-sgn09.fptcloud.com/hop-tiger-balm-19_979be288304e4f14b1600a8470284a35.jpg"
                                    alt="Tiger Balm">
                                <span class="product-name">Dầu, Cao xoa bóp...</span>
                            </div>
                            <!-- Sản phẩm 15 -->
                            <div class="product-card">
                                <img src="https://cdn.nhathuoclongchau.com.vn/unsafe/400x400/https://cms-prod.s3-sgn09.fptcloud.com/logspatat_nhathuoclongchau_cdc2be1e78.png"
                                    alt="Logspatat">
                                <span class="product-name">Thuốc khác</span>
                            </div>
                            <!-- Sản phẩm 16 -->
                            <div class="product-card">
                                <img src="https://nhathuoclongchau.com.vn/ckfinder/userfiles/images/photo/vitamin-c-1000mg-pureway-c-1.jpg"
                                    alt="Vitamin C">
                                <span class="product-name">Vitamin & Khoáng chất</span>
                            </div>
                        </div>
                    </div>
                </main>

                <!-- FOOTER PHARMACITY -->
                <footer class="footer">
                    <div class="footer__main">
                        <div class="footer__col footer__brand">
                            <img src="https://static.pharmacity.io/logo/Pharmacity_logo_white.svg"
                                alt="Pharmacycity logo nhỏ">
                            <div class="footer__slogan">Tiên phong vì sức khỏe cộng đồng</div>
                            <div class="footer__cert">
                                <img src="https://same-assets.com/pharmacy/gov-cert.png" alt="Chứng nhận bộ công thương"
                                    height="38">
                            </div>
                        </div>
                        <div class="footer__col footer__contact">
                            <h3>Liên hệ</h3>
                            <ul>
                                <li>Hotline: <a href="tel:18006821">1800 6821</a></li>
                                <li>Email: <a href="mailto:cs@pharmacity.vn">cs@pharmacity.vn</a></li>
                                <li>Địa chỉ: 248A Nơ Trang Long, P.12, Q. Bình Thạnh, TP.HCM</li>
                            </ul>
                            <div class="footer__social">
                                <a href="#" aria-label="Facebook" class="footer__icon"><img
                                        src="https://cdn-icons-png.flaticon.com/512/733/733547.png"
                                        alt="Facebook" /></a>
                                <a href="#" aria-label="Zalo" class="footer__icon"><img
                                        src="https://cdn-icons-png.flaticon.com/512/1177/1177077.png" alt="Zalo" /></a>
                                <a href="#" aria-label="Youtube" class="footer__icon"><img
                                        src="https://cdn-icons-png.flaticon.com/512/1384/1384060.png"
                                        alt="Youtube" /></a>
                            </div>
                        </div>
                        <div class="footer__col footer__links">
                            <h3>Chính sách & Hỗ trợ</h3>
                            <ul>
                                <li><a href="#">Chính sách giao hàng</a></li>
                                <li><a href="#">Đổi trả & Hoàn tiền</a></li>
                                <li><a href="#">Hướng dẫn mua hàng</a></li>
                                <li><a href="#">Tra cứu đơn hàng</a></li>
                                <li><a href="#">Tuyển dụng</a></li>
                                <li><a href="#">Hệ thống cửa hàng</a></li>
                            </ul>
                        </div>
                        <!-- Đã xóa cột đăng ký nhận tin -->
                    </div>
                    <div class="footer__meta">
                        <div>Copyright © 2024 Pharmacity. All rights reserved.</div>
                        <div class="footer__partners">
                            <img src="https://same-assets.com/pharmacy/viettelpost.png" alt="Viettel Post"
                                height="26" />
                            <img src="https://same-assets.com/pharmacy/ghn-logo.png" alt="GHN" height="22" />
                            <img src="https://same-assets.com/pharmacy/giao-hang-tiet-kiem-logo.png" alt="GHTK"
                                height="21" />
                        </div>
                    </div>
                </footer>

                <script src="/client/js/main.js"></script>
            </body>

            </html>