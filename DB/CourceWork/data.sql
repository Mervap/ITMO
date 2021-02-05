drop sequence if exists UsersIdSeq;
create sequence UsersIdSeq as int minvalue 1 start with 1 increment by 1;
insert into Users (user_id, name)
values (nextval('UsersIdSeq'), 'Mervap'),
       (nextval('UsersIdSeq'), 'Виктор Александрович'),
       (nextval('UsersIdSeq'), 'Bilbo'),
       (nextval('UsersIdSeq'), 'Mike Pervotin'),
       (nextval('UsersIdSeq'), 'Просто Николай'),
       (nextval('UsersIdSeq'), 'Dominator239'),
       (nextval('UsersIdSeq'), 'Lexa'),
       (nextval('UsersIdSeq'), 'Student');

drop sequence if exists ProjectsIdSeq;
create sequence ProjectsIdSeq as int minvalue 1 start with 1 increment by 1;
insert into Projects (project_id, name, creator_id)
values (1, 'Project1', 2),
       (2, 'Project2', 2),
       (3, 'Project3', 2),
       (4, 'Project4', 3),
       (5, 'Project5', 3),
       (6, 'Project6', 2),
       (7, 'Project7', 4),
       (8, 'Project8', 5),
       (9, 'Project9', 2),
       (10, 'Project10', 2),
       (11, 'Project11', 2);

drop sequence if exists MetricsIdSeq;
create sequence MetricsIdSeq as int minvalue 1 start with 1 increment by 1;
insert into Metrics (metric_id, name, is_less_is_better)
values (1, 'Month expenses', true),
       (2, 'Number of users', false),
       (3, 'Unused metric', true);

insert into ProjectMetrics (project_id, metric_id, value)
values (1, 1, 40000),
       (2, 1, 150000),
       (3, 1, 23000),
       (4, 1, 2460000),
       (5, 1, 245000),
       (6, 1, 2350000),
       (7, 1, 2320000),
       (8, 1, 8790000),
       (9, 1, 1340000),
       (10, 1, 240000),
       (11, 1, 1000000000),
       (2, 2, 15),
       (5, 2, 14000),
       (7, 2, 256);

insert into ProjectTeamMembers (project_id, user_id, join_date)
values (1, 2, now() - interval '1 year'),
       (1, 4, now() - interval '25 day'),
       (1, 1, now() - interval '1 day'),
       (1, 7, now()),
       (2, 1, now() - interval '10 day'),
       (2, 8, now() - interval '1 hour'),
       (3, 7, now() - interval '50 day'),
       (3, 1, now() - interval '5 day'),
       (3, 6, now() - interval '10 day'),
       (4, 2, now() - interval '40 day'),
       (4, 5, now() - interval '30 day'),
       (4, 6, now() - interval '20 day');

drop sequence if exists RolesIdSeq;
create sequence RolesIdSeq as int minvalue 1 start with 1 increment by 1;
insert into Roles (role_id, project_id, name, description)
values (nextval('RolesIdSeq'), 1, 'CEO', 'Главный главарь'),
       (nextval('RolesIdSeq'), 1, 'Software Developer', null),
       (nextval('RolesIdSeq'), 2, 'Owner', null),
       (nextval('RolesIdSeq'), 2, 'Helper', 'Помогает'),
       (nextval('RolesIdSeq'), 3, 'Admin', null),
       (nextval('RolesIdSeq'), 4, 'B', null),
       (nextval('RolesIdSeq'), 4, 'N', null),
       (nextval('RolesIdSeq'), 4, 'D', null);

insert into UserRoles (user_id, role_id, project_id, grant_date)
values (2, 1, 1, now() - interval '1 year'),
       (4, 2, 1, now() - interval '15 day'),
       (1, 2, 1, now() - interval '1 day'),
       (1, 3, 2, now() - interval '10 day'),
       (8, 4, 2, now()),
       (1, 5, 3, now() - interval '5 day'),
       (6, 5, 3, now() - interval '10 day'),
       (7, 5, 3, now() - interval '50 day'),
       (2, 6, 4, now() - interval '35 day'),
       (5, 7, 4, now() - interval '25 day'),
       (6, 8, 4, now() - interval '15 day');

insert into TaskProcessStates (process_state_id, name)
values (1, 'To do'),
       (2, 'In progress'),
       (3, 'Done');

drop sequence if exists PlacesIdSeq;
create sequence PlacesIdSeq as int minvalue 1 start with 1 increment by 1;
insert into Places (place_id, name, description, location_latitude, location_longitude)
values (nextval('PlacesIdSeq'), 'Офис', 'Мой офис', 59.94039792617646, 30.313780082424593),
       (nextval('PlacesIdSeq'), 'Пятерочка', null, 59.93165200543046, 30.348204792519127),
       (nextval('PlacesIdSeq'), 'BND', 'BDN home', 52.552004627256046, 13.3697696095243);

drop sequence if exists TasksIdSeq;
create sequence TasksIdSeq as bigint cache 10 minvalue 1 start with 1 increment by 1;
insert into Tasks (task_id, title, description, process_state_id, process_state_assignment_date,
                   expiry_date, creation_date, creator_id, place_id)
values (nextval('TasksIdSeq'), 'Сходить в магазин', null, 1, now(), now() + interval '1 day', now(), 1, null),
       (nextval('TasksIdSeq'), 'My precious', 'Remember where the ring is', 2, now() - interval '1 day', null, now() - interval '2 day', 3, null),
       (nextval('TasksIdSeq'), 'Совещание', null, 3, now() - interval '1 day', now() - interval '1 day 1 hour', now() - interval '2 day', 2, 1),
       (nextval('TasksIdSeq'), 'Йога', null, 2, now() - interval '1 hour', null, now() - interval '1 day', 2, 1),
       (nextval('TasksIdSeq'), 'Йога', null, 3, now() - interval '1 day', null, now() - interval '2 day', 2, 1),
       (nextval('TasksIdSeq'), 'HW10', null, 3, now() - interval '30 day', null, now() - interval '37 day', 1, null),
       (nextval('TasksIdSeq'), 'Course work', null, 2, now() - interval '2 day', null, now() - interval '2 month', 1, null),
       (nextval('TasksIdSeq'), 'UI', 'Создать крутой сайт!', 1, now(), now() + interval '1 hour', now(), 2, null),
       (nextval('TasksIdSeq'), 'BE', 'Создать cупер-быстрый бекенд!!!', 2, now() - interval '1 hour', now() + interval '1 day', now() - interval '2 hour', 2, null),
       (nextval('TasksIdSeq'), 'HW9', 'Help!', 1, now(), now() + interval '1 day', now(), 1, null),
       (nextval('TasksIdSeq'), 'HW8', null, 3, now() - interval '1 day', null, now() - interval '8 day', 1, null),
       (nextval('TasksIdSeq'), 'HW7', null, 3, now() - interval '8 day', null, now() - interval '13 day', 1, null),
       (nextval('TasksIdSeq'), 'HW6', null, 3, now() - interval '17 day', null, now() - interval '25 day', 1, null),
       (nextval('TasksIdSeq'), 'HWxX?!', null, 1, now() - interval '30 day', null, now() - interval '30 day', 1, null),
       (nextval('TasksIdSeq'), 'B', 'B', 3, now() - interval '10 day', null, now() - interval '15 day', 2, null),
       (nextval('TasksIdSeq'), 'N', 'N', 3, now() - interval '8 day', null, now() - interval '15 day', 5, null),
       (nextval('TasksIdSeq'), 'D', 'D', 3, now() - interval '13 day', null, now() - interval '14 day', 6, null),
       (nextval('TasksIdSeq'), 'BN', 'B vs N', 3, now() - interval '3 day', null, now() - interval '10 day', 2, 3),
       (nextval('TasksIdSeq'), 'ND', 'N vs D', 2, now() - interval '8 day', null, now() - interval '10 day', 6, 3),
       (nextval('TasksIdSeq'), 'BD', 'B vs D', 2, now() - interval '7 day', null, now() - interval '10 day', 6, 3),
       (nextval('TasksIdSeq'), 'BND', 'B vs N vs D', 1, now() - interval '10 day', null, now() - interval '10 day', 6, 3),
       (nextval('TasksIdSeq'), 'Потусить', 'Надо потусить!!!', 1, now() - interval '35 day', now() - interval '30 day', now() - interval '35 day', 7, null),
       (nextval('TasksIdSeq'), 'FP', null, 3, now() - interval '9 day', null, now() - interval '40 day', 6, null),
       (nextval('TasksIdSeq'), 'Task', 'Task', 1, now(), null, now(), 1, null);

insert into ProjectTasks (task_id, project_id, assignee_id)
values (3, 1, null),
       (8, 1, 4),
       (9, 1, 1),
       (6, 2, 1),
       (7, 2, 1),
       (10, 2, 8),
       (11, 2, 1),
       (12, 2, 1),
       (13, 2, 1),
       (14, 2, 1),
       (15, 4, 2),
       (16, 4, 5),
       (17, 4, 6),
       (18, 4, 2),
       (19, 4, 6),
       (20, 4, null),
       (21, 4, null),
       (22, 3, null),
       (23, 3, 6);

drop sequence if exists NotesIdSeq;
create sequence NotesIdSeq as bigint cache 10 minvalue 1 start with 1 increment by 1;
insert into Notes (note_id, title, text, modification_date, related_date, creation_date, creator_id, place_id)
values (nextval('NotesIdSeq'), 'Note', 'My some personal note', now() - interval '1 hour', null, now() - interval '2 hour', 3, null),
       (nextval('NotesIdSeq'), 'Продукты', 'Хлеб, молоко, яйца', now() - interval '10 hour', null, now() - interval '10 hour', 4, 2),
       (nextval('NotesIdSeq'), 'Work', 'Important work note', now(), null, now() - interval '1 hour', 4, 2),
       (nextval('NotesIdSeq'), 'data.sql', 'create table Users (...)', now(), date '2021-01-18 10:00:00', now() - interval '3 day', 1, null),
       (nextval('NotesIdSeq'), 'BND Rules', '3 stages, vs ...', now() - interval '14 day', null, now() - interval '15 day', 6, 3),
       (nextval('NotesIdSeq'), 'FP', 'data A = B | C', now() - interval '9 day', null, now() - interval '10 day', 6, null);

insert into ProjectNotes (note_id, project_id)
values (3, 1),
       (4, 2),
       (5, 4),
       (6, 3);

drop sequence if exists TagsIdSeq;
create sequence TagsIdSeq as bigint minvalue 1 start with 1 increment by 1;
insert into Tags (tag_id, name, description, color)
values (nextval('TagsIdSeq'), 'Important', 'Very important task', x'ff0000'::int),
       (nextval('TagsIdSeq'), 'Road to the moon', null, x'bdd0e4'::int),
       (nextval('TagsIdSeq'), 'Important', 'I dont like base red color', x'b22222'::int),
       (nextval('TagsIdSeq'), 'Отдых', null, x'ffffe0'::int),
       (nextval('TagsIdSeq'), 'Hw', 'Домашка', x'c0c0c0'::int);

insert into TaskTags (task_id, tag_id)
values (4, 4),
       (5, 4),
       (7, 3),
       (9, 1),
       (8, 2),
       (9, 2),
       (10, 1),
       (15, 4),
       (16, 4),
       (17, 4),
       (18, 4),
       (19, 4),
       (20, 4),
       (21, 4),
       (21, 1),
       (21, 2),
       (22, 4),
       (23, 3),
       (6, 5),
       (10, 5),
       (11, 5),
       (12, 5),
       (13, 5),
       (23, 5);

insert into NoteTags (note_id, tag_id)
values (3, 1),
       (3, 2),
       (4, 3),
       (4, 5),
       (6, 5),
       (5, 4);

insert into NotificationSeverities (severity_id, name)
values (1, 'minor'),
       (2, 'normal'),
       (3, 'major'),
       (4, 'critical');

drop sequence if exists NotificationsIdSeq;
create sequence NotificationsIdSeq as bigint minvalue 1 start with 1 increment by 1;
insert into Notifications (notification_id, first_notification_time, repeat_time, severity_id, task_id)
values (nextval('NotificationsIdSeq'), now() - interval '1 day 2 hour', interval '15 minute', 3, 3),
       (nextval('NotificationsIdSeq'), date '2021-01-16 10:00:00', interval '12 hour', 4, 7),
       (nextval('NotificationsIdSeq'), now() + interval '12 hour', interval '3 hour', 3, 1),
       (nextval('NotificationsIdSeq'), now() - interval '35 day', interval '7 day', 2, 22);

insert into UserNotificationSubscriptions (user_id, notification_id)
values (2, 1),
       (4, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (6, 4),
       (7, 4);
