-- getPersonalTasks
-- read committed
prepare getPersonalTasks(int) as
  select * from Tasks
  where creator_id = $1
    and task_id not in (select task_id from ProjectTasks);

-- getPersonalTasksWithState
-- read committed
prepare getPersonalTasksWithState(int, int) as
  select * from Tasks
  where creator_id = $1
    and process_state_id = $2
    and task_id not in (select task_id from ProjectTasks);

-- getPersonalNotes
-- read committed
prepare getPersonalNotes(int) as
  select * from Notes
  where creator_id = $1
    and note_id not in (select note_id from ProjectNotes);

-- getTaskTags
-- read committed
prepare getTaskTags(bigint) as
  select * from Tags
  where tag_id in (select tag_id from TaskTags where task_id = $1);

-- getNoteTags
-- read committed
prepare getNoteTags(bigint) as
  select * from Tags
  where tag_id in (select tag_id from NoteTags where note_id = $1);

-- getUserNotifications
-- read committed
prepare getUserNotifications(int) as
  select * from Notifications
  where notification_id in (select notification_id from UserNotificationSubscriptions
                            where user_id = $1);

-- getUserNotificationsWithSeverity
-- read committed
prepare getUserNotificationsWithSeverity(int, int) as
  select * from Notifications
  where notification_id in (select notification_id from UserNotificationSubscriptions
                            where user_id = $1
                              and severity_id = $2);

-- getUserProjects
-- read committed
prepare getUserProjects(int) as
  select project_id, name from Projects
  where project_id in (select project_id from ProjectTeamMembers where user_id = $1);

-- getUserRoles
-- read committed
prepare getUserRoles(int) as
  select R.project_id, R.role_id, R.name, R.description, UR.grant_date
  from Roles as R natural join UserRoles as UR
  where UR.user_id = $1;

-- getUserProjectRoles
-- read committed
prepare getUserProjectRoles(int, int) as
  select R.project_id, R.role_id, R.name, R.description, UR.grant_date
  from Roles as R natural join UserRoles as UR
  where UR.user_id = $1
    and R.project_id = $2;

-- getUsersWithProjectRole
-- read committed
prepare getUsersWithProjectRole(int, int) as
  select U.user_id, U.name
  from Users as U natural join UserRoles as UR
  where UR.project_id = $1
    and UR.role_id = $2;

-- getProjectRoles
-- read committed
prepare getProjectRoles(int) as
  select * from Roles
  where project_id = $1;

-- getProjectTasks
-- read committed
prepare getProjectTasks(int) as
  select * from Tasks
  where task_id in (select task_id from ProjectTasks where project_id = $1);

-- getProjectNotes
-- read committed
prepare getProjectNotes(int) as
  select * from Notes
  where note_id in (select note_id from ProjectNotes where project_id = $1);

-- getProjectMembers
-- read committed
prepare getProjectMembers(int) as
  select U.user_id, U.name
  from Users as U natural join ProjectTeamMembers as TM
  where TM.project_id = $1;

-- getProjectTasksWithState
-- read committed
prepare getProjectTasksWithState(int, int) as
  select * from Tasks
  where process_state_id = $2
    and task_id in (select task_id from ProjectTasks where project_id = $1);

-- getUnassignedTasks
-- read committed
prepare getUnassignedTasks(int) as
  select * from Tasks
  where task_id in (select task_id from ProjectTasks
                    where project_id = $1
                      and assignee_id is null);

-- getUserAssignedTasks
-- read committed
prepare getUserAssignedTasks(int, int) as
  select * from Tasks
  where task_id in (select task_id from ProjectTasks
                    where project_id = $2
                      and assignee_id = $1);

drop type if exists TaskStatType cascade;
create type TaskStatType as (
  all_assigned_tasks int,
  solved             int,
  in_progress        int,
  to_do              int
);

-- read committed
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

-- getProjectTasksStatistic
-- read committed. Может получиться немного несогласованно, но для каждого пользователя информация будет верна.
-- Для статистического запроса не страшно
prepare getProjectTasksStatistic(int) as
  select assignee_id, (getTasksStatistic(array_agg(Tasks))).*
  from ProjectTasks natural join Tasks
  where project_id = $1
    and assignee_id is not null
  group by assignee_id;

create or replace function calcMedian(in resolve_time_arr interval[]) returns interval
  language plpgsql
  security definer
as
$$
declare
  tasks_cnt int = -1;
begin
  tasks_cnt := (select count(*) from unnest(resolve_time_arr));
  if tasks_cnt = 0
  then
    return 0;
  else
    return (select resolve_time
            from unnest(resolve_time_arr) as resolve_time
            order by 1
            offset tasks_cnt / 2 limit 1);
  end if;
end
$$;

-- getMedianUserPersonalTaskResolveTime
-- read committed
prepare getMedianUserPersonalTaskResolveTime(int) as
  select *
  from calcMedian((select array_agg(resolve_time)
                   from (select T.process_state_assignment_date - T.creation_date as resolve_time
                         from Tasks as T natural join TaskProcessStates as TPS
                         where creator_id = $1
                           and TPS.name = 'Done'
                           and task_id not in (select task_id from ProjectTasks)) as ResolveTimes));

-- getMedianProjectResolveTime
-- read committed
prepare getMedianProjectResolveTime(int) as
  select *
  from calcMedian((select array_agg(resolve_time)
                   from (select T.process_state_assignment_date - T.creation_date as resolve_time
                         from Tasks as T natural join TaskProcessStates as TPS
                         where TPS.name = 'Done'
                           and task_id in (select task_id
                                           from ProjectTasks
                                           where project_id = $1)) as ResolveTimes));

-- getMedianUserProjectResolveTime
-- read committed
prepare getMedianUserProjectResolveTime(int, int) as
  select *
  from calcMedian((select array_agg(resolve_time)
                   from (select T.process_state_assignment_date - T.creation_date as resolve_time
                         from Tasks as T natural join TaskProcessStates as TPS
                         where TPS.name = 'Done'
                           and task_id in (select task_id
                                           from ProjectTasks
                                           where project_id = $2
                                             and assignee_id = $1)) as ResolveTimes));
