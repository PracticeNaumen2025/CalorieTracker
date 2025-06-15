### Настройка переменных окружения:
cp .env.example .env

### Запуск postgres и pgadmin:
docker-compose up -d

### Сервисы и доступ

<table>
  <thead>
    <tr>
      <th>Сервис</th>
      <th>URL</th>
      <th>Доступ / Учётные данные</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>API Server</td>
      <td><a href="http://localhost:8080/">http://localhost:8080</a></td>
      <td>—</td>
    </tr>
    <tr>
      <td>pgAdmin</td>
      <td><a href="http://localhost:5050/">http://localhost:5050</a></td>
      <td><code>admin@naumen.com / admin123</code></td>
    </tr>
    <tr>
      <td>Java Melody</td>
      <td><a href="http://localhost:8080/monitoring">http://localhost:8080/monitoring</a></td>
      <td>—</td>
    </tr>
  </tbody>
</table>