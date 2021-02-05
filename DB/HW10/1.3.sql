-- ExtendReservation(UserId, Pass, FlightId, SeatNo) — пытается продлить бронь места.
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

-- repeatable read
create or replace function ExtendReservation(in user_id int,
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
    update Bookings set ExpiryTime = ExpiryTime + interval '1 day' where FlightId = flight_id and SeatNo = seat_no;
    return true;
end
$$;

start transaction read write isolation level repeatable read;

-- Repeatable read достаточно. Нас интереснует одна конкретная запись о конкретном бронировании.
-- Появились ли другие записи - не важно
--
-- Достаточно ли read committed зависит от того, что конкретно мы хотим. Может быть такая ситуация:
-- муж с женой одновременно зашли в личный кабинет, увидели что бронь истекает, поэтому решили продлить бронь.
-- В итоге, бронь может продлиться на 2 дня вместо одного. И насколько это важно для нас, очень зависит от бизнес задач.
-- Если это не важно - read committed достаточно. Иначе нужен repeatable read.
--
-- Read uncommitted нам точно не хватит, так как мы пишем данные.