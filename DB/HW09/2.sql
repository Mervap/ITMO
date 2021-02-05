-- Reserve(UserId, Pass, FlightId, SeatNo) — пытается забронировать место.
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
    call expire_old(flight_id);
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

delete from Seats where true;
insert into Seats (PlaneId, SeatNo)
values (1, 10),
       (2, 7);

delete from Flights where true;
insert into Flights
    (FlightId, FlightTime, PlaneId)
values (1, timestamp '2020-11-01 09:50:00 AM', 1),
       (2, timestamp '2019-12-11 02:46:00 AM', 2);

delete from Users where true;
insert into Users
    (UserId, PasswordHash, PasswordSalt)
values (1, '591d5bc6ef35b52d007e69c0adef790d', 'b91m6g8h3'), -- hehehe
       (2, '5a9792ce5850a1a65563d4d5907bd63d', 'm7l2v7n9l'); -- hohoho

delete from Bookings where true;
insert into Bookings (FlightId, UserId, SeatNo, ExpiryTime)
values (2, 1, 1, now() + interval '1 day');

-- Before ok
select * from Bookings;
-- Ok
select Reserve(1, 'hehehe', 1, 1);
-- Now
select now();
-- After ok
select * from Bookings;

-- Bad pass
select Reserve(2, 'hoheho', 1, 2);
-- Bad flight
select Reserve(2, 'hohoho', 3, 2);
-- Reserved
select Reserve(2, 'hohoho', 2, 1);

insert into Bookings (FlightId, UserId, SeatNo, ExpiryTime)
values (2, 1, 3, now());
-- Before expired
select * from Bookings;
-- Expired
select Reserve(2, 'hohoho', 2, 3);
-- After expired
select * from Bookings;

-- After all
select * from Bookings order by expirytime;