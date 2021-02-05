-- 1.1
delete
  from Students
  where student_id in (
    select S.student_id
      from Students S
             natural join Plan P
             left join Marks M on S.student_id = M.student_id and
                                  P.course_id = M.course_id
      where M.points is null
         or M.points < 60
  );

-- 1.2
delete
  from Students
  where student_id in (
    select S.student_id
      from Students S
             natural join Plan P
             left join Marks M on S.student_id = M.student_id and
                                  P.course_id = M.course_id
      where M.points is null
         or M.points < 60
      group by S.student_id
      having count(*) >= 3
  );

-- 1.3
delete
  from Groups G
  where G.group_id not in (
    select distinct group_id from Students
  );

-- 2.1
create or replace view Losers as
  select S.student_id, count(*) as debts_count
    from Students S
           natural join Plan P
           left join Marks M on S.student_id = M.student_id and
                                P.course_id = M.course_id
    where M.points is null
       or M.points < 60
    group by S.student_id;

-- 2.2
create table LoserT (
  student_id  int not null,
  debts_count int not null,
  primary key (student_id),
  foreign key (student_id) references Students (student_id)
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
            from Students
            where student_id = new.student_id
               or student_id = old.student_id) Q
           natural join Plan P
           left join Marks M on Q.student_id = M.student_id and P.course_id = M.course_id
    where M.points is null
       or M.points < 60
    group by Q.student_id;
  return new;
end;
$$;

create trigger LoserTOnPointsUpdate
  after insert or update or delete
  on Marks
  for each row
execute function losersUpdate();

-- 2.3
drop trigger if exists LoserTOnPointsUpdate on marks;

-- 2.4
with CD as (select NP.student_id, count(*) as closed_debts
              from NewPoints NP left join Marks M
                on NP.student_id = M.student_id and
                   NP.course_id = M.course_id
              where (M.points is null or M.points < 60)
                and coalesce(M.points, 0) + NP.new_points >= 60
              group by NP.student_id),
     DEL as (delete from LoserT where student_id in (
       select student_id
         from LoserT L natural join CD
         where L.debts_count - closed_debts = 0)
       returning student_id)
update LoserT L
  set debts_count = L.debts_count - CDDEL.closed_debts
  from (select student_id, closed_debts from CD where student_id not in (select * from DEL)) CDDEL;

-- 3.1

-- WTF??

--3.2

create or replace function decreaseForbidden() returns trigger
  language plpgsql
as
$$
begin
  if new is null or old.points > new.points
  then return null; -- To stop delete
  else return new;
  end if;
end;
$$;

create trigger DecreaseForbidden
  before update or delete
  on Marks
  for each row
execute function decreaseForbidden();
