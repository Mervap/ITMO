deallocate all;
prepare getUserAssignedTasks(int, int) as
  select * from Tasks
  where task_id in (select task_id from ProjectTasks
                    where project_id = $2
                      and assignee_id = $1);

-- Beko Mervap
execute getUserAssignedTasks(1, 1);
-- Hw Mervap
execute getUserAssignedTasks(1, 2);
-- Beko Виктор Александрович
execute getUserAssignedTasks(2, 1);
-- Beko Mike Pervotin
execute getUserAssignedTasks(4, 1);
