-- Реализуйте сценарий работы:
--   1) Запрос списка свободных мест.
--   2) Отображеие списка свободных мест пользователю.
--   3) Бронирование или покупка места, выбранного пользователем.

create or replace function find_sailed_or_booked(in flight_id int) returns setof int
    language plpgsql
    security definer
as
$$
begin
    return query (
        select SeatNo from Bookings where FlightId = flight_id and ExpiryTime > now()
        union
        select SeatNo from Sales where FlightId = flight_id);
end
$$;

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

create or replace function FreeSeats(in flight_id int) returns setof int
    language plpgsql
    security definer
as
$$
declare
    plane_id int;
    max_seat int;
begin
    plane_id := (select PlaneId from Flights where FlightId = flight_id);
    max_seat := (select SeatNo from Seats where PlaneId = plane_id);

    return query (select generate_series as SeatNo
                  from generate_series(1, max_seat)
                  where generate_series not in (select * from find_sailed_or_booked(flight_id)));
exception
    when no_data_found then rollback;
end
$$;

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

create or replace function BuyFree(flight_id int, in seat_no int) returns bool
    language plpgsql
as
$$
begin
    if not exists(select * from Flights where FlightId = flight_id) then
        return false;
    end if;
    if seat_no in (select * from find_sailed_or_booked(flight_id)) then
        return false;
    end if;
    insert into Sales (FlightId, SeatNo) values (flight_id, seat_no);
    return true;
end
$$;
