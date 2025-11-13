module.exports = {
    presets: [
        // https://github.com/vuejs/vue-cli/tree/master/packages/@vue/babel-preset-app
        ['@vue/cli-plugin-babel/preset', {
            useBuiltIns: false  // 关闭 preset-env 自己做的 polyfill
        }]
    ],
    'env': {
        'development': {
            // babel-plugin-dynamic-import-node plugin only does one thing by converting all import() to require().
            // This plugin can significantly increase the speed of hot updates, when you have a large number of pages.
            'plugins': ['dynamic-import-node']
        }
    },
    plugins: [
        // 用 runtime 替换 helper、polyfill、regenerator
        ['@babel/plugin-transform-runtime', {
            corejs: 3,           // 指定用 core-js@3
            helpers: true,       // 抽取 helper 函数到 @babel/runtime-corejs3
            regenerator: true,   // 启用 generator/async 转换
            useESModules: true   // 对 ESM 代码使用 import
        }]
    ]
}
