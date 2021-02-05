deallocate all;
prepare getPersonalTasks(int) as
  select * from Tasks
  where creator_id = $1
    and task_id not in (select task_id from ProjectTasks);


-- User Mervap
execute getPersonalTasks(1);
execute getPersonalTasks(2);
-- User Bilbo
execute getPersonalTasks(3);
-- User Mike Pervotin
execute getPersonalTasks(4);
execute getPersonalTasks(5);
execute getPersonalTasks(6);
execute getPersonalTasks(7);
