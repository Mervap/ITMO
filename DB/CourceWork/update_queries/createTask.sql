create or replace procedure createTaskGeneric(in task_project_id int,
                                           in task_title varchar(150),
                                           in task_description text,
                                           in task_creator_id int,
                                           in task_process_state_id int,
                                           in task_expiry_time timestamp,
                                           in task_place_id int)
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

  if task_project_id is not null
  then
    insert into ProjectTasks (task_id, project_id, assignee_id)
    values (new_task_id, task_project_id, null);
  end if;
end;
$$;

create or replace procedure createTask(in task_title varchar(150),
                                    in task_description text,
                                    in task_creator_id int,
                                    in task_process_state_id int,
                                    in task_expiry_time timestamp,
                                    in task_place_id int)
  language sql
as
$$
call createTaskGeneric(null, task_title, task_description, task_creator_id,
                    task_process_state_id, task_expiry_time, task_place_id)
$$;


create or replace procedure createProjectTask(in task_project_id int,
                                           in task_title varchar(150),
                                           in task_description text,
                                           in task_creator_id int,
                                           in task_process_state_id int,
                                           in task_expiry_time timestamp,
                                           in task_place_id int)
  language sql
as
$$
call createTaskGeneric(task_project_id, task_title, task_description, task_creator_id,
                                    task_process_state_id, task_expiry_time, task_place_id)
$$;

call createTask('Ne2xt task1', 'ne21xt', 1, 2, now()::timestamp, 3);

call createProjectTask(2, 'Next task2', 'n1ext', 1, 2, now()::timestamp, 3);

