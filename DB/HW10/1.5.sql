-- BuyReserved(UserId, Pass, FlightId, SeatNo) — пытается выкупить забронированное место (пользователи должны совпадать).
-- Возвращает истину, если удалось и ложь — в противном случае.

create or replace function BuyReserved(in user_id int,
                                       in password varchar(80),
                                       in flight_id int,
                                       in seat_no int) returns bool
    language plpgsql
as
$$
declare
    isOk bool;
begin
    if not exists(select * from Flights where FlightId = flight_id) then
        return false;
    end if;
    call check_credentials(user_id, password, isOk);
    if not isOk then
        return false;
    end if;
    if not exists(select *
                  from Bookings
                  where FlightId = flight_id
                    and SeatNo = seat_no
                    and UserId = user_id
                    and ExpiryTime > now()) then
        return false;
    end if;
    insert into Sales (FlightId, SeatNo) values (flight_id, seat_no);
    delete from Bookings where FlightId = flight_id and SeatNo = seat_no;
    return true;
end
$$;

start transaction read write isolation level read committed;

-- Read committed достаточно. Из операций записи только добавление покупки в Sales и удаление брони из Bookings.
-- Первое не может нарушиться из-за ограничений ключа, а удалить дважды данные тем более нельзя. Поэтому
-- аномалия "косая запись" не мешает. "Фантомная запись" также не мешает, так как если мы нашли нужную бронь, нас другие
-- бронирования не интересуют. "Неповторяемое чтение" также не мешает, так как никто не мог затронуть строчку
-- с данным бронированием после того, как мы добавили билет в купленные, а купленные контролируются ограничением ключа
--
-- Read uncommitted не достаточно, так как мы пишем данные