```
Table QueueToken {
    queue_token_id bigint [pk]
    user_id bigint 
    token_value String
    remaining_time DateTime // 잔여 시간 
    position Integer // 대기 순서
    valid_until DateTime
}

Table WaitingQueue {
    waiting_queue_id bigint [pk]
    queue_token_id bigint 
    user_id bigint // 필요한 경우 역정규화
    position Integer
}

Table ProcessingQueue {
    processing_queue_id bigint [pk]
    queue_token_id bigint 
    user_id bigint // 필요한 경우 역정규화
    position Integer
}


Table Concert {
  concert_id bigint [pk]
  title String 
}

Table ConcertOption{
  concert_option_id bigint [pk]
  concert_id bigint
  concert_date datetime
  concert_duration datetime
  title String
  description String
  price decimal
}


Table TemporalReservaltion { 
  temporal_reservation_id bigint [pk]
  user_id bigint
  concert_option_id bigint
  seat_id bigint
}


Table Reservation {
    reservation_id bigint [pk]
    user_id bigint 
    concert_option_id bigint
    seat_id bigint 
    reservation_date datetime
}




Table Seat {
    seat_id bigint [pk]
    concert_id bigint
    section String
    row String
    seat_number String
    available Boolean
    reserved_until DateTime
}

Table Balance {
    balance_id bigint [pk]
    user_id bigint 
    amount Decimal
}

Table Payment {
    payment_id bigint [pk]
    user_id bigint 
    reservation_id bigint 
    amount Decimal
    status payment_status
}


Enum payment_status {
    CONFIRMED 
    CANCELLED
}


Ref: WaitingQueue.queue_token_id < QueueToken.queue_token_id
Ref: ProcessingQueue.queue_token_id < QueueToken.queue_token_id

Ref: Concert.concert_id < ConcertOption.concert_id 


Ref: ConcertOption.concert_option_id < TemporalReservaltion.concert_option_id
Ref: ConcertOption.concert_option_id < Reservation.concert_option_id

Ref: Concert.concert_id < Seat.concert_id

Ref: Reservation.reservation_id < Payment.reservation_id
```