function formatDate(date) {
    return date.toISOString().split("T")[0];
}

function getDefaultDateRange() {
    const today = new Date();
    const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
    const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);
    return [formatDate(firstDay), formatDate(lastDay)];
}

document.addEventListener("DOMContentLoaded", () => {
    const fromInput = document.getElementById("fromDate");
    const toInput = document.getElementById("toDate");

    const [fromDate, toDate] = getDefaultDateRange();
    fromInput.value = fromDate;
    toInput.value = toDate;

    document.getElementById("loadStats").addEventListener("click", () => {
        loadAllStats();
    });

    loadAllStats();
});

function loadAllStats() {
    const from = document.getElementById("fromDate").value;
    const to = document.getElementById("toDate").value;

    fetch(`/api/admin/exercises/total-entries?from=${from}&to=${to}`)
        .then(res => res.json())
        .then(data => {
            document.getElementById("totalEntries").textContent = `${data} тренировок`;
        });

    fetch(`/api/admin/exercises/total-calories?from=${from}&to=${to}`)
        .then(res => res.json())
        .then(data => {
            document.getElementById("totalCalories").textContent = `${data} ккал`;
        });

    fetch(`/api/admin/exercises/average-calories?from=${from}&to=${to}`)
        .then(res => res.json())
        .then(data => {
            document.getElementById("averageCalories").textContent = `Среднее: ${data} ккал`;
        });

    fetch(`/api/admin/exercises/popular?from=${from}&to=${to}`)
        .then(res => res.json())
        .then(data => {
            const list = document.getElementById("popularExercises");
            list.innerHTML = "";
            data.forEach(item => {
                const li = document.createElement("li");
                li.textContent = `${item.exerciseName} — ${item.usageCount} раз`;
                list.appendChild(li);
            });
        });

    fetch(`/api/admin/exercises/top-users?from=${from}&to=${to}`)
        .then(res => res.json())
        .then(data => {
            const list = document.getElementById("topUsers");
            list.innerHTML = "";
            data.forEach(item => {
                const li = document.createElement("li");
                li.textContent = `${item.userName} — ${item.caloriesBurned} ккал (${item.workoutCount} трен.)`;
                list.appendChild(li);
            });
        });

    fetch(`/api/admin/exercises/time-of-day-distribution?from=${from}&to=${to}`)
        .then(res => res.json())
        .then(data => {
            const ctx = document.getElementById("timeOfDayChart").getContext("2d");
            new Chart(ctx, {
                type: "bar",
                data: {
                    labels: Object.keys(data),
                    datasets: [{
                        label: "Количество тренировок",
                        data: Object.values(data),
                        backgroundColor: ["#4ade80", "#60a5fa", "#fbbf24"]
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        });
}
