/**
	create database iexchg_wallet character set utf8 collate utf8_general_ci;
	create user 'iexchg_wallet'@'%' identified by 'iexchg_wallet123';
	grant all on iexchg_wallet.* to iexchg_wallet;
*/

drop table if exists wallet_transaction;
create table wallet_transaction(
	id varchar(32) primary key comment 'ID',
	txid varchar(64) not null comment '交易ID',
	category varchar(32) not null comment '类型',
	symbol varchar(32) not null comment '资产符号',
	account varchar(32) comment '账户',
	address varchar(64) not null comment '地址',
	block_height bigint not null comment '区块高度',
	tx_fee decimal(20, 8) not null default 0 comment '手续费',
	amount decimal(20, 8) not null comment '金额',
	confirms integer not null default 0 comment '确认数',
	time_received bigint not null comment '接收时间',
	is_upload tinyint not null default false comment '是否上传',
	upload_message varchar(512) comment '上传返回信息',
	is_confirm tinyint not null default false comment '是否确认',
	confirm_message varchar(512) comment '确认返回信息',
	is_processed tinyint not null default false comment '是否处理完成',
	process_time bigint comment '处理时间'
) engine=InnoDB default charset=utf8;


drop table if exists wallet_withdraw_data;
create table wallet_withdraw_data(
	id varchar(32) primary key comment 'ID',
	symbol varchar(32) not null comment '资产符号',
	amount decimal(20, 8) not null comment '发送金额',
	address varchar(64) not null comment '发送地址',
	serno varchar(32) not null comment '业务流水号（唯一）',
	is_success tinyint not null default false comment '发送是否成功',
	txid varchar(64) comment '交易ID',
	message varchar(512) comment '发送返回信息',
	is_send tinyint not null default false comment '是否发送',
	is_upload tinyint not null default false comment '是否上传',
	upload_message varchar(512) comment '上传返回信息',
	is_processed tinyint not null default false comment '是否处理完成',
	send_times integer not null default 0 comment '钱包发送次数',
	process_time bigint comment '处理时间'
) engine=InnoDB default charset=utf8;


drop table if exists wallet_asset;
create table wallet_asset(
	id varchar(32) primary key comment 'ID',
	symbol varchar(32) not null comment '资产符号',
	symbol_name varchar(32) comment '资产名称',
	is_use tinyint not null default true comment '是否使用',
	tx_fee decimal(16, 8) comment '交易手续费',
	asset_type integer comment '接口类型',
	address varchar(64) not null comment '资产发送地址',
	abc_address varchar(64) not null comment '手续费地址',
	block_height bigint not null default 1 comment '区块高度',
	cfos_charge_server varchar(128) comment '充币服务器地址',
	cfos_charge_user varchar(64) comment '充币服务器用户名',
	cfos_charge_pwd varchar(64) comment '充币服务器密码',
	cfos_withdraw_server varchar(128) comment '提币服务器地址',
	cfos_withdraw_user varchar(64) comment '提币服务器用户名',
	cfos_withdraw_pwd varchar(64) comment '提币服务器密码',
	is_change tinyint not null default false comment '是否自动转移',
	change_address varchar(64) comment '转移地址',
	change_fee_address varchar(64) comment '自动转移币手续费地址'
) engine=InnoDB default charset=utf8;

drop table if exists wallet_setting;
create table wallet_setting(
	setting_key varchar(32) primary key comment '配置项',
	setting_value varchar(128) comment '配置值',
	setting_desc varchar(256) comment '配置描述'
) engine=InnoDB default charset=utf8;


drop table if exists wallet_manager;
create table wallet_manager(
	manager_name varchar(32) primary key comment '用户名',
	manager_pwd varchar(32) not null comment '密码',
	realname varchar(32)  comment '姓名',
	menus varchar(1024)  comment '菜单',
	inuse tinyint not null default false comment '使用状态',
	error_times integer not null default 0 comment '登录错误次数',
	last_login_time timestamp  comment '最近一次登录时间',
	last_logout_time timestamp  comment '最近一次退出时间'
) engine=InnoDB default charset=utf8;

drop table if exists wallet_menu;
create table wallet_menu(
	id varchar(32) primary key comment '菜单ID',
	parent_id varchar(32)  comment '上级菜单ID',
	menu_name varchar(32)  comment '菜单名称',
	menu_href varchar(256)  comment '菜单链接',
	serno integer not null default 1 comment '排序号'
) engine=InnoDB default charset=utf8;

drop table if exists wallet_log_send_asset;
create table wallet_log_send_asset(
	id varchar(32) primary key comment 'ID',
	symbol varchar(32) not null comment '资产符号',
	from_address varchar(64) not null  comment '发送地址',
	to_address varchar(64) not null  comment '发送到的地址',
	amount decimal(20,8) not null comment '发送金额',
	txid varchar(64)  comment '交易ID',
	send_time timestamp  comment '发送时间',
	send_note varchar(512)  comment '发送备注'
) engine=InnoDB default charset=utf8;



