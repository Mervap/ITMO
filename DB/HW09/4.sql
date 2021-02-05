-- BuyFree(FlightId, SeatNo) — пытается купить свободное место.
-- Возвращает истину, если удалось и ложь — в противном случае.

create or replace function find_sailed_or_booked(in flight_id int) returns setof int
    language plpgsql
as
$$
begin
    return query (
        select SeatNo from Bookings where FlightId = flight_id and ExpiryTime > now()
        union
        select SeatNo from Sales where FlightId = flight_id);
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

delete from Seats where true;
insert into Seats
    (PlaneId, SeatNo)
values (1, 10);

delete from Flights where true;
insert into Flights
    (FlightId, FlightTime, PlaneId)
values (1, timestamp '2020-11-01 09:50:00 AM', 1);

delete from Users where true;
insert into Users
    (UserId, PasswordHash, PasswordSalt)
values (1, '591d5bc6ef35b52d007e69c0adef790d', 'b91m6g8h3'), -- hehehe
       (2, '5a9792ce5850a1a65563d4d5907bd63d', 'm7l2v7n9l'); -- hohoho

delete from Bookings where true;
insert into Bookings (FlightId, UserId, SeatNo, ExpiryTime)
values (1, 1, 1, now() + interval '1 day'),
       (1, 1, 2, now());

delete from Sales where true;
insert into Sales (FlightId, SeatNo)
values (1, 3);

-- Before just ok
select * from Sales;
-- Just ok
select BuyFree(1, 4);
-- After just ok
select * from Sales;

-- Booked
select BuyFree(1, 1);
-- Sailed
select BuyFree(1, 3);

-- Before expired ok
select * from Sales;
-- Expired ok
select BuyFree(1, 2);
-- After expired ok
select * from Sales;

-- After all
select * from Sales;