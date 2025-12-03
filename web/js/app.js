// SpenDEX - Main Application JavaScript
// Handles all frontend interactions with the backend API

// API Configuration
const API_BASE = '/api';
const API_TRANSACTIONS = `${API_BASE}/transactions`;
const API_BUDGET = `${API_BASE}/budget`;
const API_EVENTS = `${API_BASE}/events`;

// Global state
let transactions = [];
let budgetInfo = {};
let currentPage = 'dashboard';

// Initialize app on page load
document.addEventListener('DOMContentLoaded', () => {
    console.log('SpenDEX app initialized');
    initializeApp();
});

// Initialize application
async function initializeApp() {
    // Load initial data
    await loadTransactions();
    await loadBudgetInfo();
    
    // Setup event listeners
    setupEventListeners();
    
    // Update UI
    updateDashboard();
}

// Setup event listeners
function setupEventListeners() {
    // Add transaction button
    const addBtn = document.getElementById('addTransactionBtn');
    if (addBtn) {
        addBtn.addEventListener('click', showAddTransactionModal);
    }
    
    // Form submission
    const form = document.getElementById('transactionForm');
    if (form) {
        form.addEventListener('submit', handleAddTransaction);
    }
}

// Load transactions from API
async function loadTransactions() {
    try {
        const response = await fetch(API_TRANSACTIONS);
        if (!response.ok) throw new Error('Failed to load transactions');
        transactions = await response.json();
        console.log('Transactions loaded:', transactions);
    } catch (error) {
        console.error('Error loading transactions:', error);
        transactions = [];
    }
}

// Load budget information from API
async function loadBudgetInfo() {
    try {
        const response = await fetch(API_BUDGET);
        if (!response.ok) throw new Error('Failed to load budget');
        budgetInfo = await response.json();
        console.log('Budget info loaded:', budgetInfo);
    } catch (error) {
        console.error('Error loading budget:', error);
        budgetInfo = {};
    }
}

// Show add transaction modal
function showAddTransactionModal() {
    // Create modal if it doesn't exist
    let modal = document.getElementById('transactionModal');
    if (!modal) {
        modal = createModal();
        document.body.appendChild(modal);
    }
    modal.style.display = 'block';
}

// Create transaction modal HTML
function createModal() {
    const modal = document.createElement('div');
    modal.id = 'transactionModal';
    modal.className = 'modal';
    modal.innerHTML = `
        <div class="modal-content">
            <div class="modal-header">
                <h2>Add Transaction</h2>
                <button class="close-btn">&times;</button>
            </div>
            <form id="transactionForm">
                <div class="form-group">
                    <label for="txType">Type:</label>
                    <select id="txType" name="type" required>
                        <option value="expense">Expense</option>
                        <option value="income">Income</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="txAmount">Amount:</label>
                    <input type="number" id="txAmount" name="amount" step="0.01" required placeholder="0.00">
                </div>
                
                <div class="form-group">
                    <label for="txCategory">Category:</label>
                    <select id="txCategory" name="category" required>
                        <option value="FOOD">Food</option>
                        <option value="TRAVEL">Travel</option>
                        <option value="BILLS">Bills</option>
                        <option value="ENTERTAINMENT">Entertainment</option>
                        <option value="OTHER">Other</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="txDescription">Description:</label>
                    <input type="text" id="txDescription" name="description" required placeholder="Enter description">
                </div>
                
                <div class="form-group">
                    <label for="txDate">Date:</label>
                    <input type="date" id="txDate" name="date" required>
                </div>
                
                <div class="modal-buttons">
                    <button type="submit" class="btn btn-primary">Add Transaction</button>
                    <button type="button" class="btn btn-secondary" id="cancelBtn">Cancel</button>
                </div>
            </form>
        </div>
    `;
    
    // Set today's date as default
    const today = new Date().toISOString().split('T')[0];
    setTimeout(() => {
        const dateInput = document.getElementById('txDate');
        if (dateInput) dateInput.value = today;
    }, 0);
    
    // Close modal on close button
    const closeBtn = modal.querySelector('.close-btn');
    if (closeBtn) {
        closeBtn.addEventListener('click', () => {
            modal.style.display = 'none';
        });
    }
    
    // Close modal on cancel button
    const cancelBtn = modal.querySelector('#cancelBtn');
    if (cancelBtn) {
        cancelBtn.addEventListener('click', () => {
            modal.style.display = 'none';
        });
    }
    
    // Close modal on outside click
    window.addEventListener('click', (event) => {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
    
    return modal;
}

// Handle add transaction form submission
async function handleAddTransaction(event) {
    event.preventDefault();
    
    const form = event.target;
    const formData = new FormData(form);
    
    // Convert FormData to URL-encoded format for form submission
    const params = new URLSearchParams();
    params.append('type', formData.get('type'));
    params.append('amount', formData.get('amount'));
    params.append('category', formData.get('category'));
    params.append('description', formData.get('description'));
    params.append('date', formData.get('date'));
    
    try {
        const response = await fetch(API_TRANSACTIONS, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: params.toString()
        });
        
        if (!response.ok) {
            throw new Error(`Failed to add transaction: ${response.statusText}`);
        }
        
        const result = await response.json();
        console.log('Transaction added:', result);
        
        // Close modal
        const modal = document.getElementById('transactionModal');
        if (modal) modal.style.display = 'none';
        
        // Reset form
        form.reset();
        
        // Reload data and update UI
        await loadTransactions();
        await loadBudgetInfo();
        updateDashboard();
        
        // Show success message
        showNotification('Transaction added successfully!', 'success');
        
    } catch (error) {
        console.error('Error adding transaction:', error);
        showNotification(`Error: ${error.message}`, 'error');
    }
}

// Update dashboard with current data
function updateDashboard() {
    updateSummaryCards();
    updateTransactionsList();
    updateCharts();
}

// Update summary cards
function updateSummaryCards() {
    const spent = budgetInfo.totalSpent || 0;
    const remaining = budgetInfo.remaining || 0;
    const budget = budgetInfo.monthlyBudget || 0;
    const daily = budgetInfo.dailyLimit || 0;
    
    // Update spent card
    const spentCard = document.getElementById('spentAmount');
    if (spentCard) spentCard.textContent = `AED ${spent.toFixed(2)}`;
    
    // Update remaining card
    const remainingCard = document.getElementById('remainingAmount');
    if (remainingCard) remainingCard.textContent = `AED ${remaining.toFixed(2)}`;
    
    // Update budget card
    const budgetCard = document.getElementById('budgetAmount');
    if (budgetCard) budgetCard.textContent = `AED ${budget.toFixed(2)}`;
    
    // Update daily limit card
    const dailyCard = document.getElementById('dailyLimit');
    if (dailyCard) dailyCard.textContent = `AED ${daily.toFixed(2)}`;
}

// Update transactions list on dashboard
function updateTransactionsList() {
    const listContainer = document.getElementById('transactionsList');
    if (!listContainer) return;
    
    if (transactions.length === 0) {
        listContainer.innerHTML = '<p class="empty-state">No transactions yet. Add one to get started!</p>';
        return;
    }
    
    // Sort transactions by date (newest first)
    const sorted = [...transactions].sort((a, b) => new Date(b.date) - new Date(a.date));
    
    // Show last 5 transactions
    const recent = sorted.slice(0, 5);
    
    listContainer.innerHTML = recent.map(tx => `
        <div class="transaction-item ${tx.type.toLowerCase()}">
            <div class="transaction-info">
                <span class="transaction-description">${tx.title}</span>
                <span class="transaction-date">${formatDate(tx.date)}</span>
            </div>
            <span class="transaction-amount ${tx.type.toLowerCase()}">
                ${tx.type === 'INCOME' ? '+' : '-'} AED ${Math.abs(tx.amount).toFixed(2)}
            </span>
        </div>
    `).join('');
}

// Update charts
function updateCharts() {
    // This would typically use Chart.js library
    // Placeholder for chart updates
    console.log('Charts updated');
}

// Format date
function formatDate(dateString) {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-US', options);
}

// Show notification
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        background: ${type === 'success' ? '#4caf50' : '#f44336'};
        color: white;
        border-radius: 4px;
        z-index: 9999;
        animation: slideIn 0.3s ease-in-out;
    `;
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.remove();
    }, 3000);
}

// Delete transaction
async function deleteTransaction(id) {
    if (!confirm('Are you sure you want to delete this transaction?')) return;
    
    try {
        const response = await fetch(`${API_TRANSACTIONS}/${id}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) throw new Error('Failed to delete transaction');
        
        // Reload data and update UI
        await loadTransactions();
        await loadBudgetInfo();
        updateDashboard();
        
        showNotification('Transaction deleted successfully!', 'success');
        
    } catch (error) {
        console.error('Error deleting transaction:', error);
        showNotification(`Error: ${error.message}`, 'error');
    }
}

// Export functions for use in HTML
window.deleteTransaction = deleteTransaction;
window.showAddTransactionModal = showAddTransactionModal;
