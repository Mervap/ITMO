-- FlightsStatistics(UserId, Pass) — статистика по рейсам:
-- возможность бронирования и покупки, число свободных, забронированных и проданных мест.

create or replace function FlightsStatistics(in user_id int, in password varchar(80))
    returns table (
        canBook   int,
        canBuy    int,
        freeCnt   int,
        bookCnt   int,
        sailedCnt int
    )
    language plpgsql
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

-- All seats cnt: 42
-- Booked: 12 (by 1: 9, by 2: 3)
-- Sailed: 2

-- (canBook, canBuy, freeCnt, bookCnt, sailedCnt)
-- (28, 37, 28, 12, 2)
select * from FlightsStatistics(1, 'hehehe');

-- (28, 31, 28, 12, 2)
select * from FlightsStatistics(2, 'hohoho');

-- Bad pass
select * from FlightsStatistics(1, 'hehehee');
