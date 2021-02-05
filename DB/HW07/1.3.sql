-- 1.3
truncate table Groups cascade;
insert into Groups (group_id, group_name)
  values (1, 'M3435'),
         (2, 'M3439'),
         (3, 'M3238');

truncate table Students cascade;
insert into Students
  (student_id, student_name, group_id)
  values (1, 'Иванов И.И.', 2),
         (2, 'Петров П.П.', 2),
         (3, 'Петров П.П.', 2);

select * from Groups;

delete
  from Groups g
  where g.group_id not in (
    select distinct group_id from students
  );

select * from Groups;
