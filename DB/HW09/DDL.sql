drop table if exists Seats cascade;
create table Seats (
    PlaneId int not null,
    SeatNo  int not null,
    primary key (PlaneId)
);

drop table if exists Flights cascade;
create table Flights (
    FlightId   int       not null,
    FlightTime timestamp not null,
    PlaneId    int       not null,
    primary key (FlightId),
    foreign key (PlaneId) references Seats (PlaneId)
        on delete cascade
);

drop table if exists Users cascade;
-- Таблица пользователей с id и паролями
create table Users (
    UserId       int      not null,
    PasswordHash char(32) not null,
    PasswordSalt char(9)  not null,
    primary key (UserId)
);

drop table if exists Bookings cascade;
-- Таблица забронированных мест.
-- Для каждого места знаем на каком рейсе и кем он забронирован и когда истекает дата бронирования
create table Bookings (
    FlightId   int       not null,
    UserId     int       not null,
    SeatNo     int       not null,
    ExpiryTime timestamp not null,
    primary key (FlightId, SeatNo),
    foreign key (UserId) references Users (UserId),
    foreign key (FlightId) references Flights (FlightId)
);

drop table if exists Sales cascade;
-- Таблица купленных мест
create table Sales (
    FlightId   int       not null,
    SeatNo     int       not null,
    primary key (FlightId, SeatNo),
    foreign key (FlightId) references Flights (FlightId)
);

