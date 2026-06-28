-- ============================================================
-- 初始化：两大产品线
-- ============================================================
INSERT INTO cafe_product_line
(line_id, line_code, series_code, line_name, line_sub_title, line_description,
 cover_image, mobile_cover_image, line_sort, line_status)
VALUES (1, 'beans', 'asteroids', '咖啡豆系列', 'WHOLE BEAN COFFEE',
        '适合手冲、意式与日常风味探索，层次清晰，香气完整。',
        '/product-lines/beans-series-cover.webp', '/product-lines/beans-series-cover.webp', 1, '0'),
       (2, 'drip', 'drip', '挂耳系列', 'DRIP BAG COFFEE',
        '适合办公室、旅行与轻松冲煮，快速稳定，随时享受。',
        '/product-lines/drip-series-cover.webp', '/product-lines/drip-series-cover.webp', 2, '0');

-- ============================================================
-- 初始化：当前六款商品
-- purchase_url 可继续使用现有淘宝链接，或由后台商品管理重新填写
-- ============================================================
INSERT INTO cafe_product
(product_id, line_id, product_slug, product_name, product_short_name, flavor,
 origin, altitude, variety, treatment, brewing_method, dosage, extraction_time,
 water_temp, powder_water_ratio, specification, package_desc, additives,
 primary_image, radar_image, is_featured, product_sort, product_status)
VALUES (1, 1, 'blue-mountain-no1', '牙买加克里夫顿庄园 蓝山一号', '蓝山一号',
        '红茶 · 焦糖 · 坚果', '牙买加克里夫顿庄园', '1900米', '铁皮卡', '水洗处理',
        '手冲 / 法压壶 / 滴滤壶 / 冰滴等', '10~15g', '120~150 秒', '90~92℃', '1:15',
        '227g / 454g', '精致袋装，防潮锁鲜', '无添加、无调味、100%原豆原香',
        '/beans/Asteroids/gallery/blue-mountain-no1/01.webp',
        '/beans/Asteroids/radar/radar-blue-mountain-no1.png', '1', 1, '0'),

       (2, 1, 'blue-flamenco', '篮韵弗朗明戈', '篮韵弗朗明戈',
        '茉莉玉兰 · 蜜柑 · 木瓜 · 椰果', '高海拔果香带', '1500–2100米',
        '未特别标注（以风味蓝韵拼配为主）', '精致水洗发酵处理，保留清新与优雅甜感',
        '手冲 / 滴滤壶 / 冰滴 / 咖啡机', '10~15g', '90~120 秒', '90~94℃', '1:15',
        '227g / 454g', '密封防潮袋装，适用于家庭或专业咖啡空间', '100% 咖啡豆，无添加',
        '/beans/Asteroids/gallery/blue-flamenco/01.webp',
        '/beans/Asteroids/radar/radar-blue-flamenco.png', '1', 2, '0'),

       (3, 1, 'soe-geisha-g1', 'SOE 埃塞格拉纳瑰夏 G1', '埃塞格拉纳瑰夏 G1',
        '玫瑰 · 黑莓 · 牛轧糖', '埃塞俄比亚 · 瑰夏', '1900–2100米', '瑰夏', '日晒处理',
        '意式浓缩 / 手冲 / 滴滤壶 / 冰滴', '10~15g', '90~120 秒', '90~94℃', '1:16',
        '227g / 454g', '密封袋装，锁香防潮，适合家庭及精品咖啡馆使用',
        '无人工香精，无任何添加',
        '/beans/Asteroids/gallery/soe-geisha-g1/01.webp',
        '/beans/Asteroids/radar/radar-soe-geisha-g1.png', '1', 3, '0'),

       (4, 2, 'mandheling', 'INTUI CAFE｜黄金曼特宁（苏门答腊 · PWN）挂耳咖啡', '黄金曼特宁',
        '黄油 · 焦糖 · 檀木 · 巧克力（隐约烟熏甜感）', '印度尼西亚 · 苏门答腊 PWN',
        '1350–1500米', '铁皮卡', '湿刨', '单杯热萃 / 冰手冲 / 冰美式 / 拿铁推荐',
        '一包', '120~150 秒', '92℃', '1:15', '80g（10g × 8包）',
        '自饮日常 × 走心送礼，两相宜', '不含任何添加物',
        '/drip/gallery/mandheling/01.webp', '/drip/radar/radar-mandheling.png', '1', 1, '0'),

       (5, 2, 'moonlight', 'INTUI CAFE｜花月夜 · 滤挂式咖啡', '花月夜',
        '菠萝 · 芒果 · 荔枝 · 哈密瓜 · 茉莉', '哥伦比亚 · 惠兰', '1800米', 'F1', '氧晒',
        '标准热萃 / 冰手冲 / 冷萃美式', '一包', '120~150 秒', '90~92℃', '1:15',
        '80g（10g × 8包）', '星月紫金设计，自饮精致、送礼体面', '不含任何添加物',
        '/drip/gallery/moonlight/01.webp', '/drip/radar/radar-moonlight.png', '1', 2, '0'),

       (6, 2, 'baha', 'INTUI CAFE｜巴哈 · 加勒比海 F1 挂耳式咖啡', '巴哈',
        '葡萄 · 莓果 · 白兰地（橡木与酒香感）', '加勒比海 Canet 庄园', '1900米', 'F1',
        '葡萄干蜜处理 · 朗姆酒桶发酵收尾', '标准热萃 / 冰手冲 / 巴哈冰美式 + 一片青柠',
        '一包', '120~150 秒', '90~92℃', '1:16 - 1:20', '80g（10g × 8包）',
        '自饮精致，送礼体面', '不含任何添加物',
        '/drip/gallery/baha/01.webp', '/drip/radar/radar-baha.png', '1', 3, '0');

-- 商品图库。前端按 image_sort 升序组装 gallery 数组。
INSERT INTO cafe_product_image
    (product_id, image_url, image_alt, image_type, image_sort)
VALUES (1, '/beans/Asteroids/gallery/blue-mountain-no1/01.webp', '蓝山一号产品图1', 'gallery', 1),
       (1, '/beans/Asteroids/gallery/blue-mountain-no1/02.webp', '蓝山一号产品图2', 'gallery', 2),
       (1, '/beans/Asteroids/gallery/blue-mountain-no1/03.webp', '蓝山一号产品图3', 'gallery', 3),
       (1, '/beans/Asteroids/gallery/blue-mountain-no1/04.webp', '蓝山一号产品图4', 'gallery', 4),
       (2, '/beans/Asteroids/gallery/blue-flamenco/01.webp', '篮韵弗朗明戈产品图1', 'gallery', 1),
       (2, '/beans/Asteroids/gallery/blue-flamenco/02.webp', '篮韵弗朗明戈产品图2', 'gallery', 2),
       (2, '/beans/Asteroids/gallery/blue-flamenco/03.webp', '篮韵弗朗明戈产品图3', 'gallery', 3),
       (2, '/beans/Asteroids/gallery/blue-flamenco/04.webp', '篮韵弗朗明戈产品图4', 'gallery', 4),
       (3, '/beans/Asteroids/gallery/soe-geisha-g1/01.webp', '埃塞格拉纳瑰夏产品图1', 'gallery', 1),
       (3, '/beans/Asteroids/gallery/soe-geisha-g1/02.webp', '埃塞格拉纳瑰夏产品图2', 'gallery', 2),
       (3, '/beans/Asteroids/gallery/soe-geisha-g1/03.webp', '埃塞格拉纳瑰夏产品图3', 'gallery', 3),
       (3, '/beans/Asteroids/gallery/soe-geisha-g1/04.webp', '埃塞格拉纳瑰夏产品图4', 'gallery', 4),
       (4, '/drip/gallery/mandheling/01.webp', '黄金曼特宁产品图1', 'gallery', 1),
       (4, '/drip/gallery/mandheling/02.webp', '黄金曼特宁产品图2', 'gallery', 2),
       (4, '/drip/gallery/mandheling/03.webp', '黄金曼特宁产品图3', 'gallery', 3),
       (4, '/drip/gallery/mandheling/04.webp', '黄金曼特宁产品图4', 'gallery', 4),
       (5, '/drip/gallery/moonlight/01.webp', '花月夜产品图1', 'gallery', 1),
       (5, '/drip/gallery/moonlight/02.webp', '花月夜产品图2', 'gallery', 2),
       (5, '/drip/gallery/moonlight/03.webp', '花月夜产品图3', 'gallery', 3),
       (5, '/drip/gallery/moonlight/04.webp', '花月夜产品图4', 'gallery', 4),
       (6, '/drip/gallery/baha/01.webp', '巴哈产品图1', 'gallery', 1),
       (6, '/drip/gallery/baha/02.webp', '巴哈产品图2', 'gallery', 2),
       (6, '/drip/gallery/baha/03.webp', '巴哈产品图3', 'gallery', 3),
       (6, '/drip/gallery/baha/04.webp', '巴哈产品图4', 'gallery', 4);

-- 初始规格；价格和SKU由后端商品管理补充。
INSERT INTO cafe_product_variant
    (product_id, variant_name, net_content, variant_sort)
VALUES (1, '227g', '227g', 1),
       (1, '454g', '454g', 2),
       (2, '227g', '227g', 1),
       (2, '454g', '454g', 2),
       (3, '227g', '227g', 1),
       (3, '454g', '454g', 2),
       (4, '8包装', '80g（10g × 8包）', 1),
       (5, '8包装', '80g（10g × 8包）', 1),
       (6, '8包装', '80g（10g × 8包）', 1);

-- 当前详情页的一组默认冲煮参数。
INSERT INTO cafe_product_brew
(product_id, brew_name, brew_method, powder_quantity, water_temp,
 powder_water_ratio, extraction_time, brew_sort)
VALUES (1, '推荐参数', '手冲 / 法压壶 / 滴滤壶 / 冰滴等', '10~15g', '90~92℃', '1:15', '120~150 秒', 1),
       (2, '推荐参数', '手冲 / 滴滤壶 / 冰滴 / 咖啡机', '10~15g', '90~94℃', '1:15', '90~120 秒', 1),
       (3, '推荐参数', '意式浓缩 / 手冲 / 滴滤壶 / 冰滴', '10~15g', '90~94℃', '1:16', '90~120 秒', 1),
       (4, '推荐参数', '单杯热萃 / 冰手冲 / 冰美式 / 拿铁推荐', '一包', '92℃', '1:15', '120~150 秒', 1),
       (5, '推荐参数', '标准热萃 / 冰手冲 / 冷萃美式', '一包', '90~92℃', '1:15', '120~150 秒', 1),
       (6, '推荐参数', '标准热萃 / 冰手冲 / 巴哈冰美式 + 一片青柠', '一包', '90~92℃', '1:16 - 1:20', '120~150 秒', 1);

