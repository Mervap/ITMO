-- 1
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

-- 2
create or replace procedure check_credentials(in user_id int, in password varchar(80), inout isOk boolean)
    language plpgsql
    security definer
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

create or replace function Reserve(in user_id int,
                                   in password varchar(80),
                                   in flight_id int,
                                   in seat_no int) returns bool
    language plpgsql
    security definer
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

-- 3

create or replace function ExtendReservation(in user_id int,
                                             in password varchar(80),
                                             in flight_id int,
                                             in seat_no int) returns bool
    language plpgsql
    security definer
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

-- 4

create or replace function BuyFree(flight_id int, in seat_no int) returns bool
    language plpgsql
    security definer
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

-- 5
create or replace function BuyReserved(in user_id int,
                                       in password varchar(80),
                                       in flight_id int,
                                       in seat_no int) returns bool
    language plpgsql
    security definer
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

-- 6

create or replace function FlightsStatistics(in user_id int, in password varchar(80))
    returns table (
                      canBook   int,
                      canBuy    int,
                      freeCnt   int,
                      bookCnt   int,
                      sailedCnt int
                  )
    language plpgsql
    security definer
as
$$
declare
    isOk           bool;
    allCnt         int := 0;
    bookedOrBought int := 0;
    bookedByUser   int := 0;
    canBook        int := 0;
    canBuy         int := 0;
    freeCnt        int := 0;
    bookCnt        int := 0;
    sailedCnt      int := 0;
begin
    call check_credentials(user_id, password, isOk);
    if not isOk then
        return;
    end if;

    allCnt := (select sum(SeatNo) from Flights natural join Seats);
    sailedCnt := (select count(*) from Sales);
    bookCnt := (select count(*) from Bookings where ExpiryTime > now());
    bookedOrBought := sailedCnt + bookCnt;
    freeCnt := allCnt - bookedOrBought;
    canBook := freeCnt;
    bookedByUser := (select count(*) from Bookings where UserId = user_id and ExpiryTime > now());
    canBuy := canBook + bookedByUser;
    return query (select *
                  from (values (canBook, canBuy, freeCnt, bookCnt, sailedCnt)) as t(canBook, canBuy, freeCnt, bookCnt, sailedCnt));
end
$$;

-- 7

create or replace function FlightStat(in user_id int, in password varchar(80), in flight_id int)
    returns table (
                      canBook   int,
                      canBuy    int,
                      freeCnt   int,
                      bookCnt   int,
                      sailedCnt int
                  )
    language plpgsql
    security definer
as
$$
declare
    isOk           bool;
    allCnt         int := 0;
    bookedOrBought int := 0;
    bookedByUser   int := 0;
    canBook        int := 0;
    canBuy         int := 0;
    freeCnt        int := 0;
    bookCnt        int := 0;
    sailedCnt      int := 0;
begin
    call check_credentials(user_id, password, isOk);
    if not isOk then
        return;
    end if;
    if not exists(select * from Flights where FlightId = flight_id) then
        return;
    end if;

    allCnt := (select sum(SeatNo)
               from Flights
                        natural join Seats
               where FlightId = flight_id);
    sailedCnt := (select count(*) from Sales where FlightId = flight_id);
    bookCnt := (select count(*) from Bookings where FlightId = flight_id and ExpiryTime > now());
    bookedOrBought := sailedCnt + bookCnt;
    freeCnt := allCnt - bookedOrBought;
    canBook := freeCnt;
    bookedByUser := (select count(*) from Bookings where FlightId = flight_id and UserId = user_id and ExpiryTime > now());
    canBuy := canBook + bookedByUser;
    return query (select *
                  from (values (canBook, canBuy, freeCnt, bookCnt, sailedCnt)) as t(canBook, canBuy, freeCnt, bookCnt, sailedCnt));
end
$$;

-- 8
create or replace procedure CompressSeats(in flight_id int)
    language plpgsql
    security definer
as
$$
declare
    available_seat int := 1;
    sailed_cursor cursor for
        select SeatNo
        from Sales
        where FlightId = flight_id
        order by SeatNo
            for update;
    booked_cursor cursor for
        select SeatNo
        from Bookings
        where FlightId = flight_id
          and ExpiryTime > now()
        order by SeatNo
            for update;
begin
    if not exists(select * from Flights where FlightId = flight_id) then
        return;
    end if;

    -- Open & close cursor automatically
    for _ in sailed_cursor
        loop
            update Sales set SeatNo = available_seat where current of sailed_cursor;
            available_seat := available_seat + 1;
        end loop;

    -- Open & close cursor automatically
    for _ in booked_cursor
        loop
            update Bookings set SeatNo = available_seat where current of booked_cursor;
            available_seat := available_seat + 1;
        end loop;
end;
$$;