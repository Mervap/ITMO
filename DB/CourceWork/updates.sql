-- repeatable read
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

-- createTask
-- repeatable read
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

-- createProjectTask
-- repeatable read
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

-- repeatable read
create or replace procedure createNoteGeneric(in note_project_id int,
                                              in note_title varchar(150),
                                              in note_text text,
                                              in note_creator_id int,
                                              in note_related_date timestamp,
                                              in note_place_id int)
  language plpgsql
as
$$
declare
  new_note_id bigint;
begin
  new_note_id := nextval('NotesIdSeq');
  insert into Notes (note_id, title, text, modification_date, creator_id, creation_date, related_date, place_id)
  values (new_note_id, note_title, note_text, now(),
          note_creator_id, now(), note_related_date, note_place_id);

  if note_project_id is not null
  then
    insert into ProjectNotes (note_id, project_id)
    values (new_note_id, note_project_id);
  end if;
end;
$$;

-- createNote
-- repeatable read
create or replace procedure createNote(in note_title varchar(150),
                                       in note_text text,
                                       in note_creator_id int,
                                       in note_related_date timestamp,
                                       in note_place_id int)
  language sql
as
$$
call createNoteGeneric(null, note_title, note_text, note_creator_id, note_related_date, note_place_id)
$$;

-- createProjectNote
-- repeatable read
create or replace procedure createProjectNote(in note_project_id int,
                                              in note_title varchar(150),
                                              in note_text text,
                                              in note_creator_id int,
                                              in note_related_date timestamp,
                                              in note_place_id int)
  language sql
as
$$
call createNoteGeneric(note_project_id, note_title, note_text, note_creator_id, note_related_date, note_place_id)
$$;

-- repeatable read
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

-- createTaskWithNotification
-- repeatable read
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


-- createProjectTaskWithNotification
-- repeatable read
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
                                       subscribe_all, task_first_notification_time, task_repeat_interval,
                                       task_severity_id)
$$;

-- createProject
-- repeatable read
create or replace procedure createProject(in project_name varchar(150),
                                          in project_creator_id int)
  language plpgsql
as
$$
declare
  new_project_id int;
begin
  new_project_id := nextval('ProjectsIdSeq');
  insert into Projects (project_id, name, creator_id)
  values (new_project_id, project_name, project_creator_id);

  insert into ProjectTeamMembers (project_id, user_id, join_date)
  values (new_project_id, project_creator_id, now());
end;
$$;

-- createProjectRole
-- read committed
prepare createProjectRole(int, varchar(100), text) as
  insert into Roles (role_id, project_id, name, description)
  values (nextval('RolesIdSeq'), $1, $2, $3);

-- addProjectTeamMember
-- read committed
prepare addProjectTeamMember(int, int) as
  insert into ProjectTeamMembers (project_id, user_id, join_date)
  values ($1, $2, now());

-- grantRoleToProjectMember
-- read committed
prepare grantRoleToProjectMember(int, int, int) as
  insert into UserRoles (user_id, role_id, project_id, grant_date)
  values ($1, $2, $3, now());

-- removeNotification
-- read committed
prepare removeNotification(int) as
  delete from Notifications
  where notification_id = $1;
