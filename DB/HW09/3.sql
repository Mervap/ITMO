-- ExtendReservation(UserId, Pass, FlightId, SeatNo) — пытается продлить бронь места.
-- Возвращает истину, если удалось и ложь — в противном случае.

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

delete from Seats where true;
insert into Seats (PlaneId, SeatNo)
values (1, 10),
       (2, 7);

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

-- Before ok
select * from Bookings
order by expirytime;
-- Ok
select ExtendReservation(1, 'hehehe', 1, 1);
-- After ok
select * from Bookings
order by expirytime;

-- Bad pass
select ExtendReservation(1, 'hehehee', 1, 1);
-- Bad user
select ExtendReservation(2, 'hohoho', 1, 1);
-- Not reserved
select ExtendReservation(1, 'hehehe', 1, 3);
-- Reservation expired
select ExtendReservation(1, 'hehehe', 1, 2);

-- After all (Bookings)
select * from Bookings order by expirytime;