// Analytics Page JavaScript
// Fetch and display analytics data with charts

const API_BASE = '/api';
const API_TRANSACTIONS = `${API_BASE}/transactions`;
const API_BUDGET = `${API_BASE}/budget`;

// Global state
let allTransactions = [];
let budgetInfo = {};
let charts = {};

// Initialize analytics on page load
document.addEventListener('DOMContentLoaded', () => {
    console.log('Analytics page loaded');
    initializeAnalytics();
});

// Initialize analytics
async function initializeAnalytics() {
    // Load data from API
    await loadTransactions();
    await loadBudgetInfo();
    
    // Setup tab switching
    setupTabSwitching();
    
    // Initialize charts
    initializeCharts();
    
    // Populate dynamic content
    updateAnalyticsSummary();
    populateCategoryCards();
}

// Load transactions from API
async function loadTransactions() {
    try {
        const response = await fetch(API_TRANSACTIONS);
        if (!response.ok) throw new Error('Failed to load transactions');
        allTransactions = await response.json();
        console.log('Transactions loaded for analytics:', allTransactions);
    } catch (error) {
        console.error('Error loading transactions:', error);
        allTransactions = [];
    }
}

// Load budget information
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

// Setup tab switching
function setupTabSwitching() {
    const tabButtons = document.querySelectorAll('.btn-tab');
    tabButtons.forEach(button => {
        button.addEventListener('click', () => {
            // Remove active class from all buttons and tabs
            document.querySelectorAll('.btn-tab').forEach(b => b.classList.remove('active'));
            document.querySelectorAll('.tab-content').forEach(t => t.classList.remove('active'));
            
            // Add active class to clicked button and corresponding tab
            button.classList.add('active');
            const tabName = button.getAttribute('data-tab');
            const tabElement = document.getElementById(tabName + 'Tab');
            if (tabElement) {
                tabElement.classList.add('active');
            }
        });
    });
}

// Update analytics summary cards
function updateAnalyticsSummary() {
    // Calculate totals
    let totalIncome = 0;
    let totalExpenses = 0;
    
    allTransactions.forEach(tx => {
        if (tx.type === 'INCOME') {
            totalIncome += tx.amount;
        } else if (tx.type === 'EXPENSE') {
            totalExpenses += tx.amount;
        }
    });
    
    const netSavings = totalIncome - totalExpenses;
    
    // Update summary cards
    const incomeCard = document.getElementById('analyticsIncome');
    if (incomeCard) incomeCard.textContent = `AED ${totalIncome.toFixed(2)}`;
    
    const expensesCard = document.getElementById('analyticsExpenses');
    if (expensesCard) expensesCard.textContent = `AED ${totalExpenses.toFixed(2)}`;
    
    const savingsCard = document.getElementById('analyticsNetSavings');
    if (savingsCard) savingsCard.textContent = `AED ${netSavings.toFixed(2)}`;
}

// Populate category cards
function populateCategoryCards() {
    const categories = ['FOOD', 'TRAVEL', 'BILLS', 'ENTERTAINMENT', 'OTHER'];
    const categoryColors = {
        'FOOD': { bg: '#FEE2E2', text: '#DC2626', icon: 'utensils' },
        'TRAVEL': { bg: '#FEF3C7', text: '#D97706', icon: 'car' },
        'BILLS': { bg: '#DBEAFE', text: '#2563EB', icon: 'file-invoice' },
        'ENTERTAINMENT': { bg: '#F3E8FF', text: '#7C3AED', icon: 'theater-masks' },
        'OTHER': { bg: '#E5E7EB', text: '#6B7280', icon: 'box' }
    };
    
    const container = document.getElementById('categoryCardsContainer');
    if (!container) return;
    
    let html = '';
    
    categories.forEach(category => {
        const categoryTransactions = allTransactions.filter(
            tx => tx.category === category && tx.type === 'EXPENSE'
        );
        const amount = categoryTransactions.reduce((sum, tx) => sum + tx.amount, 0);
        const count = categoryTransactions.length;
        
        const colors = categoryColors[category];
        
        html += `
            <div class="category-card">
                <div class="category-header" style="background-color: ${colors.bg};">
                    <i class="fas fa-${colors.icon}" style="color: ${colors.text};"></i>
                    <h4>${category.charAt(0) + category.slice(1).toLowerCase()}</h4>
                </div>
                <div class="category-details">
                    <div class="category-amount">AED ${amount.toFixed(2)}</div>
                    <div class="category-stats">
                        <div class="stat">
                            <span class="stat-label">Transactions</span>
                            <span class="stat-value">${count}</span>
                        </div>
                    </div>
                </div>
            </div>
        `;
    });
    
    container.innerHTML = html;
}

// Initialize charts
function initializeCharts() {
    createIncomeExpenseChart();
    createSpendingByCategoryChart();
    createIncomeSourcesChart();
    createSpendingTrendsChart();
}

// Create Income vs Expenses Chart
function createIncomeExpenseChart() {
    const ctx = document.getElementById('incomeExpenseChart');
    if (!ctx) return;
    
    // Group by month
    const months = {};
    const currentDate = new Date();
    
    // Initialize last 6 months
    for (let i = 5; i >= 0; i--) {
        const d = new Date(currentDate);
        d.setMonth(d.getMonth() - i);
        const monthKey = d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0');
        months[monthKey] = { income: 0, expenses: 0 };
    }
    
    // Aggregate data
    allTransactions.forEach(tx => {
        const txDate = new Date(tx.date);
        const monthKey = txDate.getFullYear() + '-' + String(txDate.getMonth() + 1).padStart(2, '0');
        if (months[monthKey]) {
            if (tx.type === 'INCOME') {
                months[monthKey].income += tx.amount;
            } else {
                months[monthKey].expenses += tx.amount;
            }
        }
    });
    
    const labels = Object.keys(months);
    const incomeData = labels.map(m => months[m].income);
    const expensesData = labels.map(m => months[m].expenses);
    
    if (charts.incomeExpense) charts.incomeExpense.destroy();
    
    charts.incomeExpense = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels.map(m => {
                const [year, month] = m.split('-');
                return new Date(year, month - 1).toLocaleDateString('en-US', { month: 'short', year: '2-digit' });
            }),
            datasets: [
                {
                    label: 'Income',
                    data: incomeData,
                    backgroundColor: '#10B981',
                    borderColor: '#10B981',
                    borderWidth: 1
                },
                {
                    label: 'Expenses',
                    data: expensesData,
                    backgroundColor: '#EF4444',
                    borderColor: '#EF4444',
                    borderWidth: 1
                }
            ]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top'
                }
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

// Create Spending by Category Chart
function createSpendingByCategoryChart() {
    const ctx = document.getElementById('spendingByCategoryChart');
    if (!ctx) return;
    
    const categories = ['FOOD', 'TRAVEL', 'BILLS', 'ENTERTAINMENT', 'OTHER'];
    const colors = ['#EF4444', '#F59E0B', '#3B82F6', '#8B5CF6', '#6B7280'];
    
    const categoryData = categories.map(cat => {
        const expenses = allTransactions.filter(
            tx => tx.category === cat && tx.type === 'EXPENSE'
        ).reduce((sum, tx) => sum + tx.amount, 0);
        return expenses;
    });
    
    if (charts.spendingByCategory) charts.spendingByCategory.destroy();
    
    charts.spendingByCategory = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: categories.map(c => c.charAt(0) + c.slice(1).toLowerCase()),
            datasets: [{
                data: categoryData,
                backgroundColor: colors,
                borderColor: '#fff',
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

// Create Income Sources Chart
function createIncomeSourcesChart() {
    const ctx = document.getElementById('incomeSourcesChart');
    if (!ctx) return;
    
    // Group income by category
    const incomeByCategory = {};
    allTransactions.forEach(tx => {
        if (tx.type === 'INCOME') {
            if (!incomeByCategory[tx.category]) {
                incomeByCategory[tx.category] = 0;
            }
            incomeByCategory[tx.category] += tx.amount;
        }
    });
    
    const labels = Object.keys(incomeByCategory);
    const data = Object.values(incomeByCategory);
    
    if (charts.incomeSources) charts.incomeSources.destroy();
    
    charts.incomeSources = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: labels.length > 0 ? labels : ['No Income Data'],
            datasets: [{
                data: data.length > 0 ? data : [1],
                backgroundColor: ['#10B981', '#3B82F6', '#8B5CF6', '#F59E0B', '#EF4444'],
                borderColor: '#fff',
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

// Create Spending Trends Chart
function createSpendingTrendsChart() {
    const ctx = document.getElementById('spendingTrendsChart');
    if (!ctx) return;
    
    // Group by month
    const months = {};
    const currentDate = new Date();
    
    // Initialize last 12 months
    for (let i = 11; i >= 0; i--) {
        const d = new Date(currentDate);
        d.setMonth(d.getMonth() - i);
        const monthKey = d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0');
        months[monthKey] = 0;
    }
    
    // Aggregate expenses
    allTransactions.forEach(tx => {
        if (tx.type === 'EXPENSE') {
            const txDate = new Date(tx.date);
            const monthKey = txDate.getFullYear() + '-' + String(txDate.getMonth() + 1).padStart(2, '0');
            if (months[monthKey] !== undefined) {
                months[monthKey] += tx.amount;
            }
        }
    });
    
    const labels = Object.keys(months);
    const data = Object.values(months);
    
    if (charts.spendingTrends) charts.spendingTrends.destroy();
    
    charts.spendingTrends = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels.map(m => {
                const [year, month] = m.split('-');
                return new Date(year, month - 1).toLocaleDateString('en-US', { month: 'short', year: '2-digit' });
            }),
            datasets: [{
                label: 'Monthly Expenses',
                data: data,
                borderColor: '#EF4444',
                backgroundColor: 'rgba(239, 68, 68, 0.1)',
                borderWidth: 2,
                fill: true,
                tension: 0.4,
                pointBackgroundColor: '#EF4444',
                pointBorderColor: '#fff',
                pointBorderWidth: 2
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top'
                }
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}
