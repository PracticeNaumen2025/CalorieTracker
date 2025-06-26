document.addEventListener("DOMContentLoaded", () => {
    // Статистика пользователей
    fetch("/api/admin/users/statistics")
        .then(res => res.json())
        .then(data => {
            document.getElementById("totalUsers").textContent = data.totalUsers;
            document.getElementById("activeUsers").textContent = data.activeUsers;
            document.getElementById("deletedUsers").textContent = data.deletedUsers;

            const rolesList = document.getElementById("usersByRole");
            rolesList.innerHTML = "";
            for (const [role, count] of Object.entries(data.usersByRole)) {
                const li = document.createElement("li");
                li.innerHTML = `<span class="font-medium">${role}:</span> ${count}`;
                rolesList.appendChild(li);
            }
        });

    // Демография
    fetch("/api/admin/users/demographics")
        .then(res => res.json())
        .then(data => {
            document.getElementById("averageAge").textContent = data.averageAge.toFixed(1);
            document.getElementById("averageHeight").textContent = data.averageHeightCm;
            document.getElementById("averageWeight").textContent = data.averageWeightKg;

            const genderList = document.getElementById("genderDistribution");
            genderList.innerHTML = "";
            for (const [gender, count] of Object.entries(data.genderDistribution)) {
                const li = document.createElement("li");
                li.innerHTML = `<span class="font-medium">${gender}:</span> ${count}`;
                genderList.appendChild(li);
            }

            const ageList = document.getElementById("ageGroups");
            ageList.innerHTML = "";
            for (const [range, count] of Object.entries(data.ageGroups)) {
                const li = document.createElement("li");
                li.innerHTML = `<span class="font-medium">${range}:</span> ${count}`;
                ageList.appendChild(li);
            }
        });

    // Установка дефолтных дат (1-е и последнее число месяца)
    const fromDateInput = document.getElementById("fromDate");
    const toDateInput = document.getElementById("toDate");

    const today = new Date();
    const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
    const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);

    const formatDate = (date) => date.toISOString().split("T")[0];
    fromDateInput.value = formatDate(firstDay);
    toDateInput.value = formatDate(lastDay);

    // Подгрузка регистраций по умолчанию
    fetch(`/api/admin/users/registrations?from=${fromDateInput.value}&to=${toDateInput.value}`)
        .then(res => res.json())
        .then(data => drawRegistrationsChart(data));

    // Обработка клика по кнопке загрузки регистраций
    document.getElementById("loadRegistrations").addEventListener("click", () => {
        const from = fromDateInput.value;
        const to = toDateInput.value;
        if (!from || !to) return;
        fetch(`/api/admin/users/registrations?from=${from}&to=${to}`)
            .then(res => res.json())
            .then(data => drawRegistrationsChart(data));
    });

    // Подгрузка активных пользователей по умолчанию
    const daysInput = document.getElementById("daysInput");
    const loadActive = () => {
        fetch(`/api/admin/users/active?days=${daysInput.value}`)
            .then(res => res.json())
            .then(data => {
                document.getElementById("activeUsersCount").textContent = data.activeUsersCount;
            });
    };
    loadActive();

    document.getElementById("loadActiveUsers").addEventListener("click", loadActive);

    // График регистраций
    function drawRegistrationsChart(data) {
        const container = document.getElementById("registrationsChart");
        container.innerHTML = "";

        const canvas = document.createElement("canvas");
        container.appendChild(canvas);

        new Chart(canvas.getContext("2d"), {
            type: "bar",
            data: {
                labels: data.map(d => d.date),
                datasets: [{
                    label: "Регистрации",
                    data: data.map(d => d.count),
                    backgroundColor: "#10b981"
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
    }
});
