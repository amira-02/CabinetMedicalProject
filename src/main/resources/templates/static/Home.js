// Smooth scrolling for anchor links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        document.querySelector(this.getAttribute('href')).scrollIntoView({
            behavior: 'smooth'
        });
    });
});

// Marquee effect
var elements = $('ul.marquee-item-list li').length;
for (var i = 0; i < elements; i++) {
    $(".marquee-item-list").clone().prependTo(".marquee-block");
}
var liEle = [];
var liEle = $(".marquee-item-list li");

// Navbar scroll effect
const navbar = document.querySelector('.navbar');
window.addEventListener('scroll', () => {
    if (window.scrollY > 50) {
        navbar.classList.add('scrolled');
    } else {
        navbar.classList.remove('scrolled');
    }
});

// Custom cursor effect
const cursor = document.getElementById("TryCircle");
let mouseX = 0;
let mouseY = 0;
let targetX = 0;
let targetY = 0;
const delay = 100;

window.addEventListener("mousemove", (e) => {
    mouseX = e.clientX;
    mouseY = e.clientY;
    cursor.classList.add("active");
});

function moveCursor() {
    targetX += (mouseX - targetX) * 0.1;
    targetY += (mouseY - targetY) * 0.1;
    cursor.style.left = `${targetX - 15}px`;
    cursor.style.top = `${targetY - 15}px`;
    requestAnimationFrame(moveCursor);
}

moveCursor();

const targetSection = document.getElementById("targetSection");
targetSection.addEventListener("mouseenter", () => {
    cursor.classList.add("active");
    cursor.classList.remove("shrink");
});
targetSection.addEventListener("mouseleave", () => {
    cursor.classList.add("shrink");
    cursor.classList.remove("active");
});

document.querySelectorAll("p, a, img").forEach((element) => {
    element.addEventListener("mouseenter", () => {
        cursor.classList.add("shrink");
        cursor.classList.remove("active");
    });

    element.addEventListener("mouseleave", () => {
        cursor.classList.remove("shrink");
        cursor.classList.add("active");
    });
});

window.addEventListener("mouseleave", () => {
    cursor.classList.add("shrink");
    cursor.classList.remove("active");
});

window.addEventListener("mouseenter", () => {
    cursor.classList.remove("shrink");
});