create table Groups (
  group_id   int         not null,
  group_name varchar(10) not null,
  primary key (group_id),
  unique (group_name)
);

create table Students (
  student_id   int         not null,
  student_name varchar(50) not null,
  group_id     int         not null,
  primary key (student_id),
  foreign key (group_id) references Groups (group_id)
    on delete cascade
);

create table Courses (
  course_id   int          not null,
  course_name varchar(150) not null,
  primary key (course_id)
);

create table Lecturers (
  lecturer_id   int         not null,
  lecturer_name varchar(50) not null,
  primary key (lecturer_id)
);

create table Plan (
  group_id    int not null,
  course_id   int not null,
  lecturer_id int not null,
  primary key (group_id, course_id, lecturer_id),
  foreign key (group_id) references Groups (group_id)
    on delete cascade,
  foreign key (course_id) references Courses (course_id)
    on delete cascade,
  foreign key (lecturer_id) references Lecturers (lecturer_id)
    on delete cascade
);

create table Marks (
  student_id int        not null,
  course_id  int        not null,
  points     numeric(3) not null,
  primary key (student_id, course_id),
  foreign key (student_id) references Students (student_id)
    on delete cascade,
  foreign key (course_id) references Courses (course_id)
    on delete cascade
);
