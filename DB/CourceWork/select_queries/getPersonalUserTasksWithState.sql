deallocate all;
prepare getPersonalTasksWithState(int, int) as
  select * from Tasks
  where creator_id = $1
    and process_state_id = $2
    and task_id not in (select task_id from ProjectTasks);


-- User Виктор Александрович TO DO
execute getPersonalTasksWithState(2, 1);
-- User Виктор Александрович In progress
execute getPersonalTasksWithState(2, 2);
-- User Виктор Александрович Done
execute getPersonalTasksWithState(2, 3);