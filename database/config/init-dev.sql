-- 1. 사용자 존재 여부 확인
SELECT user, host FROM mysql.user WHERE user = 'exporter';

-- 2. 사용자 생성 (존재하지 않는 경우)
CREATE USER 'exporter'@'%' IDENTIFIED BY 'password';

-- 3. 권한 부여
GRANT ALL PRIVILEGES ON *.* TO 'exporter'@'%';
FLUSH PRIVILEGES;

-- 4. 인증 플러그인 변경
ALTER USER 'exporter'@'%' IDENTIFIED WITH caching_sha2_password BY 'password';
