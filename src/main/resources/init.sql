create table members(
                        member_id bigint not null,
                        name varchar(100) not null,
                        email varchar(256) not null,
                        password varchar(256) not null,
                        primary key(member_id)
);

create table todos(
                      id bigint not null,
                      member_id bigint not null,
                      title varchar(100) not null,
                      todo_order long not null,
                      completed boolean not null,
                      primary key(id),
                      foreign key(member_id) references members(member_id)
);