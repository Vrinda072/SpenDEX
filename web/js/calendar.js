document.addEventListener('DOMContentLoaded', function() {
    // Initialize FullCalendar
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        headerToolbar: false, // We're using our own custom header
        height: 'auto',
        firstDay: 1, // Monday as first day of week
        dayMaxEvents: 3, // Show a "more" link when too many events
        events: [
            // Sample events - in a real app, these would come from your backend
            {
                title: 'Rent Payment',
                start: '2025-01-01',
                end: '2025-01-01',
                color: '#EF4444',
                extendedProps: {
                    type: 'bill',
                    amount: 1200.00,
                    description: 'Monthly rent payment',
                    paid: false
                }
            },
            {
                title: 'Salary',
                start: '2025-01-15',
                end: '2025-01-15',
                color: '#10B981',
                extendedProps: {
                    type: 'income',
                    amount: 3500.00,
                    description: 'Monthly salary',
                    paid: true
                }
            },
            {
                title: 'Grocery Shopping',
                start: '2025-01-20',
                end: '2025-01-20',
                color: '#F59E0B',
                extendedProps: {
                    type: 'expense',
                    amount: 125.75,
                    description: 'Weekly groceries',
                    category: 'food'
                }
            }
        ],
        eventClick: function(info) {
            // Show event details when an event is clicked
            showEventDetails(info.event);
            info.jsEvent.preventDefault();
        },
        datesSet: function(dateInfo) {
            // Update the current date range display
            updateDateRange(dateInfo.view);
        }
    });

    // Render the calendar
    calendar.render();

    // DOM Elements
    const modal = document.getElementById('eventModal');
    const eventDetailsModal = document.getElementById('eventDetailsModal');
    const addEventBtn = document.getElementById('addEventBtn');
    const closeModalBtns = document.querySelectorAll('.close-modal');
    const cancelEventBtn = document.getElementById('cancelEvent');
    const eventForm = document.getElementById('eventForm');
    const deleteEventBtn = document.getElementById('deleteEvent');
    const prevPeriodBtn = document.getElementById('prevPeriod');
    const nextPeriodBtn = document.getElementById('nextPeriod');
    const todayBtn = document.getElementById('todayBtn');
    const viewTypeBtns = document.querySelectorAll('.view-type-btn');

    // Event Listeners
    addEventBtn.addEventListener('click', openAddEventModal);
    cancelEventBtn.addEventListener('click', closeModal);
    closeModalBtns.forEach(btn => btn.addEventListener('click', closeModal));
    eventForm.addEventListener('submit', saveEvent);
    prevPeriodBtn.addEventListener('click', () => navigateCalendar('prev'));
    nextPeriodBtn.addEventListener('click', () => navigateCalendar('next'));
    todayBtn.addEventListener('click', goToToday);
    viewTypeBtns.forEach(btn => {
        btn.addEventListener('click', () => changeView(btn.dataset.view));
    });

    // Close modal when clicking outside
    window.addEventListener('click', (e) => {
        if (e.target === modal) closeModal();
        if (e.target === eventDetailsModal) closeEventDetails();
    });

    // Functions
    function openAddEventModal() {
        // Reset form
        eventForm.reset();
        document.getElementById('eventId').value = '';
        deleteEventBtn.style.display = 'none';
        
        // Set default values
        const now = new Date();
        const startDate = now.toISOString().slice(0, 16);
        document.getElementById('eventStartDate').value = startDate;
        
        // Show modal
        modal.style.display = 'block';
    }

    function openEditEventModal(event) {
        // Fill form with event data
        document.getElementById('eventId').value = event.id || '';
        document.getElementById('eventTitle').value = event.title || '';
        document.getElementById('eventType').value = event.extendedProps.type || '';
        document.getElementById('eventStartDate').value = event.start ? event.start.toISOString().slice(0, 16) : '';
        document.getElementById('eventEndDate').value = event.end ? event.end.toISOString().slice(0, 16) : '';
        document.getElementById('eventAmount').value = event.extendedProps.amount || '';
        document.getElementById('eventCategory').value = event.extendedProps.category || '';
        document.getElementById('eventDescription').value = event.extendedProps.description || '';
        
        // Show delete button for existing events
        deleteEventBtn.style.display = 'inline-block';
        
        // Show modal
        modal.style.display = 'block';
    }

    function showEventDetails(event) {
        // Set event details
        document.getElementById('eventDetailsTitle').textContent = event.title;
        
        // Format date
        const startDate = event.start ? event.start.toLocaleDateString('en-US', { 
            weekday: 'long', 
            year: 'numeric', 
            month: 'long', 
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        }) : '';
        
        const endDate = event.end ? event.end.toLocaleDateString('en-US', { 
            weekday: 'long', 
            year: 'numeric', 
            month: 'long', 
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        }) : '';
        
        let dateText = startDate;
        if (endDate && startDate !== endDate) {
            dateText += ` - ${endDate}`;
        }
        
        document.getElementById('eventDetailsDate').textContent = dateText;
        
        // Set amount if exists
        const amountEl = document.getElementById('eventDetailsAmount');
        if (event.extendedProps.amount) {
            amountEl.style.display = 'flex';
            amountEl.querySelector('span').textContent = `$${parseFloat(event.extendedProps.amount).toFixed(2)}`;
        } else {
            amountEl.style.display = 'none';
        }
        
        // Set category if exists
        const categoryEl = document.getElementById('eventDetailsCategory');
        if (event.extendedProps.category) {
            categoryEl.style.display = 'flex';
            categoryEl.querySelector('span').textContent = formatCategory(event.extendedProps.category);
        } else {
            categoryEl.style.display = 'none';
        }
        
        // Set description if exists
        const descriptionEl = document.getElementById('eventDetailsDescription');
        if (event.extendedProps.description) {
            descriptionEl.style.display = 'flex';
            descriptionEl.querySelector('p').textContent = event.extendedProps.description;
        } else {
            descriptionEl.style.display = 'none';
        }
        
        // Set up edit button
        document.getElementById('editEventBtn').onclick = () => {
            closeEventDetails();
            openEditEventModal(event);
        };
        
        // Set up delete button
        document.getElementById('deleteEventBtn').onclick = () => {
            if (confirm('Are you sure you want to delete this event?')) {
                deleteEvent(event.id);
                closeEventDetails();
            }
        };
        
        // Show mark as paid button for bills
        const markAsPaidBtn = document.getElementById('markAsPaidBtn');
        if (event.extendedProps.type === 'bill') {
            markAsPaidBtn.style.display = 'inline-flex';
            markAsPaidBtn.onclick = () => markAsPaid(event.id, !event.extendedProps.paid);
            markAsPaidBtn.innerHTML = `<i class="fas fa-${event.extendedProps.paid ? 'times' : 'check'}-circle"></i> ${event.extendedProps.paid ? 'Mark as Unpaid' : 'Mark as Paid'}`;
        } else {
            markAsPaidBtn.style.display = 'none';
        }
        
        // Show modal
        eventDetailsModal.style.display = 'block';
    }

    function closeModal() {
        modal.style.display = 'none';
    }

    function closeEventDetails() {
        eventDetailsModal.style.display = 'none';
    }

    function saveEvent(e) {
        e.preventDefault();
        
        // Get form values
        const id = document.getElementById('eventId').value;
        const title = document.getElementById('eventTitle').value;
        const type = document.getElementById('eventType').value;
        const start = document.getElementById('eventStartDate').value;
        const end = document.getElementById('eventEndDate').value || start;
        const amount = parseFloat(document.getElementById('eventAmount').value) || 0;
        const category = document.getElementById('eventCategory').value;
        const description = document.getElementById('eventDescription').value;
        
        // Create event object
        const eventData = {
            id: id || generateId(),
            title,
            start,
            end: end !== start ? end : null,
            color: getColorForType(type),
            extendedProps: {
                type,
                amount,
                category,
                description
            }
        };
        
        // Add to calendar
        if (id) {
            // Update existing event
            const event = calendar.getEventById(id);
            if (event) {
                event.setProp('title', title);
                event.setStart(start);
                event.setEnd(end !== start ? end : null);
                event.setProp('color', getColorForType(type));
                event.setExtendedProp('type', type);
                event.setExtendedProp('amount', amount);
                event.setExtendedProp('category', category);
                event.setExtendedProp('description', description);
            }
        } else {
            // Add new event
            calendar.addEvent(eventData);
        }
        
        // In a real app, you would save to your backend here
        console.log('Event saved:', eventData);
        
        // Close modal
        closeModal();
    }

    function deleteEvent(eventId) {
        const event = calendar.getEventById(eventId);
        if (event) {
            event.remove();
            // In a real app, you would delete from your backend here
            console.log('Event deleted:', eventId);
        }
    }

    function markAsPaid(eventId, paid) {
        const event = calendar.getEventById(eventId);
        if (event) {
            event.setExtendedProp('paid', paid);
            // In a real app, you would update your backend here
            console.log(`Event ${eventId} marked as ${paid ? 'paid' : 'unpaid'}`);
            
            // Update the UI
            closeEventDetails();
            showEventDetails(event);
        }
    }

    function navigateCalendar(direction) {
        if (direction === 'prev') {
            calendar.prev();
        } else if (direction === 'next') {
            calendar.next();
        }
    }

    function goToToday() {
        calendar.today();
    }

    function changeView(view) {
        calendar.changeView(view);
        
        // Update active button
        viewTypeBtns.forEach(btn => {
            if (btn.dataset.view === view) {
                btn.classList.add('active');
            } else {
                btn.classList.remove('active');
            }
        });
    }

    function updateDateRange(view) {
        const title = view.title;
        document.getElementById('currentDateRange').textContent = title;
    }

    function formatCategory(category) {
        // Convert snake_case to Title Case
        return category
            .split('_')
            .map(word => word.charAt(0).toUpperCase() + word.slice(1))
            .join(' ');
    }

    function getColorForType(type) {
        const colors = {
            bill: '#EF4444',    // Red
            income: '#10B981',  // Green
            expense: '#F59E0B', // Yellow
            reminder: '#3B82F6', // Blue
            other: '#8B5CF6'    // Purple
        };
        return colors[type] || '#6B7280'; // Default to gray
    }

    function generateId() {
        return 'event_' + Math.random().toString(36).substr(2, 9);
    }

    // Initialize date range display
    updateDateRange(calendar.view);
});
