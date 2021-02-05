create table UniGroups (
  group_id   int         not null,
  group_name varchar(10) not null,
  primary key (group_id),
  unique (group_name)
);

create table Students (
  student_id int         not null,
  first_name varchar(50) not null,
  last_name  varchar(50) not null,
  group_id   int         not null,
  primary key (student_id),
  foreign key (group_id) references UniGroups (group_id)
);

create table Lecturers (
  lecturer_id int         not null,
  first_name  varchar(50) not null,
  last_name   varchar(50) not null,
  primary key (lecturer_id)
);

create table Courses (
  course_id   int          not null,
  course_name varchar(150) not null,
  primary key (course_id)
);

create table GroupCourses (
  group_id    int not null,
  course_id   int not null,
  lecturer_id int not null,
  primary key (group_id, course_id),
  foreign key (group_id) references UniGroups (group_id),
  foreign key (course_id) references Courses (course_id),
  foreign key (lecturer_id) references Lecturers (lecturer_id)
);


create table Marks (
  student_id int        not null,
  course_id  int        not null,
  mark       numeric(1) not null,
  primary key (student_id, course_id),
  foreign key (student_id) references Students (student_id),
  foreign key (course_id) references Courses (course_id)
);
