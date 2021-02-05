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

start transaction read write isolation level snapshot;

-- Snapshot достаточно. Косая запись может произойти при параллельном изменении данных о другом рейсе. Но разные рейсы никак
-- не связаны и нас это не волнует.
--
-- В то же время, repeatable read мало, так как при параллельной покупке/бронировании нового места мы можем его не увидеть
-- и не переместить. Тогда после завершения операции у нас не все места будут перемещены в начало самолета