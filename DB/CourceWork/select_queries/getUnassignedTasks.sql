deallocate all;
prepare getUnassignedTasks(int) as
  select * from Tasks
  where task_id in (select task_id from ProjectTasks
                    where project_id = $1
                      and assignee_id is null);

-- Beko
execute getUnassignedTasks(1);
-- Hw
execute getUnassignedTasks(2);
execute getUnassignedTasks(3);
execute getUnassignedTasks(4);
