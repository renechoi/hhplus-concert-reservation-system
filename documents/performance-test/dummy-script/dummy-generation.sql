explain select * from concert_option;
explain select * from seat;
explain select * from reservation;
explain select * from temporal_reservation;
explain select * from balance;
explain select * from balance_transaction;
explain select * from payment_transaction;
explain select * from waiting_queue_token_entity;





DELIMITER //

DROP PROCEDURE IF EXISTS GenerateConcertData;

CREATE PROCEDURE GenerateConcertData()
BEGIN
    DECLARE i INT DEFAULT 0;

    -- Concert 생성
    SET i = 0;
    WHILE i < 10000 DO
            INSERT INTO concert (title, created_at, request_at)
            VALUES (CONCAT('Concert ', i), NOW(), NOW());
            SET i = i + 1;
        END WHILE;

    COMMIT;
END //

DELIMITER ;

CALL GenerateConcertData();






DELIMITER //

DROP PROCEDURE IF EXISTS GenerateConcertOptionData;

CREATE PROCEDURE GenerateConcertOptionData()
BEGIN
    DECLARE j INT DEFAULT 0;
    DECLARE batchSize INT DEFAULT 100000;

    DECLARE currentTime DATETIME DEFAULT NOW();
    DECLARE randomTime DATETIME DEFAULT NOW() + INTERVAL FLOOR(RAND() * 365) DAY;
    DECLARE concertDuration TIME DEFAULT SEC_TO_TIME(FLOOR(RAND() * 7200));

    SET j = 0;
    START TRANSACTION;
    WHILE j < 1000000 DO
            INSERT INTO concert_option (concert_id, concert_date, concert_duration, title, description, price, created_at, request_at)
            VALUES (
                       MOD(j, 10000) + 1,
                       randomTime,
                       concertDuration,
                       CONCAT('Option ', j),
                       CONCAT('Description ', j),
                       FLOOR(RAND() * 100) + 10,
                       currentTime,
                       currentTime
                   );
            SET j = j + 1;

            -- batchSize 기준 커밋
            IF j % batchSize = 0 THEN
                COMMIT;
                START TRANSACTION;
            END IF;
        END WHILE;
    COMMIT;
END //

DELIMITER ;

-- Call the procedure
CALL GenerateConcertOptionData();





DELIMITER //

DROP PROCEDURE IF EXISTS GenerateSeatData;

CREATE PROCEDURE GenerateSeatData()
BEGIN
    DECLARE k INT DEFAULT 0;
    DECLARE batchSize INT DEFAULT 100000;
    DECLARE currentTime DATETIME DEFAULT NOW();

    SET k = 0;
    START TRANSACTION;
    WHILE k < 1000000 DO
            INSERT INTO seat (concert_option_id, seat_number, occupied, created_at)
            VALUES (
                       k + 1,  -- 순차적으로 증가하는 concert_option_id
                       k + 1,
                       FALSE,
                       currentTime
                   );
            SET k = k + 1;

            -- batchSize 기준 커밋
            IF k % batchSize = 0 THEN
                COMMIT;
                START TRANSACTION;
            END IF;
        END WHILE;
    COMMIT;
END //

DELIMITER ;

-- Call the procedure
CALL GenerateSeatData();




DELIMITER //

DROP PROCEDURE IF EXISTS GenerateReservationData;

CREATE PROCEDURE GenerateReservationData()
BEGIN
    DECLARE l INT DEFAULT 0;
    DECLARE batchSize INT DEFAULT 100000;

    DECLARE currentTime DATETIME DEFAULT NOW();

    SET l = 0;
    START TRANSACTION;
    WHILE l < 1000000 DO
            INSERT INTO reservation (user_id, concert_option_id, seat_id, is_canceled, temporal_reservation_reserve_at, reserve_at, created_at, request_at)
            VALUES (
                       l + 1,  -- 순차적으로 증가하는 user_id
                       l + 1,  -- 순차적으로 증가하는 concert_option_id
                       l + 1,  -- 순차적으로 증가하는 seat_id
                       FALSE,
                       currentTime,
                       currentTime,
                       currentTime,
                       currentTime
                   );
            SET l = l + 1;

            -- batchSize 기준 커밋
            IF l % batchSize = 0 THEN
                COMMIT;
                START TRANSACTION;
            END IF;
        END WHILE;
    COMMIT;
END //

DELIMITER ;

-- Call the procedure
CALL GenerateReservationData();





DELIMITER //

DROP PROCEDURE IF EXISTS GenerateTemporalReservationData;

CREATE PROCEDURE GenerateTemporalReservationData()
BEGIN
    DECLARE m INT DEFAULT 0;
    DECLARE batchSize INT DEFAULT 100000;

    DECLARE currentTime DATETIME DEFAULT NOW();
    DECLARE randomTime2 DATETIME;

    SET m = 0;
    START TRANSACTION;
    WHILE m < 1000000 DO
            SET randomTime2 = currentTime + INTERVAL FLOOR(RAND() * 10) MINUTE;
            INSERT INTO temporal_reservation (user_id, concert_option_id, seat_id, is_confirmed, reserve_at, expire_at, created_at, request_at, is_canceled)
            VALUES (
                       m + 1,  -- 순차적으로 증가하는 user_id
                       m + 1,  -- 순차적으로 증가하는 concert_option_id
                       m + 1,  -- 순차적으로 증가하는 seat_id
                       FALSE,
                       currentTime,
                       randomTime2,
                       currentTime,
                       currentTime,
                       FALSE
                   );
            SET m = m + 1;

            -- batchSize 기준 커밋
            IF m % batchSize = 0 THEN
                COMMIT;
                START TRANSACTION;
            END IF;
        END WHILE;
    COMMIT;
END //

DELIMITER ;

-- Call the procedure
CALL GenerateTemporalReservationData();
















DELIMITER //

DROP PROCEDURE IF EXISTS GenerateBalanceData;

CREATE PROCEDURE GenerateBalanceData()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE batchSize INT DEFAULT 100000;

    DECLARE currentTime DATETIME DEFAULT NOW();
    DECLARE randomAmount DECIMAL(10, 2) DEFAULT 0;

    SET i = 0;
    START TRANSACTION;
    WHILE i < 1000000 DO
            SET randomAmount = FLOOR(RAND() * 10000) / 100;

            INSERT INTO balance (user_id, amount, created_at, updated_at)
            VALUES (
                       i + 1,  -- 순차적으로 증가하는 user_id
                       randomAmount,
                       currentTime,
                       currentTime
                   );

            SET i = i + 1;

            IF i % batchSize = 0 THEN
                COMMIT;
                START TRANSACTION;
            END IF;
        END WHILE;
    COMMIT;
END //

DELIMITER ;

-- Call the procedure
CALL GenerateBalanceData();






DELIMITER //

DROP PROCEDURE IF EXISTS GenerateBalanceTransactionData;

CREATE PROCEDURE GenerateBalanceTransactionData()
BEGIN
    DECLARE j INT DEFAULT 0;
    DECLARE batchSize INT DEFAULT 10000;

    DECLARE currentTime DATETIME DEFAULT NOW();
    DECLARE randomAmount DECIMAL(10, 2) DEFAULT 0;
    DECLARE randomTransactionType VARCHAR(10);
    DECLARE randomTransactionReason VARCHAR(15);

    SET j = 0;
    START TRANSACTION;
    WHILE j < 1000000 DO
            SET randomAmount = FLOOR(RAND() * 10000) / 100;
            SET randomTransactionType = IF(RAND() < 0.5, 'CHARGE', 'USE');
            SET randomTransactionReason = ELT(FLOOR(RAND() * 5) + 1, 'NORMAL', 'PAYMENT', 'REFUND', 'ROLLED_BACK', 'ADJUSTMENT');

            INSERT INTO balance_transaction (user_id, amount, transaction_type, transaction_reason, created_at)
            VALUES (
                       j + 1,  -- 순차적으로 증가하는 user_id
                       randomAmount,
                       randomTransactionType,
                       randomTransactionReason,
                       currentTime
                   );

            SET j = j + 1;

            IF j % batchSize = 0 THEN
                COMMIT;
                START TRANSACTION;
            END IF;
        END WHILE;
    COMMIT;
END //

DELIMITER ;

-- Call the procedure
CALL GenerateBalanceTransactionData();


DELIMITER //

DROP PROCEDURE IF EXISTS GeneratePaymentTransactionData;

CREATE PROCEDURE GeneratePaymentTransactionData()
BEGIN
    DECLARE k INT DEFAULT 0;
    DECLARE batchSize INT DEFAULT 100000;

    DECLARE currentTime DATETIME DEFAULT NOW();
    DECLARE randomAmount DECIMAL(10, 2) DEFAULT 0;
    DECLARE randomPaymentMethod VARCHAR(15);
    DECLARE randomPaymentStatus VARCHAR(10);

    SET k = 0;
    START TRANSACTION;
    WHILE k < 1000000 DO
            SET randomAmount = FLOOR(RAND() * 10000) / 100;
            SET randomPaymentMethod = ELT(FLOOR(RAND() * 4) + 1, 'CREDIT_CARD', 'DEBIT_CARD', 'PAYPAL', 'BANK_TRANSFER');
            SET randomPaymentStatus = ELT(FLOOR(RAND() * 4) + 1, 'COMPLETE', 'FAILED', 'PENDING', 'CANCELLED');

            INSERT INTO payment_transaction (user_id, target_id, amount, payment_method, payment_status, created_at)
            VALUES (
                       k + 1,  -- 순차적으로 증가하는 user_id
                       CONCAT('Target', MOD(k, 10000) + 1),
                       randomAmount,
                       randomPaymentMethod,
                       randomPaymentStatus,
                       currentTime
                   );

            SET k = k + 1;

            IF k % batchSize = 0 THEN
                COMMIT;
                START TRANSACTION;
            END IF;
        END WHILE;
    COMMIT;
END //

DELIMITER ;

-- Call the procedure
CALL GeneratePaymentTransactionData();




DELIMITER //

DROP PROCEDURE IF EXISTS GenerateWaitingQueueTokenData;

CREATE PROCEDURE GenerateWaitingQueueTokenData()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE batchSize INT DEFAULT 100000;
    DECLARE currentTime DATETIME DEFAULT NOW();
    DECLARE randomTime DATETIME DEFAULT NOW() + INTERVAL FLOOR(RAND() * 365) DAY;
    DECLARE status ENUM('WAITING', 'PROCESSING', 'COMPLETED', 'EXPIRED');

    SET i = 0;
    START TRANSACTION;
    WHILE i < 1000000 DO
            INSERT INTO waiting_queue_token_entity (
                user_id,
                token_value,
                position,
                valid_until,
                status,
                created_at,
                request_at
            )
            VALUES (
                       i,  -- user_id를 숫자로 설정
                       i,  -- token_value를 숫자로 설정
                       i + 1,
                       randomTime,
                       ELT(FLOOR(RAND() * 4) + 1, 'WAITING', 'PROCESSING', 'COMPLETED', 'EXPIRED'),
                       currentTime,
                       currentTime
                   );

            SET i = i + 1;

            -- batchSize 기준 커밋
            IF i % batchSize = 0 THEN
                COMMIT;
                START TRANSACTION;
            END IF;
        END WHILE;
    COMMIT;
END //

DELIMITER ;

-- Call the procedure
CALL GenerateWaitingQueueTokenData();




























