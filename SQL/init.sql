delete from wallet_asset;
insert into wallet_asset (
	id, symbol, symbol_name, is_use, address, abc_address, block_height
) values (
	'1', 'DOCFP', '数字指纹', true, '123', '123', 670014
);


delete from wallet_manager;
insert into wallet_manager (
	manager_name, manager_pwd, realname, menus, inuse, error_times
) values(
	'manager', '0795151DEFBA7A4B5DFA89170DE46277', '管理员', '101,101-101,101-102,102,102-101,102-102,102-103', true, 0
);

delete from wallet_menu;
insert into wallet_menu (
	id, parent_id, menu_name, menu_href, serno
) values(
	'101', null, '信息管理', null, 1
);
insert into wallet_menu (
	id, parent_id, menu_name, menu_href, serno
) values(
	'101-101', '101', '管理员信息', 'manager/manager', 1
);
insert into wallet_menu (
	id, parent_id, menu_name, menu_href, serno
) values(
	'101-102', '101', '资产信息', 'manager/asset', 2
);

insert into wallet_menu (
	id, parent_id, menu_name, menu_href, serno
) values(
	'102', null, '钱包管理', null, 2
);
insert into wallet_menu (
	id, parent_id, menu_name, menu_href, serno
) values(
	'102-101', '102', '充币数据', 'manager/transaction', 1
);
insert into wallet_menu (
	id, parent_id, menu_name, menu_href, serno
) values(
	'102-102', '102', '提币数据', 'manager/withdraw', 2
);
insert into wallet_menu (
	id, parent_id, menu_name, menu_href, serno
) values(
	'102-103', '102', '发送', 'manager/withdraw/send', 3
);



