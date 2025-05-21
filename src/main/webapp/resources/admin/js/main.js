
const avatarIcon = document.getElementById('admin-avatar-icon');
const dropdown = document.getElementById('user-dropdown');

avatarIcon.addEventListener('click', function (e) {
    e.stopPropagation();
    // Toggle dropdown
    dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
});

document.addEventListener('click', function (e) {
    // Ẩn dropdown nếu click ra ngoài
    if (!dropdown.contains(e.target) && e.target !== avatarIcon) {
        dropdown.style.display = 'none';
    }
});

