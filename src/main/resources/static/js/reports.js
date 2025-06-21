// Инициализация дат
function initializeDates() {
    const today = new Date();
    const thirtyDaysAgo = new Date(today.getTime() - (30 * 24 * 60 * 60 * 1000));
    document.getElementById('dateTo').value = today.toISOString().split('T')[0];
    document.getElementById('dateFrom').value = thirtyDaysAgo.toISOString().split('T')[0];
}

// Установка периода
function setPeriod(days) {
    const today = new Date();
    const startDate = new Date(today.getTime() - (days * 24 * 60 * 60 * 1000));
    document.getElementById('dateTo').value = today.toISOString().split('T')[0];
    document.getElementById('dateFrom').value = startDate.toISOString().split('T')[0];
    updateReports();
}

// График калорий по дням
let caloriesChartInstance = null;
function initializeCaloriesChart(labels, caloriesData, goalLine) {
    const ctx = document.getElementById('caloriesChart').getContext('2d');
    if (caloriesChartInstance) caloriesChartInstance.destroy();
    caloriesChartInstance = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [
                {
                    label: 'Потреблено калорий',
                    data: caloriesData,
                    borderColor: '#10b981',
                    backgroundColor: 'rgba(16, 185, 129, 0.1)',
                    tension: 0.1,
                    fill: true
                },
                {
                    label: 'Цель',
                    data: goalLine,
                    borderColor: '#f43f5e',
                    borderDash: [5, 5],
                    pointRadius: 0,
                    tension: 0
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: false
                }
            },
            plugins: {
                legend: {
                    display: true
                }
            }
        }
    });
}

// График соотношения БЖУ
let macrosChartInstance = null;
function initializeMacrosChart(protein, fat, carbs) {
    const ctx = document.getElementById('macrosChart').getContext('2d');
    if (macrosChartInstance) macrosChartInstance.destroy();
    macrosChartInstance = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Белки', 'Жиры', 'Углеводы'],
            datasets: [{
                data: [protein, fat, carbs],
                backgroundColor: [
                    '#10b981',
                    '#f59e0b',
                    '#3b82f6'
                ],
                borderWidth: 2,
                borderColor: '#ffffff'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

// Обновление отчетов
function updateReports() {
    const dateFrom = document.getElementById('dateFrom').value;
    const dateTo = document.getElementById('dateTo').value;
    fetch('/api/summary/period', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            startPeriod: dateFrom,
            endPeriod: dateTo
        })
    })
        .then(r => r.json())
        .then(data => {
            if (!Array.isArray(data) || data.length === 0) {
                document.getElementById('avgCalories').textContent = '-';
                document.getElementById('totalDays').textContent = '0';
                initializeCaloriesChart([], [], []);
                initializeMacrosChart(0, 0, 0);
                return;
            }
            // Средние значения
            let sumCalories = 0, sumProtein = 0, sumFat = 0, sumCarbs = 0;
            const labels = [];
            const caloriesData = [];
            const goalLine = [];
            data.forEach(day => {
                labels.push(day.date);
                caloriesData.push(day.totalCalories || 0);
                goalLine.push(1800); // Можно заменить на динамическую цель
                sumCalories += day.totalCalories || 0;
                sumProtein += day.totalProtein || 0;
                sumFat += day.totalFat || 0;
                sumCarbs += day.totalCarbs || 0;
            });
            const avgCalories = Math.round(sumCalories / data.length);
            const avgProtein = Math.round(sumProtein / data.length);
            const avgFat = Math.round(sumFat / data.length);
            const avgCarbs = Math.round(sumCarbs / data.length);
            document.getElementById('avgCalories').textContent = avgCalories;
            document.getElementById('totalDays').textContent = data.length;
            initializeCaloriesChart(labels, caloriesData, goalLine);
            // Процент БЖУ
            const totalMacros = avgProtein + avgFat + avgCarbs;
            let p = 0, f = 0, c = 0;
            if (totalMacros > 0) {
                p = Math.round((avgProtein / totalMacros) * 100);
                f = Math.round((avgFat / totalMacros) * 100);
                c = 100 - p - f;
            }
            initializeMacrosChart(p, f, c);
        });
}

// Экспорт функции
function exportToPDF() {
    alert('Функция экспорта в PDF будет реализована на бэкенде');
}
function exportToCSV() {
    alert('Функция экспорта в CSV будет реализована на бэкенде');
}
function printReport() {
    window.print();
}

document.addEventListener('DOMContentLoaded', function() {
    initializeDates();
    setTimeout(() => {
        updateReports();
    }, 100);
    document.getElementById('dateFrom').addEventListener('change', updateReports);
    document.getElementById('dateTo').addEventListener('change', updateReports);
}); 