-- Reserve(UserId, Pass, FlightId, SeatNo) — пытается забронировать место.
-- Возвращает истину, если удалось и ложь — в противном случае.

-- read committed (на паре было)
create or replace procedure check_credentials(in user_id int, in password varchar(80), inout isOk boolean)
    language plpgsql
as
$$
begin
    if not exists(select *
                  from Users
                  where UserId = user_id
                    and PasswordHash = md5(concat(password, PasswordSalt)))
    then
        isOk := false;
        return;
    end if;
    isOk := true;
end
$$;

-- serializable (Один покупает, другой бронирует, оба видят что место свободно, с точки зрения снепшота все ок)
create or replace function Reserve(in user_id int,
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
    if seat_no in (select * from find_sailed_or_booked(flight_id)) then
        return false;
    end if;
    if exists(select * from Bookings where FlightId = flight_id and SeatNo = seat_no) then
        update Bookings
        set UserId     = user_id,
            ExpiryTime = now() + interval '1 day'
        where FlightId = flight_id
          and SeatNo = seat_no;
    else
        insert into Bookings (FlightId, UserId, SeatNo, ExpiryTime)
        values (flight_id, user_id, seat_no, now() + interval '1 day');
    end if;
    return true;
exception
    when no_data_found then rollback;
end
$$;

start transaction read write isolation level serializable;

-- Необходимо serializable. База данных ничего не знает о связи таблиц Sales и Bookings.
-- Но с точки зрения предметной области, место не может быть одновременно куплено и забронировано.
-- При этом у нас параллельно может быть 2 операции:
--   1) Пользователь покупает билет на место
--   2) Пользователь бронирует билет на место
-- С точки зрения снепшота все хорошо, потому что никакие ограничения не наруша.тся и мы пишем в разные таблицы.
-- В итоге может сложиться ситуация, когда место будет и куплено, и забронировано. Поэтому snapshot не достаточно.