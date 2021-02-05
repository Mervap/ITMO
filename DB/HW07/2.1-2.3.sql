-- Data
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

-- 2.1
create or replace view Losers as
  select s.student_id, count(*) as debts_count
    from students s
           natural join plan p
           left join marks m on s.student_id = m.student_id and
                                p.course_id = m.course_id
    where m.points is null
       or m.points < 60
    group by s.student_id;

-- 2.2
drop table if exists LoserT;
create table LoserT (
  student_id  int not null,
  debts_count int not null,
  primary key (student_id),
  foreign key (student_id) references students (student_id)
);

create or replace function losersUpdate() returns trigger
  language plpgsql
as
$$
begin
  delete from LoserT where student_id = new.student_id or student_id = old.student_id;
  insert into LoserT (student_id, debts_count)
  select Q.student_id, count(*)
    from (select student_id, group_id
            from students
            where student_id = new.student_id
               or student_id = old.student_id) Q
           natural join plan P
           left join marks M on Q.student_id = M.student_id and P.course_id = M.course_id
    where m.points is null
       or m.points < 60
    group by Q.student_id;
  return new;
end;
$$;

drop trigger if exists LoserTOnPointsUpdate on marks;
create trigger LoserTOnPointsUpdate
  after insert or update or delete
  on marks
  for each row
execute function losersUpdate();

drop trigger if exists LoserTOnStudentUpdate on students;
create trigger LoserTOnStudentUpdate
  after insert or delete
  on students
  for each row
execute function losersUpdate();

select * from LoserT order by student_id;
insert into LoserT (student_id, debts_count) select student_id, debts_count from Losers;
select * from LoserT order by student_id;
update marks set points = 40 where student_id = 1 and course_id = 1;
select * from LoserT order by student_id;
update marks set points = 70 where student_id = 1 and course_id = 1;
select * from LoserT order by student_id;
insert into marks (student_id, course_id, points) values (3, 2, 60);
select * from LoserT order by student_id;
insert into students (student_id, student_name, group_id) values (7, 'Someone', 2);
select * from LoserT order by student_id;
delete from marks where points = 75;
select * from LoserT order by student_id;
update marks set student_id = 7 where student_id = 2;
select * from LoserT order by student_id;

-- 2.3
drop trigger if exists LoserTOnPointsUpdate on marks;