export interface ThemeConfigProp {
  isDark: boolean;
}

export interface GlobalState {
  token: string;
  userInfo: any;
  language: string;
  themeConfig: ThemeConfigProp;
}
