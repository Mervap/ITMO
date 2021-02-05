-- 1.2
truncate table Groups cascade;
insert into Groups (group_id, group_name)
  values (1, 'M3435'),
         (2, 'M3439'),
         (3, 'M3238');

truncate table Students cascade;
insert into Students
  (student_id, student_name, group_id)
  values (1, 'Иванов И.И.', 1),
         (2, 'Петров П.П.', 2),
         (3, 'Петров П.П.', 2),
         (4, 'Сидров С.С.', 2),
         (5, 'Неизвестный Н.Н.', 2),
         (6, 'Безымянный Б.Б', 3);

truncate table Courses cascade;
insert into Courses
  (course_id, course_name)
  values (1, 'Базы данных'),
         (2, 'Управление проектами'),
         (3, 'ППО'),
         (4, 'Теория информации');

truncate table Lecturers cascade;
insert into Lecturers
  (lecturer_id, lecturer_name)
  values (1, 'Корнеев Г.А.'),
         (2, 'Шовкопляс Г.Ф.'),
         (3, 'Кузнецова Е.М.'),
         (4, 'Киракозов А.Х.');

truncate table Plan cascade;
insert into Plan
  (group_id, course_id, lecturer_id)
  values (1, 1, 1),
         (1, 2, 2),
         (1, 3, 2),
         (1, 4, 2),
         (2, 1, 1),
         (2, 2, 2),
         (2, 3, 1),
         (3, 1, 1),
         (3, 2, 2),
         (3, 3, 2),
         (3, 4, 2);

truncate table Marks cascade;
insert into Marks
  (student_id, course_id, points)
  values (1, 1, 91),
         (1, 2, 75),
         (1, 3, 97),
         (1, 4, 85),
         (2, 1, 100),
         (2, 2, 78),
         (2, 3, 67),
         (3, 1, 76),
         (3, 3, 76),
         (4, 1, 76),
         (4, 2, 16),
         (4, 3, 54),
         (5, 1, 46),
         (5, 2, 16),
         (6, 1, 10),
         (6, 2, 10),
         (6, 3, 10),
         (6, 4, 16);

select * from students;

delete
  from Students
  where student_id in (
    select s.student_id
      from students s
             natural join plan p
             left join marks m on s.student_id = m.student_id and
                                  p.course_id = m.course_id
      where m.points is null
         or m.points < 60
      group by s.student_id
      having count(*) >= 3
  );

select * from students;
