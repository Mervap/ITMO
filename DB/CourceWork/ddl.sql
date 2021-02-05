deallocate all;

------------
-- TABLES --
------------

create table Users (
  user_id int          not null,
  name    varchar(100) not null,
  primary key (user_id),
  unique (name)
);

create table TaskProcessStates (
  process_state_id int          not null,
  name             varchar(150) not null,
  primary key (process_state_id)
);

create table Tasks (
  task_id                       bigint       not null,
  title                         varchar(150) not null,
  description                   text         null,
  creator_id                    int          not null,
  creation_date                 timestamp    not null,
  process_state_id              int          not null,
  process_state_assignment_date timestamp    not null,
  expiry_date                   timestamp    null,
  place_id                      int          null,
  primary key (task_id),
  foreign key (creator_id) references Users (user_id),
  foreign key (process_state_id) references TaskProcessStates (process_state_id),
  check ( process_state_assignment_date >= creation_date )
);

create table Notes (
  note_id           bigint       not null,
  title             varchar(150) not null,
  text              text         not null,
  modification_date timestamp    not null,
  creator_id        int          not null,
  creation_date     timestamp    not null,
  related_date      timestamp    null,
  place_id          int          null,
  primary key (note_id),
  foreign key (creator_id) references Users (user_id),
  check ( modification_date >= creation_date )
);

create table NotificationSeverities (
  severity_id int          not null,
  name        varchar(150) not null,
  primary key (severity_id)
);

create table Notifications (
  notification_id         bigint    not null,
  task_id                 bigint    not null,
  first_notification_time timestamp not null,
  repeat_time             interval  null,
  severity_id             int       not null,
  primary key (notification_id),
  foreign key (task_id) references Tasks (task_id),
  foreign key (severity_id) references NotificationSeverities (severity_id),
  check ( repeat_time > interval '0')
);

create table UserNotificationSubscriptions (
  user_id         int    not null,
  notification_id bigint not null,
  primary key (user_id, notification_id),
  foreign key (notification_id) references Notifications (notification_id) on delete cascade,
  foreign key (user_id) references Users (user_id)
);

create table Tags (
  tag_id      bigint       not null,
  name        varchar(150) not null,
  description text         null,
  color       int          not null,
  primary key (tag_id),
  check ( x'000000'::int <= color and color <= x'ffffff'::int )
);

create table TaskTags (
  task_id bigint not null,
  tag_id  bigint not null,
  primary key (task_id, tag_id),
  foreign key (task_id) references Tasks (task_id),
  foreign key (tag_id) references Tags (tag_id)
);

create table NoteTags (
  note_id bigint not null,
  tag_id  bigint not null,
  primary key (note_id, tag_id),
  foreign key (tag_id) references Tags (tag_id),
  foreign key (note_id) references Notes (note_id)
);

create table Places (
  place_id           int             not null,
  name               varchar(150)    not null,
  description        text            null,
  location_latitude  decimal(17, 15) not null,
  location_longitude decimal(18, 15) not null,
  primary key (place_id),
  check ( -90 <= location_latitude and location_longitude <= 90 ),
  check ( -180 <= location_longitude and location_longitude <= 180 )
);

create table Projects (
  project_id int          not null,
  name       varchar(150) not null,
  creator_id int          not null,
  primary key (project_id),
  foreign key (creator_id) references Users (user_id)
);

create table Metrics (
  metric_id         int          not null,
  name              varchar(150) not null,
  is_less_is_better boolean      not null,
  primary key (metric_id)
);

create table ProjectMetrics (
  project_id int not null,
  metric_id  int not null,
  value      bigint,
  primary key (project_id, metric_id),
  foreign key (project_id) references Projects (project_id),
  foreign key (metric_id) references Metrics (metric_id)
);

create table ProjectTeamMembers (
  project_id int       not null,
  user_id    int       not null,
  join_date  timestamp not null,
  primary key (project_id, user_id),
  foreign key (user_id) references Users (user_id),
  foreign key (project_id) references Projects (project_id)
);

create table ProjectTasks (
  task_id     bigint not null,
  project_id  int    not null,
  assignee_id int    null,
  primary key (task_id),
  foreign key (task_id) references Tasks (task_id),
  foreign key (project_id) references Projects (project_id),
  foreign key (project_id, assignee_id) references ProjectTeamMembers (project_id, user_id)
);

create table ProjectNotes (
  note_id    bigint not null,
  project_id int    not null,
  primary key (note_id),
  foreign key (note_id) references Notes (note_id),
  foreign key (project_id) references Projects (project_id)
);

create table Roles (
  role_id     int          not null,
  project_id  int          not null,
  name        varchar(100) not null,
  description text         null,
  primary key (project_id, role_id),
  foreign key (project_id) references Projects (project_id),
  unique (project_id, name)
);

create table UserRoles (
  user_id    int       not null,
  role_id    int       not null,
  project_id int       not null,
  grant_date timestamp not null,
  primary key (user_id, project_id, role_id),
  foreign key (project_id, user_id) references ProjectTeamMembers (project_id, user_id),
  foreign key (project_id, role_id) references Roles (project_id, role_id)
);

-----------------
-- CONSTRAINTS --
-----------------

-- Проверяет, что создатель задачи проекта является участником этого проекта
-- Проверяется отдельно, так как информация о владельце и о принадлежности проекту хранится в разных таблицах
create or replace function checkProjectTaskCreator() returns trigger
  language plpgsql
as
$$
declare
  creator_id int := -1;
begin
  creator_id := (select T.creator_id from Tasks as T where task_id = new.task_id);
  if not exists(select * from ProjectTeamMembers where project_id = new.project_id and user_id = creator_id)
  then
    raise exception 'Creator of task is not a project member';
  else
    return new;
  end if;
end;

$$;

drop trigger if exists CheckProjectTaskCreator on ProjectTasks;
create constraint trigger CheckProjectTaskCreator
  after insert or update
  on ProjectTasks
  for each row
execute function checkProjectTaskCreator();

-- Проверяет, что создатель заметки проекта является участником этого проекта
-- Проверяется отдельно, так как информация о владельце и о принадлежности проекту хранится в разных таблицах
create or replace function checkProjectNoteCreator() returns trigger
  language plpgsql
as
$$
declare
  creator_id int := -1;
begin
  creator_id := (select N.creator_id from Notes as N where note_id = new.note_id);
  if not exists(select * from ProjectTeamMembers where project_id = new.project_id and user_id = creator_id)
  then
    raise exception 'Creator of note is not a project member';
  else
    return new;
  end if;
end;

$$;

drop trigger if exists CheckProjectNoteCreator on ProjectNotes;
create constraint trigger CheckProjectNoteCreator
  after insert or update
  on ProjectNotes
  for each row
execute function checkProjectNoteCreator();

-------------
-- INDICES --
-------------

-- Users
-- По id доставать из индекса имя
create unique index on Users using btree (user_id, name);
-- По имени доставать из индекса id
create unique index on Users using btree (name, user_id);

-- TaskProcessStates
-- Таблица очень маленькая. Скорее всего хранится в памяти и пройти по
-- нескольким строкам очень дешево, индексы не нужны

-- Tasks
-- Полезно для личных задач. По юзеру достать из индекса его задачи
-- + foreign key
create unique index on Tasks using btree (creator_id, task_id);
-- По типу задачи достать из индекса сами задачи
-- Полезно для, например, фильтрации (найти все не выполненные задачи)
-- + foreign key
create unique index on Tasks using btree (process_state_id, task_id);

-- Notes
-- Полезно для личных заметок. По юзеру быстро найти его заметки
-- + foreign key
create unique index on Notes using btree (creator_id, note_id);

-- NotificationSeverities
-- Аналогично TaskProcessStates

-- Notifications
-- По важности достать уведомления на задачи. Например, пользовательское
-- приложение шлет уведомление только если уведомление важное (предполагается,
-- что это локальные настройки приложения, а не базы)
-- + foreign key
create unique index on Notifications using btree (severity_id, task_id, notification_id);
-- Найти уведомления по задаче
-- + foreign key
create index on Notifications using hash (task_id);

-- UserNotificationSubscriptions
-- Дуальный к primary key. По уведомлению достать подписанных пользователей
-- + foreign key
create unique index on UserNotificationSubscriptions using btree (notification_id, user_id);

-- Tags
-- Покрывающий. Скорее всего название тега маленькое и мы хотим
-- отображать теги вместе с задачами/заметками. Поэтому полезно
-- уметь доставать имя и цвет из индекса
create unique index on Tags using btree (tag_id, name, color);

-- TaskTags
-- Дуальный к primary key. Находить задачи по тегам (полезно для сортировки)
-- + foreign key
create unique index on TaskTags using btree (tag_id, task_id);

-- NoteTags
-- Дуальный к primary key. Находить заметки по тегам (полезно для сортировки)
-- + foreign key
create unique index on NoteTags using btree (tag_id, note_id);

-- Places
-- Вероятно, хотим показывать место на карте + писать название
-- Так как размер не большой, можем доставать из индекса
create unique index on Places using btree (place_id, name, location_latitude, location_longitude);

-- Projects
-- Доставать название проекта из индекса
create unique index on Projects using btree (project_id, name);

-- ProjectTeamMembers
-- Дуальный к primary key. Доставать из индекса проекты, в которых
-- задействован пользователь
-- + foreign key
create unique index on ProjectTeamMembers using btree (user_id, project_id);

-- ProjectTasks
-- Доставать из индекса проект, к которому относится задача
create unique index on ProjectTasks using btree (task_id, project_id);
-- Дуальный. Доставать из индекса задачи, относящиеся к проекту
-- + foreign key
create unique index on ProjectTasks using btree (project_id, task_id);

-- ProjectNotes
-- Доставать из индекса проект, к которому относится заметка
create unique index on ProjectNotes using btree (note_id, project_id);
-- Дуальный. Доставать из индекса заметки, относящиеся к проекту
-- + foreign key
create unique index on ProjectNotes using btree (project_id, note_id);

-- UserRoles
-- Достать из индекса пользователей, которые имею роль в проекте
-- Полезно при назначении задач
-- + foreign key
create unique index on UserRoles using btree (project_id, role_id, user_id);

---------------------------
-- Возможно бесполезные  --
---------------------------

-- Следующие индексы имеют неоднозначный результат
-- Их добавление должно тестироваться на реальных данных, чтобы
-- оценить, дают ли они какой-то выигрыш в производительности

-- Если мы часто показываем только название задач, полезен для
-- взятия названия из индекса (вероятное решение для мобильных устройств).
-- Если же мы сразу показываем префикс описания, то все равно нужно целиком доставать
-- запись, и индекс, вероятно, бесполезен (возможное решение для веб приложения)
create unique index on Tasks using btree (task_id, title);
-- Уже есть индекс (creator_id, task_id), но если пользователи очень часто
-- фильтруют задачи по типу, может быть полезен
create unique index on Tasks using btree (creator_id, process_state_id, task_id);

-- Аналогично Tasks (task_id, title). Однако показывать только
-- название совсем странное решение, на мой взгляд
create unique index on Notes using btree (note_id, title);

-- Уже есть индекс (severity_id, task_id, notification_id)
-- Скорее всего существует одно уведомление на 1 задачу, поэтому бесполезен.
-- Но если пользователи часто навешивают мног оуведомлений на задачи, позволит
-- быстро из находить по самой задаче
create unique index on Notifications using btree (task_id, severity_id, notification_id);

-- Уже есть индекс (project_id, task_id)
-- Может быть полезен при фильтрации задач пользователя внутри проекта
create unique index on ProjectTasks using btree (project_id, assignee_id, task_id);

-- Уже есть индекс (project_id, role_id, user_id)
-- Может быть полезен, если пользователи часто хотят смотреть, какие роли есть
-- у других пользователей. Вероятно, бесполезен
create unique index on UserRoles using btree (project_id, user_id, role_id);
