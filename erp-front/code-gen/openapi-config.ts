import type { ConfigFile } from "@rtk-query/codegen-openapi";

const config: ConfigFile = {
  schemaFile: "http://localhost:8080/api-docs/V1%20API",
  apiFile: "./store/emptyApi.ts",
  apiImport: "emptySplitApi",
  outputFile: "./store/authApi.ts",
  exportName: "authApi",
  hooks: true,
};

export default config;
