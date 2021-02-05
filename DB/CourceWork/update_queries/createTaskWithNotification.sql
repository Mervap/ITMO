create or replace procedure createTaskWithNotificationGeneric(in task_project_id int,
                                                           in task_title varchar(150),
                                                           in task_description text,
                                                           in task_creator_id int,
                                                           in task_process_state_id int,
                                                           in task_expiry_time timestamp,
                                                           in task_place_id int,
                                                           in subscribe_creator bool,
                                                           in subscribe_all bool,
                                                           in task_first_notification_time timestamp,
                                                           in task_repeat_interval interval,
                                                           in task_severity_id int)
  language plpgsql
as
$$
declare
  new_task_id         bigint;
  new_notification_id bigint;
begin
  new_task_id := nextval('TasksIdSeq');
  new_notification_id := nextval('NotificationsIdSeq');
  insert into Tasks (task_id, title, description,
                     creator_id, creation_date, process_state_id, process_state_assignment_date,
                     expiry_date, place_id)
  values (new_task_id, task_title, task_description,
          task_creator_id, now(), task_process_state_id, now(),
          task_expiry_time, task_place_id);

  insert into Notifications (notification_id, task_id, first_notification_time, repeat_time, severity_id)
  values (new_notification_id, new_task_id, task_first_notification_time, task_repeat_interval, task_severity_id);

  if task_project_id is not null
  then
    insert into ProjectTasks (task_id, project_id, assignee_id)
    values (new_task_id, task_project_id, null);
    if subscribe_all
    then
      insert into UserNotificationSubscriptions (user_id, notification_id)
      select user_id, new_notification_id
      from ProjectTeamMembers
      where project_id = task_project_id;
    end if;
  end if;
  if subscribe_creator and (task_project_id is null or not subscribe_all)
  then
    insert into UserNotificationSubscriptions (user_id, notification_id)
    values (task_creator_id, new_notification_id);
  end if;
end;
$$;

create or replace procedure createTaskWithNotification(in task_title varchar(150),
                                                    in task_description text,
                                                    in task_creator_id int,
                                                    in task_process_state_id int,
                                                    in task_expiry_time timestamp,
                                                    in task_place_id int,
                                                    in subscribe_creator bool,
                                                    in task_first_notification_time timestamp,
                                                    in task_repeat_interval interval,
                                                    in task_severity_id int)
  language sql
as
$$
call createTaskWithNotificationGeneric(null, task_title, task_description, task_creator_id, task_process_state_id,
                                    task_expiry_time, task_place_id, subscribe_creator, false,
                                    task_first_notification_time, task_repeat_interval, task_severity_id)
$$;


create or replace procedure createProjectTaskWithNotification(in task_project_id int,
                                                           in task_title varchar(150),
                                                           in task_description text,
                                                           in task_creator_id int,
                                                           in task_process_state_id int,
                                                           in task_expiry_time timestamp,
                                                           in task_place_id int,
                                                           in subscribe_creator bool,
                                                           in subscribe_all bool,
                                                           in task_first_notification_time timestamp,
                                                           in task_repeat_interval interval,
                                                           in task_severity_id int)
  language sql
as
$$
call createTaskWithNotificationGeneric(task_project_id, task_title, task_description, task_creator_id,
                                    task_process_state_id, task_expiry_time, task_place_id, subscribe_creator,
                                    subscribe_all, task_first_notification_time, task_repeat_interval, task_severity_id)
$$;

call createTaskWithNotification('Next task1', 'ne1xt', 1, 2, now()::timestamp, 3, true, now()::timestamp + interval '1 day',
                             interval '1 day 1 hour', 3);

call createProjectTaskWithNotification(2, 'Next task1', 'n1ext', 1, 2, now()::timestamp, 3, true, true,
                                    now()::timestamp + interval '1 day',
                                    interval '1 day 1 hour', 3);

