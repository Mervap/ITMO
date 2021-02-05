-- 1.1
truncate table Groups cascade;
insert into Groups (group_id, group_name)
  values (1, 'M3435'),
         (2, 'M3439'),
         (3, 'M3437');

truncate table Students cascade;
insert into Students
(student_id, student_name, group_id)
  values (1, 'Иванов И.И.', 1),
         (2, 'Петров П.П.', 1),
         (3, 'Петров П.П.', 2),
         (4, 'Сидров С.С.', 3);

truncate table Courses cascade;
insert into Courses
(course_id, course_name)
  values (1, 'Базы данных'),
         (2, 'Управление проектами');

truncate table Lecturers cascade;
insert into Lecturers
(lecturer_id, lecturer_name)
  values (1, 'Корнеев Г.А.'),
         (2, 'Шовкопляс Г.Ф.');

truncate table Plan cascade;
insert into Plan
(group_id, course_id, lecturer_id)
  values (1, 1, 1),
         (1, 2, 2),
         (2, 1, 1),
         (2, 2, 2),
         (3, 1, 1);

truncate table Marks cascade;
insert into Marks
(student_id, course_id, points)
  values (1, 1, 91),
         (1, 2, 75),
         (2, 1, 23),
         (2, 2, 67),
         (3, 2, 76),
         (4, 1, 80);

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
  );

select * from students;