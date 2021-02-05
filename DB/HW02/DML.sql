insert into People
  (person_id, first_name, last_name, birthday, passport_series, passport_no) values
  (1, 'Валерий', 'Тепляков', date '1999-07-25', '1234', '567890'),
  (2, 'Vasiliy', 'Pupkin', date '1999-01-14', '4321', '098765'),
  (3, 'Роман', 'Смирнов', null, '6666', '666666'),
  (4, 'Ярослав', 'Журба', null, '6667', '666666'),
  (5, 'Арсений', 'Серока', null, '1488', '202020'),
  (6, 'Roman', 'Melnikov', null, '8841', '020202');

insert into UniGroups
  (group_id, group_no) values
  (1, 'M3439'),
  (2, 'M3437'),
  (3, '22-O');

insert into StudentGroups
  (student_id, group_id) values
  (1, 1),
  (4, 1),
  (2, 2),
  (3, 2),
  (1, 3),
  (3, 3);

insert into Courses
  (course_id, name) values
  (1, 'Фукциональное программирование'),
  (2, 'Архитектура ЭВМ'),
  (3, 'Шахматы');

alter table GroupCourses
  drop constraint GroupCourses_TeacherGroupCourses_fkey;

insert into GroupCourses
  (group_id, course_id, teacher_id, classes) values
  (1, 1, 5, 15),
  (2, 2, 6, 10),
  (1, 3, 4, null);

insert into TeacherGroupCourses
  (group_id, course_id, teacher_id) values
  (1, 1, 5),
  (1, 1, 6),
  (2, 2, 6),
  (1, 3, 4);

alter table GroupCourses
  add constraint GroupCourses_TeacherGroupCourses_fkey
    foreign key (group_id, course_id, teacher_id)
      references TeacherGroupCourses (group_id, course_id, teacher_id);

insert into CourseResults
  (course_id, student_id, signed_id, score) values
  (1, 1, 5, 5),
  (2, 2, 6, 3),
  (3, 4, 4, 5);
