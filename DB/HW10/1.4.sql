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

start transaction read write isolation level serializable;


-- Serializable. Аналогично 1.2