-- FreeSeats(FlightId) — список мест, доступных для продажи и доступных для бронирования.

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

delete from Seats where true;
insert into Seats (PlaneId, SeatNo)
values (1, 10),
       (2, 15),
       (3, 17),
       (4, 7);

delete from Flights where true;
insert into Flights
    (FlightId, FlightTime, PlaneId)
values (1, timestamp '2020-11-01 09:50:00 AM', 1),
       (2, timestamp '2021-09-01 07:45:00 PM', 2),
       (3, timestamp '2019-12-11 02:46:00 AM', 4),
       (4, timestamp '2021-03-01 10:35:00 PM', 1);

delete from Users where true;
insert into Users
    (UserId, PasswordHash, PasswordSalt)
values (1, '591d5bc6ef35b52d007e69c0adef790d', 'b91m6g8h3'), -- hehehe
       (2, '5a9792ce5850a1a65563d4d5907bd63d', 'm7l2v7n9l'); -- hohoho

delete from Bookings where true;
insert into Bookings
    (FlightId, UserId, SeatNo, ExpiryTime)
values (3, 1, 1, now() + interval '1 day'),
       (3, 1, 2, now() + interval '1 day'),
       (3, 1, 3, now() + interval '1 day'),
       (3, 1, 4, now() + interval '1 day'),
       (3, 1, 5, now() + interval '1 day'),
       (3, 1, 6, now() + interval '1 day'),
       (3, 1, 7, now() + interval '1 day'),
       (1, 2, 2, now() + interval '1 day'),
       (1, 2, 3, now() + interval '1 day'),
       (1, 1, 7, now() + interval '1 day'),
       (4, 1, 5, now() + interval '1 day'),
       (4, 2, 2, now() + interval '1 day'),
       (2, 1, 3, now());

insert into Sales
    (FlightId, SeatNo)
values (2, 1),
       (2, 2);

-- 1..10 except 2, 3, 7
select FreeSeats(1);
-- 1..15 except 1, 2
select FreeSeats(2);
-- nothing
select FreeSeats(3);
-- 1..7 except 2, 5
select FreeSeats(4);
-- Flight not found
select FreeSeats(5);

-- After all
select * from Bookings order by expirytime;