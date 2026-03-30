#!/bin/bash

# Chạy ứng dụng thứ nhất ở chế độ nền (background)
java -jar /app/project-api.jar &

# Chạy ứng dụng thứ hai ở chế độ nền (background)
java -jar /app/project-oauth.jar &

# Lệnh này giúp container không bị thoát và cho phép bạn xem log của cả 2
wait -n

# Nếu một trong hai tiến trình chết, container sẽ dừng lại (tốt cho việc debug)
exit $?