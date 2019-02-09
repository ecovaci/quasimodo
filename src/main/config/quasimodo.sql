-- ECO area
drop table eco;
create table eco (
id int auto_increment primary key,
code varchar not null,
name varchar not null,
variation varchar,
moves varchar not null
);

create index idx_code on eco (code);
create index idx_moves on eco (moves);

-- PGN area

--create table Game (
--    id integer primary key autoincrement,
--    content varchar,
--    event varchar,
--    site varchar(20),
--    game_date varchar(12),
--    round varchar(20),
--    white varchar,
--    black varchar,
--    result varchar(8),
--    white_title varchar(10), 
--    black_title varchar(10),
--    white_elo varchar(5), 
--    black_elo varchar(5),
--    eco varchar(5)
--);
--
--create table Position (
--    game_id int,
--    z_key int
--);
--
--alter table Position
--add foreign key (game_id) references Game(id);
