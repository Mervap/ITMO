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

start transaction read only isolation level read uncommitted;

-- Read uncommitted. Это статистический запрос, который только читает данные и который может устареть сразу после того,
-- как был показан пользователю. Так как он проходится по всем данным, мы получим совсем небольшие погрешности в полученной статистике.
-- Однако изоляция уровнеи выше может сильно ударить по производительности, так как запрос явно очень тяжелый.

