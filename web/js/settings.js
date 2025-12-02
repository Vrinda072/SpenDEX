document.addEventListener('DOMContentLoaded', function() {
    // DOM Elements
    const settingsMenuItems = document.querySelectorAll('.settings-menu li');
    const settingsTabs = document.querySelectorAll('.settings-tab');
    const addCategoryBtn = document.getElementById('addCategoryBtn');
    const categoryModal = document.getElementById('addCategoryModal');
    const closeModalBtns = document.querySelectorAll('.close-modal');
    const cancelCategoryBtn = document.getElementById('cancelCategory');
    const categoryForm = document.getElementById('categoryForm');
    const categoryTabs = document.querySelectorAll('.category-tab');
    const expenseCategories = document.getElementById('expenseCategories');
    const incomeCategories = document.getElementById('incomeCategories');
    const themeToggle = document.getElementById('themePreference');
    const markAsReadBtn = document.querySelector('.settings-actions .btn-secondary');
    const savePrefsBtn = document.querySelector('.settings-actions .btn-primary');
    const deleteAccountBtn = document.getElementById('deleteAccountBtn');
    const deactivateAccountBtn = document.getElementById('deactivateAccountBtn');
    const twoFactorToggle = document.getElementById('twoFactorToggle');
    const cloudBackupToggle = document.getElementById('cloudBackupToggle');
    const backupNowBtn = document.querySelector('.backup-option .btn-secondary');
    const changePasswordBtn = document.getElementById('changePasswordBtn');

    // Initialize settings page
    initSettings();

    // Event Listeners
    settingsMenuItems.forEach(item => {
        item.addEventListener('click', () => switchTab(item.dataset.tab));
    });

    categoryTabs.forEach(tab => {
        tab.addEventListener('click', () => switchCategoryTab(tab.dataset.type));
    });

    addCategoryBtn?.addEventListener('click', () => {
        categoryModal.style.display = 'block';
    });

    closeModalBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            categoryModal.style.display = 'none';
        });
    });

    cancelCategoryBtn?.addEventListener('click', () => {
        categoryModal.style.display = 'none';
    });

    categoryForm?.addEventListener('submit', (e) => {
        e.preventDefault();
        saveCategory();
    });

    // Close modal when clicking outside
    window.addEventListener('click', (e) => {
        if (e.target === categoryModal) {
            categoryModal.style.display = 'none';
        }
    });

    // Theme toggle
    themeToggle?.addEventListener('change', (e) => {
        document.documentElement.setAttribute('data-theme', e.target.value);
        // In a real app, save to user preferences
        localStorage.setItem('theme', e.target.value);
    });

    // Mark all as read
    markAsReadBtn?.addEventListener('click', () => {
        // In a real app, mark notifications as read
        alert('All notifications marked as read');
    });

    // Save preferences
    savePrefsBtn?.addEventListener('click', () => {
        // In a real app, save all preferences
        alert('Preferences saved successfully');
    });

    // Delete account
    deleteAccountBtn?.addEventListener('click', () => {
        if (confirm('Are you sure you want to delete your account? This action cannot be undone.')) {
            // In a real app, handle account deletion
            alert('Account deletion requested. You will receive a confirmation email.');
        }
    });

    // Deactivate account
    deactivateAccountBtn?.addEventListener('click', () => {
        if (confirm('Are you sure you want to deactivate your account? You can reactivate it by logging in again.')) {
            // In a real app, handle account deactivation
            alert('Account deactivated successfully');
        }
    });

    // Two-factor authentication toggle
    twoFactorToggle?.addEventListener('change', (e) => {
        if (e.target.checked) {
            // In a real app, set up 2FA
            alert('Two-factor authentication is being set up...');
        } else {
            // In a real app, disable 2FA
            if (confirm('Are you sure you want to disable two-factor authentication? This will make your account less secure.')) {
                alert('Two-factor authentication has been disabled.');
            } else {
                e.target.checked = true;
            }
        }
    });

    // Cloud backup toggle
    cloudBackupToggle?.addEventListener('change', (e) => {
        if (e.target.checked) {
            // In a real app, enable cloud backup
            alert('Cloud backup has been enabled.');
        } else {
            // In a real app, disable cloud backup
            if (confirm('Are you sure you want to disable cloud backup? Your data will no longer be backed up automatically.')) {
                alert('Cloud backup has been disabled.');
            } else {
                e.target.checked = true;
            }
        }
    });

    // Backup now
    backupNowBtn?.addEventListener('click', () => {
        // In a real app, trigger a backup
        alert('Backup started. You will be notified when it is complete.');
    });

    // Change password
    changePasswordBtn?.addEventListener('click', () => {
        // In a real app, show change password form
        const newPassword = prompt('Enter your new password:');
        if (newPassword) {
            alert('Your password has been updated successfully.');
        }
    });

    // Functions
    function initSettings() {
        // Load saved theme preference
        const savedTheme = localStorage.getItem('theme') || 'system';
        if (themeToggle) {
            themeToggle.value = savedTheme;
            document.documentElement.setAttribute('data-theme', savedTheme);
        }

        // Activate first tab by default
        if (settingsMenuItems.length > 0) {
            const defaultTab = 'profile'; // Or get from URL hash
            switchTab(defaultTab);
        }

        // Initialize category tabs
        if (categoryTabs.length > 0) {
            switchCategoryTab('expense');
        }
    }

    function switchTab(tabId) {
        // Update active menu item
        settingsMenuItems.forEach(item => {
            if (item.dataset.tab === tabId) {
                item.classList.add('active');
            } else {
                item.classList.remove('active');
            }
        });

        // Show selected tab content
        settingsTabs.forEach(tab => {
            if (tab.id === `${tabId}-tab`) {
                tab.classList.add('active');
            } else {
                tab.classList.remove('active');
            }
        });

        // Update URL hash
        window.location.hash = tabId;

        // In a real app, you might load tab content dynamically here
    }

    function switchCategoryTab(type) {
        categoryTabs.forEach(tab => {
            if (tab.dataset.type === type) {
                tab.classList.add('active');
            } else {
                tab.classList.remove('active');
            }
        });

        if (type === 'expense') {
            expenseCategories?.classList.remove('hidden');
            incomeCategories?.classList.add('hidden');
        } else {
            expenseCategories?.classList.add('hidden');
            incomeCategories?.classList.remove('hidden');
        }
    }

    function saveCategory() {
        const name = document.getElementById('categoryName').value.trim();
        const type = document.getElementById('categoryType').value;
        const color = document.getElementById('categoryColor').value;
        
        if (!name) {
            alert('Please enter a category name');
            return;
        }

        // In a real app, save to your backend
        console.log('Saving category:', { name, type, color });
        
        // Create and append the new category element
        const categoryItem = document.createElement('div');
        categoryItem.className = 'category-item';
        categoryItem.innerHTML = `
            <div class="category-color" style="background-color: ${color};"></div>
            <span class="category-name">${name}</span>
            <div class="category-actions">
                <button class="btn-icon"><i class="fas fa-edit"></i></button>
                <button class="btn-icon"><i class="fas fa-trash"></i></button>
            </div>
        `;

        // Add to the appropriate list
        const targetList = type === 'expense' ? expenseCategories : incomeCategories;
        targetList?.appendChild(categoryItem);

        // Close the modal and reset the form
        categoryModal.style.display = 'none';
        categoryForm.reset();
    }

    // Handle tab switching from URL hash
    window.addEventListener('load', () => {
        const hash = window.location.hash.substring(1);
        if (hash) {
            switchTab(hash);
        }
    });
});
