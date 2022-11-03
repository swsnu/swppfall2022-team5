module.exports = {
    "presets": [['@babel/preset-env', { targets: { node: 'current' } }],
        '@babel/preset-typescript',
        "next/babel",
    ],
    "plugins": [
        "@babel/proposal-class-properties",
        "@babel/proposal-object-rest-spread",
    ]
};