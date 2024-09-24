import { apiKey, baseUrl } from "@/constants";
import {
	CreateUserRequest,
	ErrorResponse,
	RefreshAccessTokenResponse,
	SignInRequest,
	TwoFaVerifyRequest,
	User,
	UserResponse
} from "@/interface";
import { createAppAsyncThunk } from "@/app/withTypes";
import axios from "axios";
import {
	clearStorage,
	saveUserToLocalStorage,
	saveUserToSessionStorage,
	setAccessTokenLocal,
	setAccessTokenSession
} from "@/repository/browserStorage";

const headers = {
	"Content-Type": "application/json",
	"X-API-KEY": apiKey
};

export const saveToStorage = (data: User, isLocalStorage: Boolean) => {
	const { accessToken, twoFactorRequired, ...plainUser } = data;
	if (isLocalStorage) {
		saveUserToLocalStorage(plainUser);
		setAccessTokenLocal(accessToken);
	} else {
		saveUserToSessionStorage(plainUser);
		setAccessTokenSession(accessToken);
	}
};

export const createUser = createAppAsyncThunk<
	UserResponse,
	CreateUserRequest,
	{ rejectValue: ErrorResponse }
>("auth/createUser", async (createUserData: CreateUserRequest, { rejectWithValue, getState }) => {
	try {
		const response = await axios.post(`${baseUrl}/api/v1/users/register`, createUserData, {
			headers
		});

		if (getState().auth.rememberMe) {
			saveToStorage(response.data.data, true);
		} else {
			saveToStorage(response.data.data, false);
		}

		return response.data;
	} catch (error: any) {
		if (error.response) {
			const errorResponse: ErrorResponse = {
				code: error.response.status,
				message: error.response.data.message,
				errors: error.response.data.errors || null
			};
			return rejectWithValue(errorResponse); // Reject with the ErrorResponse
		}
		// If there's no specific response from the API, throw a general error
		throw new Error("Network error or unknown error occurred");
	}
});

export const signIn = createAppAsyncThunk<
	UserResponse,
	SignInRequest,
	{ rejectValue: ErrorResponse }
>("auth/signIn", async (signInData: SignInRequest, { rejectWithValue, getState }) => {
	try {
		const response = await axios.post<UserResponse>(`${baseUrl}/api/v1/auth/signIn`, signInData, {
			headers
		});

		if (getState().auth.rememberMe) {
			saveToStorage(response.data.data, true);
		} else {
			saveToStorage(response.data.data, false);
		}

		return response.data;
	} catch (error: any) {
		if (error.response) {
			const errorResponse: ErrorResponse = {
				code: error.response.status,
				message: error.response.data.message,
				errors: error.response.data.errors || null
			};
			return rejectWithValue(errorResponse); // Reject with the ErrorResponse
		}
		// If there's no specific response from the API, throw a general error
		throw new Error("Network error or unknown error occurred");
	}
});

export const verify2Fa = createAppAsyncThunk<
	UserResponse,
	TwoFaVerifyRequest,
	{ rejectValue: ErrorResponse }
>("auth/verify2Fa", async (twoFaData: TwoFaVerifyRequest, { rejectWithValue, getState }) => {
	try {
		const accessToken = getState().auth.accessToken;
		const customHeader = {
			...headers,
			...(accessToken && { Authorization: `Bearer ${accessToken}` })
		};

		const response = await axios.post<UserResponse>(
			`${baseUrl}/api/v1/auth/verify-2fa`,
			twoFaData,
			{ headers: customHeader }
		);

		if (getState().auth.rememberMe) {
			saveToStorage(response.data.data, true);
		} else {
			saveToStorage(response.data.data, false);
		}

		return response.data;
	} catch (error: any) {
		if (error.response) {
			const errorResponse: ErrorResponse = {
				code: error.response.status,
				message: error.response.data.message,
				errors: error.response.data.errors || null
			};
			return rejectWithValue(errorResponse);
		}
		throw new Error("Network error or unknown error occurred");
	}
});

export const signOut = createAppAsyncThunk<void, void, { rejectValue: ErrorResponse }>(
	"auth/signOut",
	async (_, { rejectWithValue, getState }) => {
		try {
			const accessToken = getState().auth.accessToken;
			const customHeader = {
				...headers,
				...(accessToken && { Authorization: `Bearer ${accessToken}` })
			};

			const response = await axios.post<void>(`${baseUrl}/api/v1/auth/signOut`, null, {
				headers: customHeader
			});

			clearStorage();

			return response.data;
		} catch (error: any) {
			if (error.response) {
				const errorResponse: ErrorResponse = {
					code: error.response.status,
					message: error.response.data.message,
					errors: error.response.data.errors || null
				};
				return rejectWithValue(errorResponse);
			}
			throw new Error("Network error or unknown error occurred");
		}
	}
);

export const refreshAccessToken = createAppAsyncThunk<
	RefreshAccessTokenResponse,
	void,
	{ rejectValue: ErrorResponse }
>("auth/refreshAccessToken", async (_, { rejectWithValue, getState }) => {
	try {
		const accessToken = getState().auth.accessToken;
		const customHeader = {
			...headers,
			...(accessToken && { Authorization: `Bearer ${accessToken}` })
		};

		const response = await axios.post<RefreshAccessTokenResponse>(
			`${baseUrl}/api/v1/auth/refresh`,
			null,
			{
				headers: customHeader
			}
		);

		if (getState().auth.rememberMe) {
			setAccessTokenLocal(response.data.data.accessToken);
		} else {
			setAccessTokenSession(response.data.data.accessToken);
		}

		return response.data;
	} catch (error: any) {
		if (error.response) {
			const errorResponse: ErrorResponse = {
				code: error.response.status,
				message: error.response.data.message,
				errors: error.response.data.errors || null
			};
			return rejectWithValue(errorResponse);
		}
		throw new Error("Network error or unknown error occurred");
	}
});
