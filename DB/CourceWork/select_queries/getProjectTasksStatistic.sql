drop function if exists getTasksStatistic;
drop type TaskStatType;
create type TaskStatType as (
  all_assigned_tasks int,
  solved             int,
  in_progress        int,
  to_do              int
);
create or replace function getTasksStatistic(in tasks_to_stat Tasks[]) returns TaskStatType
  language plpgsql
as
$$
declare
  all_assigned_tasks int := 0;
  solved             int := 0;
  in_progress        int := 0;
  to_do              int := 0;
  task_row           Tasks%ROWTYPE;
  tasks_stat_cursor cursor for
    select TPS.name as process_name, count(*) as cnt
    from unnest(tasks_to_stat) natural join TaskProcessStates as TPS
    group by TPS.name;
begin
  -- Open & close cursor automatically
  for task_row in tasks_stat_cursor
    loop
      case when task_row.process_name = 'To do' then to_do := task_row.cnt;
        when task_row.process_name = 'In progress' then in_progress := task_row.cnt;
        else solved := task_row.cnt;
      end case;
    end loop;
  all_assigned_tasks := solved + in_progress + to_do;
  return (all_assigned_tasks, solved, in_progress, to_do);
end
$$;

deallocate all;
prepare getProjectTasksStatistic(int) as
  select assignee_id, (getTasksStatistic(array_agg(Tasks))).*
  from ProjectTasks natural join Tasks
  where project_id = $1
    and assignee_id is not null
  group by assignee_id;


-- beko
execute getProjectTasksStatistic(1);
-- hw
execute getProjectTasksStatistic(2);
execute getProjectTasksStatistic(3);
execute getProjectTasksStatistic(4);
