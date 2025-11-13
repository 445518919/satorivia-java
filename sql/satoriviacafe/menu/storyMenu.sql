-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('品牌故事', '1100', '1', 'story', 'cafe/story/index', 1, 0, 'C', '0', '0', 'cafe:story:list', '#', 'admin', sysdate(), '', null, '品牌故事菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('品牌故事查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'cafe:story:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('品牌故事新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'cafe:story:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('品牌故事修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'cafe:story:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('品牌故事删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'cafe:story:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('品牌故事导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'cafe:story:export',       '#', 'admin', sysdate(), '', null, '');
