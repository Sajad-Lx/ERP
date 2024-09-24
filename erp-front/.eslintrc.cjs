module.exports = {
	settings: {
		react: {
			version: "detect"
		}
	},
	root: true,
	env: { browser: true, es2020: true, node: true },
	extends: [
		"eslint:recommended",
		"plugin:@typescript-eslint/recommended",
		"plugin:react/jsx-runtime",
		"plugin:react-hooks/recommended",
		"prettier",
		"plugin:prettier/recommended"
	],
	parser: "@typescript-eslint/parser",
	parserOptions: {
		ecmaVersion: "latest",
		sourceType: "module",
		jsxPragma: "React",
		ecmaFeatures: { jsx: true }
	},
	plugins: ["react-refresh", "@typescript-eslint", "react-hooks", "prettier"],
	rules: {
		"react-refresh/only-export-components": "warn"
	}
};
