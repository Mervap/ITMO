deallocate all;
prepare getProjectTasksWithState(int, int) as
  select * from Tasks
  where process_state_id = $2
    and task_id in (select task_id from ProjectTasks where project_id = $1);

-- Project Beko Inc. To do
execute getProjectTasksWithState(1, 1);
-- Project Beko Inc. inpr
execute getProjectTasksWithState(1, 2);
-- Project Beko Inc. done
execute getProjectTasksWithState(1, 3);
-- Project hw to do
execute getProjectTasksWithState(2, 1);
-- Project hw inpr
execute getProjectTasksWithState(2, 2);
-- Project hw done
execute getProjectTasksWithState(2, 3);