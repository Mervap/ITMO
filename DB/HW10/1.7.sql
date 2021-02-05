-- FlightStat(UserId, Pass, FlightId) — статистика по рейсу:
-- возможность бронирования и покупки, число свободных, забронированных и проданных мест.

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

create or replace function FlightStat(in user_id int, in password varchar(80), in flight_id int)
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

start transaction read only isolation level read committed;

-- В целом, ситуация похожа на 1.1. Если мы показываем эти данные пользователю (что вероятнее всего), стоит сделать
-- изоляцию уровня "read committed", так как тогда при экзотических ситуациях (вроде описанной в 1.1) мы с меньшей вероятностью
-- потеряем пользователя. Этот запрос не такой тяжелый как 1.6, он проходится по малому подмножеству данных и мы можем
-- позволить себе повесить некоторые блокировки. Однако если этот запрос используется для анализа внутри компании,
-- имеет смысл сделать изоляцию уровня "read uncommitted", так как мы не будем сильно ударять по параллелизму, а
-- изменения в статистике будут незначительными
