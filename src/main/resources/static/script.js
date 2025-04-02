document.addEventListener('DOMContentLoaded', function() {
    // Theme toggle functionality
    const themeToggle = document.getElementById('theme-toggle');
    const prefersDarkScheme = window.matchMedia('(prefers-color-scheme: dark)');

    // Check for saved theme preference or use the system preference
    const currentTheme = localStorage.getItem('theme') ||
        (prefersDarkScheme.matches ? 'dark' : 'light');

    // Apply the current theme
    document.body.classList.toggle('dark', currentTheme === 'dark');

    // Toggle theme when button is clicked
    themeToggle.addEventListener('click', function() {
        const isDark = document.body.classList.toggle('dark');
        localStorage.setItem('theme', isDark ? 'dark' : 'light');
    });

    // Smooth scrolling for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();

            const targetId = this.getAttribute('href');
            if (targetId === '#') return;

            const targetElement = document.querySelector(targetId);
            if (targetElement) {
                window.scrollTo({
                    top: targetElement.offsetTop - 80, // Offset for header
                    behavior: 'smooth'
                });

                // Update active nav link
                document.querySelectorAll('.nav-links a').forEach(link => {
                    link.classList.remove('active');
                });
                this.classList.add('active');
            }
        });
    });

    // Set active nav link based on scroll position
    window.addEventListener('scroll', function() {
        const sections = document.querySelectorAll('section[id]');
        const scrollPosition = window.scrollY + 100; // Offset for header

        sections.forEach(section => {
            const sectionTop = section.offsetTop;
            const sectionHeight = section.offsetHeight;
            const sectionId = section.getAttribute('id');

            if (scrollPosition >= sectionTop &&
                scrollPosition < sectionTop + sectionHeight) {
                document.querySelector(`.nav-links a[href="#${sectionId}"]`)?.classList.add('active');
            } else {
                document.querySelector(`.nav-links a[href="#${sectionId}"]`)?.classList.remove('active');
            }
        });

        // Handle a home link
        if (scrollPosition < document.querySelector('section[id]').offsetTop) {
            document.querySelector('.nav-links a[href="#"]')?.classList.add('active');
        } else {
            document.querySelector('.nav-links a[href="#"]')?.classList.remove('active');
        }
    });

    // User Profile Page Functionality
    // Changes Password Toggle
    const changePasswordBtn = document.getElementById('change-password-btn');
    const passwordFormContainer = document.getElementById('password-form-container');
    const cancelPasswordBtn = document.getElementById('cancel-password');
    const passwordForm = document.getElementById('password-form');

    if (changePasswordBtn && passwordFormContainer) {
        changePasswordBtn.addEventListener('click', function() {
            passwordFormContainer.classList.remove('hidden');
            changePasswordBtn.classList.add('hidden');
        });
    }

    if (cancelPasswordBtn && passwordFormContainer && changePasswordBtn) {
        cancelPasswordBtn.addEventListener('click', function() {
            passwordFormContainer.classList.add('hidden');
            changePasswordBtn.classList.remove('hidden');
            passwordForm.reset();
        });
    }

    if (passwordForm) {
        passwordForm.addEventListener('submit', function(e) {

            const currentPassword = document.getElementById('currentPassword').value;
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;

            // Validate passwords
            if (!currentPassword || !newPassword || !confirmPassword) {
                alert('Please fill in all password fields');
                e.preventDefault()
                return;
            }

            if (newPassword !== confirmPassword) {
                alert('New password and confirmation do not match');
                e.preventDefault()
            }

            // Not Needed because of redirection to the same page
            // passwordFormContainer.classList.add('hidden');
            // changePasswordBtn.classList.remove('hidden');
            // passwordForm.reset();
        });
    }

    // Delete Account Confirmation
    const deleteAccountBtn = document.getElementById('delete-account-btn');
    const deleteConfirmation = document.getElementById('delete-confirmation');
    const cancelDeleteBtn = document.getElementById('cancel-delete');

    if (deleteAccountBtn && deleteConfirmation) {
        deleteAccountBtn.addEventListener('click', function() {
            deleteConfirmation.classList.remove('hidden');
            deleteAccountBtn.classList.add('hidden');
        });
    }

    if (cancelDeleteBtn && deleteConfirmation && deleteAccountBtn) {
        cancelDeleteBtn.addEventListener('click', function() {
            deleteConfirmation.classList.add('hidden');
            deleteAccountBtn.classList.remove('hidden');
        });
    }

    // Not Needed -> Implemented in Spring Boot (Server Side)
    // // JWT Cookie Handling
    // function getJwtCookie() {
    //     const cookies = document.cookie.split(';');
    //     for (let i = 0; i < cookies.length; i++) {
    //         const cookie = cookies[i].trim();
    //         if (cookie.startsWith('jwt=')) {
    //             return cookie.substring(4);
    //         }
    //     }
    //     return null;
    // }

    // // Check if user is logged in
    // function checkUserAuthentication() {
    //     const jwt = getJwtCookie();
    //     if (!jwt && window.location.pathname.includes('/user')) {
    //         // Redirect to log in if on user page without JWT
    //         window.location.href = '/api/v1/auth/login';
    //     }
    // }

    // Call authentication check
    // checkUserAuthentication();

    // Sample data for demonstration (would be replaced by actual data from Spring Boot)
    // This is just for preview purposes
    // const sampleData = {
    //     displaySampleData: function() {
    //         console.log("Banking application initialized");
    //         // In a real application, this would be handled by Thymeleaf and Spring Boot
    //     }
    // };
    //
    // sampleData.displaySampleData();

    // Contact Form Validation
    const contactForm = document.querySelector('.contact-form');
    if (contactForm) {
        contactForm.addEventListener('submit', function(e) {
            e.preventDefault();

            // Simple validation
            const name = document.getElementById('name').value;
            const email = document.getElementById('email').value;
            const subject = document.getElementById('subject').value;
            const message = document.getElementById('message').value;

            if (!name || !email || !subject || !message) {
                alert('Please fill in all fields');
                return;
            }

            // In a real application, you would send this to the server
            // For now, we'll just show a success message
            alert('Message sent successfully! We will get back to you soon.');
            contactForm.reset();
        });
    }


    // Transfer Form Validation
    const transferForm = document.querySelector('.transfer-form');
    if (transferForm) {
        transferForm.addEventListener('submit', function(e) {
            e.preventDefault();

            // Simple validation
            const fromAccount = document.getElementById('fromAccount').value;
            const toAccount = document.getElementById('toAccount').value;
            const amount = document.getElementById('amount').value;
            const transferDate = document.getElementById('transferDate').value;

            if (!fromAccount || !toAccount || !amount || !transferDate) {
                alert('Please fill in all required fields');
                return;
            }

            if (fromAccount === toAccount) {
                alert('From and To accounts cannot be the same');
                return;
            }

            if (parseFloat(amount) <= 0) {
                alert('Amount must be greater than zero');
                return;
            }

            // In a real application, you would send this to the server
            // For now, we'll just show a success message
            alert('Transfer initiated successfully!');
            transferForm.reset();
        });
    }
});