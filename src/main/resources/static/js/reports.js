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
async function updateReports() {
    const dateFrom = document.getElementById('dateFrom').value;
    const dateTo   = document.getElementById('dateTo').value;

    // 1) Параллельно запрашиваем сводку за период и активную цель на dateTo
    const [summaryRes, goalRes] = await Promise.all([
        fetch('/api/summary/period', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ startPeriod: dateFrom, endPeriod: dateTo })
        }),
        fetch(`/api/goals/active?date=${dateTo}`)
    ]);

    // 2) Обработка сводки
    const data = await summaryRes.json();
    if (!Array.isArray(data) || data.length === 0) {
        document.getElementById('avgCalories').textContent = '-';
        document.getElementById('totalDays').textContent  = '0';
        initializeCaloriesChart([], [], []);
        initializeMacrosChart(0, 0, 0);
        return;
    }

    // 3) Обработка цели
    let goal;
    if (goalRes.ok) {
        goal = await goalRes.json();
    } else {
        goal = { dailyCalorieGoal: 0, proteinPercentage: 0, fatPercentage: 0, carbPercentage: 0 };
    }

    // 4) Подготовка данных для графиков и суммирование
    let sumCalories = 0, sumProtein = 0, sumFat = 0, sumCarbs = 0;
    const labels       = [];
    const caloriesData = [];
    const goalLine     = [];

    data.forEach(day => {
        labels.push(day.date);
        const cal = day.totalCalories || 0;
        caloriesData.push(cal);
        goalLine.push(goal.dailyCalorieGoal);
        sumCalories += cal;
        sumProtein  += day.totalProtein || 0;
        sumFat      += day.totalFat     || 0;
        sumCarbs    += day.totalCarbs   || 0;
    });

    const avgCalories = Math.round(sumCalories / data.length);
    const avgProtein  = sumProtein / data.length;
    const avgFat      = sumFat     / data.length;
    const avgCarbs    = sumCarbs   / data.length;

    document.getElementById('avgCalories').textContent = avgCalories;
    document.getElementById('totalDays').textContent  = data.length;

    // 5) Рисуем график калорий
    initializeCaloriesChart(labels, caloriesData, goalLine);

    // 6) Рисуем процент БЖУ по рациону
    const totalAvgMacros = avgProtein + avgFat + avgCarbs;
    let pPerc = 0, fPerc = 0, cPerc = 0;
    if (totalAvgMacros > 0) {
        pPerc = Math.round((avgProtein / totalAvgMacros) * 100);
        fPerc = Math.round((avgFat     / totalAvgMacros) * 100);
        cPerc = 100 - pPerc - fPerc;
    }
    initializeMacrosChart(pPerc, fPerc, cPerc);
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