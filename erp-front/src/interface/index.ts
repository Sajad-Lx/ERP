export interface ThemeConfigProp {
	isDark: boolean;
}

export interface GlobalState {
	token: string | null;
	user: any | null;
	language: string;
	themeConfig: ThemeConfigProp;
}

export interface Role {
	role: "STANDARD_USER" | "STAFF_USER" | "ADMIN";
}

export interface PlainUser {
	userId: BigInteger;
	email: string;
	role: Role;
	username: string;
	firstName?: string;
	lastName?: string;
}

export interface User {
	accessToken: string;
	twoFactorRequired?: boolean;

	userId: BigInteger;
	email: string;
	role: Role;
	username: string;
	firstName?: string;
	lastName?: string;
}

export interface UserResponse {
	code: number;
	message: string;
	data: User;
}

export interface SignInRequest {
	email: string;
	password: string;
}

export interface TwoFaVerifyRequest {
	userId: number;
	twoFaCode: string;
}

export interface RefreshAccessTokenResponse {
	code: number;
	message: string;
	data: {
		accessToken: string;
	};
}

export interface CreateUserRequest {
	username: string;
	email: string;
	firstName: string;
	lastName: string;
	password: string;
}

export interface UpdateUserRequest {
	username: string;
	firstName: string;
	lastName: string;
	role: Role;
}

export interface Pageable {
	page: number;
	size: number;
	sort: Array<string>;
}

export interface ErrorResponse {
	code: number;
	message: string;
	errors: string | null;
}
