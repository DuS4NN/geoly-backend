create table badge
(
	id int not null
		primary key,
	image_url varchar(100) null,
	name varchar(20) null
)
engine=InnoDB
;

create table category
(
	id int not null
		primary key,
	image_url varchar(100) null,
	name varchar(20) null
)
engine=InnoDB
;

create table language
(
	id int not null
		primary key,
	image_url varchar(100) null,
	name varchar(20) null
)
engine=InnoDB
;

create table log
(
	id int not null
		primary key,
	created_at timestamp null,
	data json null,
	log_type varchar(20) null
)
engine=InnoDB
;

create table role
(
	id int not null
		primary key,
	name varchar(20) null
)
engine=InnoDB
;

create table sale
(
	id int not null
		primary key,
	active tinyint default '1' null,
	created_at timestamp null,
	end_at timestamp null,
	start_at timestamp null
)
engine=InnoDB
;

create table user
(
	id int not null
		primary key,
	about varchar(500) not null,
	active tinyint default '1' null,
	address varchar(30) null,
	created_at timestamp null,
	email varchar(254) null,
	nick_name varchar(15) null,
	password varchar(60) null,
	profile_image_url varchar(100) default '/static/image/default_profile_image.png' null,
	verified tinyint default '0' null,
	constraint UK_ob8kqyqqgmefl0aco34akdtpe
		unique (email),
	constraint UK_d2ia11oqhsynodbsi46m80vfc
		unique (nick_name)
)
engine=InnoDB
;

create table party
(
	id int not null
		primary key,
	created_at timestamp null,
	name varchar(15) null,
	user_id int null,
	constraint FKtcag4fsdqkmo7owjkk1p25h41
		foreign key (user_id) references user (id)
)
engine=InnoDB
;

create index FKtcag4fsdqkmo7owjkk1p25h41
	on party (user_id)
;

create table party_invate
(
	id int not null
		primary key,
	created_at timestamp null,
	status varchar(20) null,
	party_id int null,
	user_id int null,
	constraint FKpyswset8n8eh0tfmurgqxs699
		foreign key (party_id) references party (id),
	constraint FKd54or3m1px5xyp6q7c99heoot
		foreign key (user_id) references user (id)
)
engine=InnoDB
;

create index FKd54or3m1px5xyp6q7c99heoot
	on party_invate (user_id)
;

create index FKpyswset8n8eh0tfmurgqxs699
	on party_invate (party_id)
;

create table party_invite
(
	id int not null
		primary key,
	created_at timestamp null,
	status varchar(255) null,
	party_id int null,
	user_id int null,
	constraint FKg0d7md7nqv6y8unxo7tfr722f
		foreign key (party_id) references party (id),
	constraint FK5n5bg86wu1ledopg6in964h6j
		foreign key (user_id) references user (id)
)
engine=InnoDB
;

create index FK5n5bg86wu1ledopg6in964h6j
	on party_invite (user_id)
;

create index FKg0d7md7nqv6y8unxo7tfr722f
	on party_invite (party_id)
;

create table party_user
(
	id int not null
		primary key,
	created_at timestamp null,
	party_id int null,
	user_id int null,
	constraint FKoatbt6tv26kk2r5vgua7ojsm
		foreign key (party_id) references party (id),
	constraint FKgbm3cruyq6n25lriiyc2xad43
		foreign key (user_id) references user (id)
)
engine=InnoDB
;

create index FKgbm3cruyq6n25lriiyc2xad43
	on party_user (user_id)
;

create index FKoatbt6tv26kk2r5vgua7ojsm
	on party_user (party_id)
;

create table point
(
	id int not null
		primary key,
	amount int null,
	created_at timestamp null,
	user_id int null,
	constraint FKh4qxmn9mig6kith0ish2r67ka
		foreign key (user_id) references user (id)
)
engine=InnoDB
;

create index FKh4qxmn9mig6kith0ish2r67ka
	on point (user_id)
;

create table quest
(
	id int not null
		primary key,
	active tinyint null,
	created_at timestamp null,
	daily tinyint null,
	description varchar(500) not null,
	difficulty tinyint null,
	private_quest tinyint null,
	category_id int null,
	user_id int null,
	constraint FK4foeblydmua10qb2oeuane6fb
		foreign key (category_id) references category (id),
	constraint FKlgsc5aj1jqdfwj8v3pkhbidnx
		foreign key (user_id) references user (id)
)
engine=InnoDB
;

create table image
(
	id int not null
		primary key,
	created_at timestamp null,
	image_url varchar(100) null,
	quest_id int null,
	constraint FK3n5negxv9eym0std2euttvg6w
		foreign key (quest_id) references quest (id)
)
engine=InnoDB
;

create index FK3n5negxv9eym0std2euttvg6w
	on image (quest_id)
;

create table party_quest
(
	id int not null
		primary key,
	active tinyint null,
	party_id int null,
	quest_id int null,
	constraint FK2gigo39gg9climkuaalbb2od0
		foreign key (party_id) references party (id),
	constraint FK8fl0nu8hgj8jesc5hpwbio1ub
		foreign key (quest_id) references quest (id)
)
engine=InnoDB
;

create index FK2gigo39gg9climkuaalbb2od0
	on party_quest (party_id)
;

create index FK8fl0nu8hgj8jesc5hpwbio1ub
	on party_quest (quest_id)
;

create index FK4foeblydmua10qb2oeuane6fb
	on quest (category_id)
;

create index FKlgsc5aj1jqdfwj8v3pkhbidnx
	on quest (user_id)
;

create table quest_report
(
	id int not null
		primary key,
	created_at timestamp null,
	reason varchar(20) null,
	quest_id int null,
	user_id int null,
	constraint FKj20n6bqiyj8mvbjwr5exdh37t
		foreign key (quest_id) references quest (id),
	constraint FKodp2v6api3il5gl748pxypkwj
		foreign key (user_id) references user (id)
)
engine=InnoDB
;

create index FKj20n6bqiyj8mvbjwr5exdh37t
	on quest_report (quest_id)
;

create index FKodp2v6api3il5gl748pxypkwj
	on quest_report (user_id)
;

create table quest_review
(
	id int not null
		primary key,
	created_at timestamp null,
	review tinyint null,
	review_text varchar(500) null,
	quest_id int null,
	user_id int null,
	constraint FK1hjwwrr4xddjj9c7iicvvm78j
		foreign key (quest_id) references quest (id),
	constraint FKqg1fbvcavq2lbpp8g8w8vtiam
		foreign key (user_id) references user (id)
)
engine=InnoDB
;

create index FK1hjwwrr4xddjj9c7iicvvm78j
	on quest_review (quest_id)
;

create index FKqg1fbvcavq2lbpp8g8w8vtiam
	on quest_review (user_id)
;

create table stage
(
	id int not null
		primary key,
	answer varchar(200) null,
	latiude varchar(10) null,
	longitude varchar(10) null,
	question varchar(200) null,
	type varchar(255) null,
	quest_id int null,
	latitude varchar(10) null,
	constraint FKmujks4h5f77vk9x2hh1l4ypvg
		foreign key (quest_id) references quest (id)
)
engine=InnoDB
;

create index FKmujks4h5f77vk9x2hh1l4ypvg
	on stage (quest_id)
;

create table token
(
	id int not null
		primary key,
	action varchar(20) null,
	created_at timestamp null,
	token varchar(60) null,
	user_id int null,
	constraint FKe32ek7ixanakfqsdaokm4q9y2
		foreign key (user_id) references user (id)
)
engine=InnoDB
;

create index FKe32ek7ixanakfqsdaokm4q9y2
	on token (user_id)
;

create table transaction
(
	id int not null
		primary key,
	amount float null,
	created_at timestamp null,
	uuid varchar(100) null,
	verified tinyint null,
	user_id int null,
	constraint FKsg7jp0aj6qipr50856wf6vbw1
		foreign key (user_id) references user (id)
)
engine=InnoDB
;

create table premium
(
	id int not null
		primary key,
	end_at timestamp null,
	start_at timestamp null,
	transaction_id int null,
	user_id int null,
	constraint FKotoo6bvi30qyel0rvhs8oh9wo
		foreign key (transaction_id) references transaction (id),
	constraint FK55rhaw7hb4ib9usgjd0rvyubb
		foreign key (user_id) references user (id)
)
engine=InnoDB
;

create index FK55rhaw7hb4ib9usgjd0rvyubb
	on premium (user_id)
;

create index FKotoo6bvi30qyel0rvhs8oh9wo
	on premium (transaction_id)
;

create index FKsg7jp0aj6qipr50856wf6vbw1
	on transaction (user_id)
;

create table user_badge
(
	id int not null
		primary key,
	created_at timestamp null,
	badge_id int null,
	user_id int null,
	constraint FKjqx9n26pk9mqf1qo8f7xvvoq9
		foreign key (badge_id) references badge (id),
	constraint FK2jw9fpotmmbda07k27qc9t2ul
		foreign key (user_id) references user (id)
)
engine=InnoDB
;

create index FK2jw9fpotmmbda07k27qc9t2ul
	on user_badge (user_id)
;

create index FKjqx9n26pk9mqf1qo8f7xvvoq9
	on user_badge (badge_id)
;

create table user_option
(
	id int not null
		primary key,
	dark_mode tinyint null,
	map_theme tinyint default '1' null,
	private_profile tinyint default '0' null,
	language_id int null,
	user_id int null,
	constraint FKr6eermf5aq2nua7hsspmffg02
		foreign key (language_id) references language (id),
	constraint FK8usx5lqy3o113k7k4436xberw
		foreign key (user_id) references user (id)
)
engine=InnoDB
;

create index FK8usx5lqy3o113k7k4436xberw
	on user_option (user_id)
;

create index FKr6eermf5aq2nua7hsspmffg02
	on user_option (language_id)
;

create table user_quest
(
	id int not null
		primary key,
	created_at timestamp null,
	status varchar(255) null,
	updated_at timestamp null,
	quest_id int null,
	stage_id int null,
	user_id int null,
	constraint FKmlculhppmv80bbt4qjlqonn39
		foreign key (quest_id) references quest (id),
	constraint FKkmk9if2ig0f3gq6ttiskhio7c
		foreign key (stage_id) references stage (id),
	constraint FK8jdyakv4atarqd05u7axubmyk
		foreign key (user_id) references user (id)
)
engine=InnoDB
;

create index FK8jdyakv4atarqd05u7axubmyk
	on user_quest (user_id)
;

create index FKkmk9if2ig0f3gq6ttiskhio7c
	on user_quest (stage_id)
;

create index FKmlculhppmv80bbt4qjlqonn39
	on user_quest (quest_id)
;

create table user_report
(
	id int not null
		primary key,
	created_at timestamp null,
	reason varchar(20) null,
	complainant int null,
	reported int null,
	constraint FK2cm7ci55e41b6eakunxydh3lg
		foreign key (complainant) references user (id),
	constraint FKo1lnulinqcbvabo3w4ni3k4d1
		foreign key (reported) references user (id)
)
engine=InnoDB
;

create index FK2cm7ci55e41b6eakunxydh3lg
	on user_report (complainant)
;

create index FKo1lnulinqcbvabo3w4ni3k4d1
	on user_report (reported)
;

create table user_role
(
	id int not null
		primary key,
	created_at timestamp null,
	role_id int null,
	user_id int null,
	constraint FKa68196081fvovjhkek5m97n3y
		foreign key (role_id) references role (id),
	constraint FK859n2jvi8ivhui0rl0esws6o
		foreign key (user_id) references user (id)
)
engine=InnoDB
;

create index FK859n2jvi8ivhui0rl0esws6o
	on user_role (user_id)
;

create index FKa68196081fvovjhkek5m97n3y
	on user_role (role_id)
;

