create table members(
                        member_id bigint not null,
                        name varchar(100) not null,
                        email varchar(256) not null,
                        password varchar(256) not null,
                        primary key(member_id),
                        auth_provider varchar(256),
                        oauth2id varchar(256)
);

-- ALTER TABLE members ADD auth_provider varchar(256);
-- ALTER TABLE members ADD oauth2id varchar(256);

create table todos(
                      id bigint not null,
                      member_id bigint not null,
                      title varchar(100) not null,
                      todo_order long not null,
                      completed boolean not null,
                      primary key(id),
                      foreign key(member_id) references members(member_id)
);

create table refreshtoken(
                      id bigint not null,
                      member_id bigint,
                      refresh_token varchar(512) not null,
                      primary key(id),
                      foreign key(member_id) references members(member_id)
);