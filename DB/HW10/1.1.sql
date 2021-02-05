-- FreeSeats(FlightId) — список мест, доступных для продажи и доступных для бронирования.

-- read committed (не хотим учитывать то, что еще точно не забронировано)
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

-- read committed
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

start transaction read only isolation level read committed;

-- Read committed достаточно. Мы только читаем данные, поэтому аномалия "косая запись" не мешает.
-- Также все данные мы читаем только один раз, поэтому "фантомная запись" и "неповторяемое чтение" нам так же не мешают.
--
-- Read uncommitted не достаточно. С одной строны, информация о том, какие места свободны, является крайне непостоянной.
-- Когда пользователь увидит свободные места и примет решение какое же место он хочет купить/забронировать,
-- часть мест уже может быть продана или забронирована. Однако при "read uncommitted" мы можем НЕ показать места, которые
-- находятся в обработке и потенциально НЕ будут заняты (н-р другой пользователь просто отменит операцию). Тогда текущий
-- пользователь может вовсе отказать от покупки (например, ему нужно было купить 3 места, а мы показали только 2 свободных),
-- но на самом деле желание пользователя могло быть удовлетворено (третье место на самом деле не было куплено, потому что
-- второй пользователь передумал)
