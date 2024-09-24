import { ErrorResponse, PlainUser, RefreshAccessTokenResponse, User, UserResponse } from "@/interface";
import { PayloadAction } from "@reduxjs/toolkit";
import { createUser, refreshAccessToken, signIn, signOut, verify2Fa } from "./authApi";
import { createAppSlice } from "@/app/createAppSlice";
import { RootState } from "@/app/store";
import { getAccessToken, getUserFromStorage } from "@/repository/browserStorage";

interface AuthState {
	accessToken: string | null;
	user: PlainUser | null;
	rememberMe: boolean;
	status: "idle" | "loading" | "succeeded" | "failed";
	error: string | null;
}

const initialState: AuthState = {
	accessToken: getAccessToken(),
	user: getUserFromStorage(),
	rememberMe: true,
	status: "idle",
	error: null
};

export const authSlice = createAppSlice({
	name: "auth",
	initialState,
	reducers: {
		toggleRememberMe: (state) => {
			state.rememberMe = !state.rememberMe;
		}
	},

	extraReducers: (builder) => {
		builder
			// createUser
			.addCase(createUser.pending, (state) => {
				state.status = "loading";
			})
			.addCase(createUser.fulfilled, (state, action: PayloadAction<UserResponse>) => {
				state.status = "succeeded";
				const {accessToken, ...user } = action.payload.data
				state.user = user || null;
				state.accessToken = accessToken || null;
			})
			.addCase(createUser.rejected, (state, action: PayloadAction<ErrorResponse | undefined>) => {
				state.status = "failed";
				state.error = action.payload?.message || "Failed to create user";
			})

			// signIn
			.addCase(signIn.pending, (state) => {
				state.status = "loading";
			})
			.addCase(signIn.fulfilled, (state, action: PayloadAction<UserResponse>) => {
				state.status = "succeeded";
				state.user = action.payload.data || null;
				state.accessToken = action.payload.data.accessToken || null;
			})
			.addCase(signIn.rejected, (state, action: PayloadAction<ErrorResponse | undefined>) => {
				state.status = "failed";
				state.error = action.payload?.message || "Failed to sign in";
			})

			// verify2Fa
			.addCase(verify2Fa.pending, (state) => {
				state.status = "loading";
			})
			.addCase(verify2Fa.fulfilled, (state, action: PayloadAction<UserResponse>) => {
				state.status = "succeeded";
				state.user = action.payload.data || null;
				state.accessToken = action.payload.data.accessToken || null;
			})
			.addCase(verify2Fa.rejected, (state, action: PayloadAction<ErrorResponse | undefined>) => {
				state.status = "failed";
				state.error = action.payload?.message || "Failed to verify 2FA";
			})

			// signOut
			.addCase(signOut.pending, (state) => {
				state.status = "loading";
			})
			.addCase(signOut.fulfilled, (state) => {
				state.user = null;
				state.accessToken = null;
				state.status = "idle";
			})
			.addCase(signOut.rejected, (state, action: PayloadAction<ErrorResponse | undefined>) => {
				state.status = "failed";
				state.error = action.payload?.message || "Failed to sign out";
			})

			// refreshAccessToken
			.addCase(refreshAccessToken.pending, (state) => {
				state.status = "loading";
			})
			.addCase(
				refreshAccessToken.fulfilled,
				(state, action: PayloadAction<RefreshAccessTokenResponse>) => {
					state.status = "succeeded";
					state.accessToken = action.payload.data.accessToken || null;
				}
			)
			.addCase(
				refreshAccessToken.rejected,
				(state, action: PayloadAction<ErrorResponse | undefined>) => {
					state.status = "failed";
					state.error = action.payload?.message || "Failed to refresh access token";
				}
			);
	}
});

export const { toggleRememberMe } = authSlice.actions;

export default authSlice.reducer;

export const selectRememberMe = (state: RootState) => state.auth.rememberMe;
export const selectUser = (state: RootState) => state.auth.user;
export const selectAccessToken = (state: RootState) => state.auth.accessToken;
