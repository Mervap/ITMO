deallocate all;
prepare getProjectTasks(int) as
  select * from Tasks
  where task_id in (select task_id from ProjectTasks where project_id = $1);

-- Project Beko Inc.
execute getProjectTasks(1);
-- Project hw
execute getProjectTasks(2);
execute getProjectTasks(3);
execute getProjectTasks(4);
