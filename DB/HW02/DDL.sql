create table People (
  person_id       int         not null,
  first_name      varchar(50) not null,
  last_name       varchar(50) not null,
  birthday        date        null,
  passport_series char(4)     not null,
  passport_no     char(6)     not null,
  primary key (person_id),
  unique (passport_series, passport_no)
);

create table UniGroups (
  group_id int         not null,
  group_no varchar(10) not null,
  primary key (group_id),
  unique (group_no)
);

create table Courses (
  course_id int          not null,
  name      varchar(150) not null,
  primary key (course_id),
  unique (name)
);

create table StudentGroups (
  student_id int not null,
  group_id   int not null,
  primary key (student_id, group_id),
  foreign key (student_id) references People (person_id),
  foreign key (group_id) references UniGroups (group_id)
);

create table CourseResults (
  course_id  int        not null,
  student_id int        not null,
  signed_id  int        not null,
  score      numeric(1) not null,
  primary key (course_id, student_id),
  foreign key (course_id) references Courses (course_id),
  foreign key (student_id) references People (person_id),
  foreign key (signed_id) references People (person_id)
);

create table GroupCourses (
  group_id   int not null,
  course_id  int not null,
  teacher_id int not null,
  classes    int null,
  primary key (group_id, course_id),
  foreign key (group_id) references UniGroups (group_id),
  foreign key (course_id) references Courses (course_id)
);

create table TeacherGroupCourses (
  group_id   int not null,
  course_id  int not null,
  teacher_id int not null,
  primary key (group_id, course_id, teacher_id),
  foreign key (group_id, course_id) references GroupCourses (group_id, course_id),
  foreign key (teacher_id) references People (person_id)
);

alter table GroupCourses
  add constraint GroupCourses_TeacherGroupCourses_fkey
    foreign key (group_id, course_id, teacher_id)
      references TeacherGroupCourses (group_id, course_id, teacher_id);