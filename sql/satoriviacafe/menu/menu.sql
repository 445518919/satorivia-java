INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (1100, 'CAFE管理', 0, 1, 'cafe', NULL, '', '', 1, 0, 'M', '0', '0', '', 'build', 'admin', '2025-11-13 16:54:58',
        'admin', '2025-11-13 20:37:15', '系统管理目录');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2018, '产品', 1100, 1, 'production', 'cafe/production/index', NULL, '', 1, 0, 'C', '0', '0',
        'cafe:production:list', 'shopping', 'admin', '2025-11-13 20:20:59', 'admin', '2025-11-13 20:38:09', '产品菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2019, '产品查询', 2018, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'cafe:production:query', '#', 'admin',
        '2025-11-13 20:20:59', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2020, '产品新增', 2018, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'cafe:production:add', '#', 'admin',
        '2025-11-13 20:20:59', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2021, '产品修改', 2018, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'cafe:production:edit', '#', 'admin',
        '2025-11-13 20:20:59', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2022, '产品删除', 2018, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'cafe:production:remove', '#', 'admin',
        '2025-11-13 20:20:59', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2023, '产品导出', 2018, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'cafe:production:export', '#', 'admin',
        '2025-11-13 20:20:59', '', NULL, '');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2024, '品牌故事', 1100, 1, 'story', 'cafe/story/index', NULL, '', 1, 0, 'C', '0', '0', 'cafe:story:list',
        'edit', 'admin', '2025-11-13 20:21:05', 'admin', '2025-11-13 20:38:17', '品牌故事菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2025, '品牌故事查询', 2024, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'cafe:story:query', '#', 'admin',
        '2025-11-13 20:21:05', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2026, '品牌故事新增', 2024, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'cafe:story:add', '#', 'admin',
        '2025-11-13 20:21:05', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2027, '品牌故事修改', 2024, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'cafe:story:edit', '#', 'admin',
        '2025-11-13 20:21:05', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2028, '品牌故事删除', 2024, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'cafe:story:remove', '#', 'admin',
        '2025-11-13 20:21:05', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2029, '品牌故事导出', 2024, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'cafe:story:export', '#', 'admin',
        '2025-11-13 20:21:05', '', NULL, '');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2031, '咖啡产品访问跟踪日志', 1100, 1, 'track_log', 'cafe/track_log/index', NULL, '', 1, 0, 'C', '0', '0',
        'cafe:track_log:list', '#', 'admin', '2026-04-29 07:07:28', '', NULL, '咖啡产品访问跟踪日志菜单');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2032, '咖啡产品访问跟踪日志查询', 2031, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'cafe:track_log:query', '#',
        'admin', '2026-04-29 07:07:28', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2033, '咖啡产品访问跟踪日志新增', 2031, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'cafe:track_log:add', '#',
        'admin', '2026-04-29 07:07:28', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2034, '咖啡产品访问跟踪日志修改', 2031, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'cafe:track_log:edit', '#',
        'admin', '2026-04-29 07:07:28', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2035, '咖啡产品访问跟踪日志删除', 2031, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'cafe:track_log:remove', '#',
        'admin', '2026-04-29 07:07:28', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`,
                        `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2036, '咖啡产品访问跟踪日志导出', 2031, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'cafe:track_log:export', '#',
        'admin', '2026-04-29 07:07:28', '', NULL, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status,
                      perms, icon, create_by, create_time, update_by, update_time, remark)
values ('轮播图', '1100', '1', 'banner', 'cafe/banner/index', 1, 0, 'C', '0', '0', 'cafe:banner:list', '#', 'admin',
        sysdate(), '', null, '轮播图菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status,
                      perms, icon, create_by, create_time, update_by, update_time, remark)
values ('轮播图查询', @parentId, '1', '#', '', 1, 0, 'F', '0', '0', 'cafe:banner:query', '#', 'admin', sysdate(), '',
        null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status,
                      perms, icon, create_by, create_time, update_by, update_time, remark)
values ('轮播图新增', @parentId, '2', '#', '', 1, 0, 'F', '0', '0', 'cafe:banner:add', '#', 'admin', sysdate(), '',
        null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status,
                      perms, icon, create_by, create_time, update_by, update_time, remark)
values ('轮播图修改', @parentId, '3', '#', '', 1, 0, 'F', '0', '0', 'cafe:banner:edit', '#', 'admin', sysdate(), '',
        null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status,
                      perms, icon, create_by, create_time, update_by, update_time, remark)
values ('轮播图删除', @parentId, '4', '#', '', 1, 0, 'F', '0', '0', 'cafe:banner:remove', '#', 'admin', sysdate(), '',
        null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status,
                      perms, icon, create_by, create_time, update_by, update_time, remark)
values ('轮播图导出', @parentId, '5', '#', '', 1, 0, 'F', '0', '0', 'cafe:banner:export', '#', 'admin', sysdate(), '',
        null, '');


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status,
                      perms, icon, create_by, create_time, update_by, update_time, remark)
values ('豆子笔记', '1100', '1', 'note', 'cafe/note/index', 1, 0, 'C', '0', '0', 'cafe:note:list', '#', 'admin',
        sysdate(), '', null, '豆子笔记菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status,
                      perms, icon, create_by, create_time, update_by, update_time, remark)
values ('豆子笔记查询', @parentId, '1', '#', '', 1, 0, 'F', '0', '0', 'cafe:note:query', '#', 'admin', sysdate(), '',
        null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status,
                      perms, icon, create_by, create_time, update_by, update_time, remark)
values ('豆子笔记新增', @parentId, '2', '#', '', 1, 0, 'F', '0', '0', 'cafe:note:add', '#', 'admin', sysdate(), '',
        null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status,
                      perms, icon, create_by, create_time, update_by, update_time, remark)
values ('豆子笔记修改', @parentId, '3', '#', '', 1, 0, 'F', '0', '0', 'cafe:note:edit', '#', 'admin', sysdate(), '',
        null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status,
                      perms, icon, create_by, create_time, update_by, update_time, remark)
values ('豆子笔记删除', @parentId, '4', '#', '', 1, 0, 'F', '0', '0', 'cafe:note:remove', '#', 'admin', sysdate(), '',
        null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status,
                      perms, icon, create_by, create_time, update_by, update_time, remark)
values ('豆子笔记导出', @parentId, '5', '#', '', 1, 0, 'F', '0', '0', 'cafe:note:export', '#', 'admin', sysdate(), '',
        null, '');
