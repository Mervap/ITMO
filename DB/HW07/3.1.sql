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

create or replace function decreaseForbidden() returns trigger
  language plpgsql
as
$$
begin
  if new is null or old.points > new.points
    then return null;
    else return new;
  end if;
end;
$$;

drop trigger if exists DecreaseForbidden on marks;
create trigger DecreaseForbidden
  before update or delete
  on marks
  for each row
execute function decreaseForbidden();

select * from marks order by student_id;
update marks set points = points + 1 where student_id = 1 and course_id = 1;
select * from marks order by student_id;
update marks set points = points - 1 where student_id = 1 and course_id = 1;
select * from marks order by student_id;
delete from marks where student_id = 1 and course_id = 1;
select * from marks order by student_id;