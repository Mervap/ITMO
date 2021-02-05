-- CompressSeats(FlightId) — оптимизирует занятость мест в самолете.
-- В результате оптимизации, в начале самолета должны быть купленные места, затем — забронированные, а в конце — свободные.
-- Примечание: клиенты, которые уже выкупили билеты так же должны быть пересажены.

create or replace procedure CompressSeats(in flight_id int)
    language plpgsql
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

delete
from Seats
where true;
insert into Seats (PlaneId, SeatNo)
values (1, 10),
       (2, 15);

delete
from Flights
where true;
insert into Flights
    (FlightId, FlightTime, PlaneId)
values (1, timestamp '2020-11-01 09:50:00 AM', 1),
       (2, timestamp '2021-09-01 07:45:00 PM', 2);

delete
from Users
where true;
insert into Users
    (UserId, PasswordHash, PasswordSalt)
values (1, '591d5bc6ef35b52d007e69c0adef790d', 'b91m6g8h3'), -- hehehe
       (2, '5a9792ce5850a1a65563d4d5907bd63d', 'm7l2v7n9l'); -- hohoho

delete
from Bookings
where true;
insert into Bookings
    (FlightId, UserId, SeatNo, ExpiryTime)
values (1, 1, 5, now() + interval '1 day'),
       (1, 1, 7, now() + interval '1 day'),
       (1, 1, 9, now()),
       (2, 2, 11, now() + interval '1 day'),
       (2, 2, 5, now() + interval '1 day'),
       (2, 1, 10, now() + interval '1 day'),
       (2, 1, 2, now() + interval '1 day'),
       (2, 1, 12, now() + interval '1 day');

insert into Sales
    (FlightId, SeatNo)
values (1, 1),
       (1, 6),
       (2, 7),
       (2, 3),
       (2, 1);

create or replace function find_sailed_or_booked(in flight_id int) returns setof int
    language plpgsql
as
$$
begin
    return query (
        select SeatNo
        from Bookings
        where FlightId = flight_id
          and ExpiryTime > now()
        union
        select SeatNo
        from Sales
        where FlightId = flight_id);
end
$$;

create or replace function FreeSeats(in flight_id int) returns setof int
    language plpgsql
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

call CompressSeats(1);

-- 5..10
select FreeSeats(1);

-- 1: 3, 4
select * from Bookings;

-- 1: 1, 2
select * from Sales;

call CompressSeats(2);

-- 9..15
select FreeSeats(2);

-- 2: 4..8
select * from Bookings;

-- 2: 1..3
select * from Sales;
-- call CompressSeats(2);
