insert into UniGroups (group_id, group_name) values
  (1, 'M3439'),
  (2, 'M3437');

insert into Students
  (student_id, first_name, last_name, group_id) values
  (1, 'Валерий', 'Тепляков', 1),
  (2, 'Vasiliy', 'Pupkin', 2),
  (3, 'Роман', 'Смирнов', 2),
  (4, 'Ярослав', 'Журба', 1);

insert into Lecturers
  (lecturer_id, first_name, last_name) values
  (1, 'Ярослав', 'Журба'),
  (2, 'Арсений', 'Серока'),
  (3, 'Roman', 'Melnikov');

insert into Courses
  (course_id, course_name) values
  (1, 'Фукциональное программирование'),
  (2, 'Архитектура ЭВМ'),
  (3, 'Шахматы');

insert into GroupCourses
  (group_id, course_id, lecturer_id) values
  (1, 1, 2),
  (2, 2, 3),
  (1, 3, 1);

insert into Marks
  (student_id, course_id, mark) values
  (1, 1, 5),
  (2, 2, 3),
  (4, 3, 5);
