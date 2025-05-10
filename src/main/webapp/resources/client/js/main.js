// ======= 1. SHOW/HIDE MENU DẠNG MOBILE =========
const menuBtn = document.querySelector('.header__menu-btn');
const menuLinks = document.querySelector('.header__menu-links');
if (menuBtn && menuLinks) {
    menuBtn.addEventListener('click', function () {
        menuLinks.classList.toggle('menu-active');
        menuBtn.classList.toggle('menu-opened');
    });
    // Đóng menu khi click ra ngoài (mobile)
    document.addEventListener('click', function (e) {
        if (!menuLinks.contains(e.target) && !menuBtn.contains(e.target)) {
            menuLinks.classList.remove('menu-active');
            menuBtn.classList.remove('menu-opened');
        }
    });
}

// ======= 2. NÚT CUỘN LÊN ĐẦU TRANG =========
const scrollBtn = document.createElement('button');
scrollBtn.className = 'scroll-top-btn';
scrollBtn.innerHTML = '↑';
scrollBtn.title = 'Lên đầu trang';
document.body.appendChild(scrollBtn);
scrollBtn.style.display = 'none';

window.addEventListener('scroll', function () {
    if (window.scrollY > 160) {
        scrollBtn.style.display = 'block';
    } else {
        scrollBtn.style.display = 'none';
    }
});
scrollBtn.addEventListener('click', function () {
    window.scrollTo({ top: 0, behavior: 'smooth' });
});

// ======= 3. HIỆU ỨNG FOCUS THANH TÌM KIẾM =========
const searchInput = document.querySelector('.header__search input');
const searchBox = document.querySelector('.header__search');
if (searchInput && searchBox) {
    searchInput.addEventListener('focus', () => {
        searchBox.classList.add('searching');
        // Nếu mobile, tự scroll search box
        if (window.innerWidth < 760) searchBox.scrollIntoView({ behavior: 'smooth', block: 'center' });
    });
    searchInput.addEventListener('blur', () => {
        searchBox.classList.remove('searching');
    });
}

// ======= 4. EFFECT VỚI PRODUCT CARD =========
const cards = document.querySelectorAll('.product-card');
cards.forEach(card => {
    // Hiệu ứng nhấn vào
    card.addEventListener('mousedown', () => {
        card.classList.add('active');
    });
    card.addEventListener('mouseup', () => {
        card.classList.remove('active');
    });
    card.addEventListener('mouseleave', () => {
        card.classList.remove('active');
    });
    // Tooltip "Xem chi tiết"
    card.addEventListener('mouseenter', function (e) {
        let tt = document.createElement('div');
        tt.className = 'product-tooltip';
        tt.innerText = 'Xem chi tiết';
        card.appendChild(tt);
        setTimeout(() => tt.classList.add('show'), 10);
    });
    card.addEventListener('mouseleave', function (e) {
        const tool = card.querySelector('.product-tooltip');
        if (tool) card.removeChild(tool);
    });
});

// ===== CSS động thêm: =====
const style = document.createElement('style');
style.innerHTML = `
/* Nút cuộn lên đầu */
.scroll-top-btn {
  position: fixed; bottom: 30px; right: 21px; z-index: 999;
  background: #1657a2; color: #fff; border: none;
  width: 46px; height: 46px;
  border-radius: 50%; font-size: 2rem; box-shadow: 0 2px 8px #0002;
  cursor: pointer; transition: background 0.19s, box-shadow 0.19s;
}
.scroll-top-btn:hover { background: #6ec01a; color: #fff; box-shadow: 0 6px 24px #6ec01a40; }
/* Hiệu ứng focus search */
.header__search.searching { box-shadow: 0 0 0 3px #6ec01a48; border: 1.5px solid #6ec01a; }
/* Menu show ở mobile */
@media (max-width: 850px) {
  .header__menu-links {
    position: absolute; left: 5px; right: 5px;
    background: #155cb3; top: 100%; z-index: 20;
    border-radius: 7px; box-shadow: 0 6px 18px #0001;
    display: none; flex-direction: column; gap: 0 !important;
  }
  .header__menu-links.menu-active {
    display: flex;
  }
  .header__menu-btn.menu-opened {
    background: #ebf6e8;
    color: #3c7a33;
  }
}
/* Hiệu ứng product-card */
.product-card.active {
  box-shadow: 0 6px 36px 0 #2975c160;
  transform: scale(0.98);
  transition: all 0.15s;
}
.product-tooltip {
  position: absolute;
  left: 50%; bottom: 17px;
  transform: translateX(-50%) scale(0.8);
  padding: 4.5px 16px;
  font-size: 0.97em;
  background: #1657a2; border-radius: 7px;
  color: #fff;
  pointer-events: none;
  opacity: 0;
  transition: all 0.2s;
  z-index: 25;
  white-space: nowrap;
}
.product-tooltip.show { opacity: 1; transform: translateX(-50%) scale(1); }
.product-card { position: relative; }
`;
document.head.appendChild(style);