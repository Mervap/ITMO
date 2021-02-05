delete from UniGroups;
insert into UniGroups (group_id, group_name)
  values (1, 'M3435'),
         (2, 'M3439'),
         (3, 'M3238'),
         (4, 'M3239');

delete from Students;
insert into Students
  (student_id, student_name, group_id)
  values (1, 'Иванов И.И.', 1),
         (2, 'Петров П.П.', 1),
         (3, 'Петров П.П.', 2),
         (4, 'Сидров С.С.', 2),
         (5, 'Неизвестный Н.Н.', 3),
         (6, 'Безымянный Б.Б', 4),
         (7, 'Иксов И.И', 2),
         (8, 'Игреков И.И', 2);

delete from Courses;
insert into Courses
  (course_id, course_name)
  values (1, 'Базы данных'),
         (2, 'Управление проектами'),
         (3, 'ППО'),
         (4, 'Теория информации'),
         (6, 'Математический анализ'),
         (7, 'Технологии Java');

delete from Lecturers;
insert into Lecturers
  (lecturer_id, lecturer_name)
  values (1, 'Корнеев Г.А.'),
         (2, 'Шовкопляс Г.Ф.'),
         (3, 'Кузнецова Е.М.'),
         (4, 'Киракозов А.Х.'),
         (5, 'Якуба Н.В.'),
         (6, 'Трофимюк Г.А.'),
         (7, 'Кудряшов Б.Д.'),
         (8, 'Кохась К.П.');

delete from Plan;
insert into Plan
  (group_id, course_id, lecturer_id)
  values (1, 1, 2),
         (2, 1, 1),
         (1, 2, 3),
         (1, 3, 4),
         (2, 3, 4),
         (1, 4, 5),
         (2, 4, 6),
         (1, 4, 7),
         (2, 4, 7),
         (4, 6, 8),
         (1, 7, 1),
         (2, 7, 1),
         (3, 7, 1),
         (4, 7, 1);

delete from Marks;
insert into Marks
  (student_id, course_id, points)
  values (1, 1, 91),
         (2, 1, 62),
         (3, 1, 23),
         (2, 2, 100),
         (3, 2, 76),
         (4, 2, 85),
         (7, 1, 56),
         (8, 1, 91),
         (7, 7, 57),
         (8, 7, 34),
         (5, 7, 96),
         (6, 7, 25),
         (3, 3, 16);