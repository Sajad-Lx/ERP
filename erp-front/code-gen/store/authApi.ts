import { emptySplitApi as api } from "./emptyApi";
const injectedRtkApi = api.injectEndpoints({
  endpoints: (build) => ({
    createUser: build.mutation<CreateUserApiResponse, CreateUserApiArg>({
      query: (queryArg) => ({
        url: `/api/v1/users/register`,
        method: "POST",
        body: queryArg.createUserRequest,
      }),
    }),
    postApiV1AuthVerify2Fa: build.mutation<
      PostApiV1AuthVerify2FaApiResponse,
      PostApiV1AuthVerify2FaApiArg
    >({
      query: (queryArg) => ({
        url: `/api/v1/auth/verify-2fa`,
        method: "POST",
        body: queryArg.twoFaVerifyRequest,
      }),
    }),
    signOut: build.mutation<SignOutApiResponse, SignOutApiArg>({
      query: () => ({ url: `/api/v1/auth/signOut`, method: "POST" }),
    }),
    signIn: build.mutation<SignInApiResponse, SignInApiArg>({
      query: (queryArg) => ({
        url: `/api/v1/auth/signIn`,
        method: "POST",
        body: queryArg.signInRequest,
      }),
    }),
    refreshAccessToken: build.mutation<
      RefreshAccessTokenApiResponse,
      RefreshAccessTokenApiArg
    >({
      query: () => ({ url: `/api/v1/auth/refresh`, method: "POST" }),
    }),
    getUserList: build.query<GetUserListApiResponse, GetUserListApiArg>({
      query: (queryArg) => ({
        url: `/api/v1/users`,
        params: { pageable: queryArg.pageable },
      }),
    }),
    updateMe: build.mutation<UpdateMeApiResponse, UpdateMeApiArg>({
      query: (queryArg) => ({
        url: `/api/v1/users`,
        method: "PATCH",
        body: queryArg.updateUserRequest,
      }),
    }),
    getUserById: build.query<GetUserByIdApiResponse, GetUserByIdApiArg>({
      query: (queryArg) => ({ url: `/api/v1/users/${queryArg.userId}` }),
    }),
    deleteUser: build.mutation<DeleteUserApiResponse, DeleteUserApiArg>({
      query: (queryArg) => ({
        url: `/api/v1/users/${queryArg.userId}`,
        method: "DELETE",
      }),
    }),
    updateUser: build.mutation<UpdateUserApiResponse, UpdateUserApiArg>({
      query: (queryArg) => ({
        url: `/api/v1/users/${queryArg.userId}`,
        method: "PATCH",
        body: queryArg.updateUserRequest,
      }),
    }),
  }),
  overrideExisting: false,
});
export { injectedRtkApi as authApi };
export type CreateUserApiResponse =
  /** status 201 Created */ CreateUserResponse;
export type CreateUserApiArg = {
  createUserRequest: CreateUserRequest;
};
export type PostApiV1AuthVerify2FaApiResponse =
  /** status 200 OK */ SignInResponse;
export type PostApiV1AuthVerify2FaApiArg = {
  twoFaVerifyRequest: TwoFaVerifyRequest;
};
export type SignOutApiResponse = /** status 200 OK */ void;
export type SignOutApiArg = void;
export type SignInApiResponse = /** status 200 OK */ SignInResponse;
export type SignInApiArg = {
  signInRequest: SignInRequest;
};
export type RefreshAccessTokenApiResponse =
  /** status 201 Create AccessToken */ RefreshAccessTokenResponse;
export type RefreshAccessTokenApiArg = void;
export type GetUserListApiResponse = /** status 200 OK */ GetUserResponse[];
export type GetUserListApiArg = {
  pageable: Pageable;
};
export type UpdateMeApiResponse = /** status 200 OK */ UpdateMeResponse;
export type UpdateMeApiArg = {
  updateUserRequest: UpdateUserRequest;
};
export type GetUserByIdApiResponse = /** status 200 OK */ GetUserResponse;
export type GetUserByIdApiArg = {
  userId: number;
};
export type DeleteUserApiResponse = /** status 204 No Content */ void;
export type DeleteUserApiArg = {
  userId: number;
};
export type UpdateUserApiResponse = /** status 200 OK */ UpdateUserResponse;
export type UpdateUserApiArg = {
  userId: number;
  updateUserRequest: UpdateUserRequest;
};
export type CreateUserResponse = {
  /** User Id */
  userId: number;
  /** User Role */
  role: "STANDARD_USER" | "STAFF_USER" | "ADMIN";
  /** User Name */
  username: string;
  /** User Email */
  email: string;
  /** First Name */
  firstName: string;
  /** Last Name */
  lastName: string;
  /** User AccessToken */
  accessToken: string;
};
export type ErrorResponse = {
  /** Error Code */
  code: number;
  /** Error Message */
  message: string;
  /** Error Item */
  errors?: object;
};
export type CreateUserRequest = {
  /** User Name */
  username: string;
  /** User Email */
  email: string;
  /** First Name */
  firstName: string;
  /** Last Name */
  lastName: string;
  /** User Password */
  password: string;
};
export type SignInResponse = {
  /** User Access Token */
  accessToken?: string | null;
  /** 2FA required or not */
  twoFactorRequired: boolean;
  /** Message */
  message: string;
  /** User Id */
  userId: number;
  /** User Role */
  role: "STANDARD_USER" | "STAFF_USER" | "ADMIN";
  /** User Name */
  username: string;
  /** User Email */
  email: string;
};
export type TwoFaVerifyRequest = {
  /** User Id */
  userId: number;
  /** 2FA code */
  twoFaCode: string;
};
export type SignInRequest = {
  /** User Email */
  email: string;
  /** User Password */
  password: string;
};
export type RefreshAccessTokenResponse = {
  /** User AccessToken */
  accessToken: string;
};
export type GetUserResponse = {
  /** User Id */
  userId: number;
  /** User Role */
  role: "STANDARD_USER" | "STAFF_USER" | "ADMIN";
  /** User Name */
  username: string;
  /** User Email */
  email: string;
  /** First Name */
  firstName: string;
  /** Last Name */
  lastName: string;
};
export type Pageable = {
  page?: number;
  size?: number;
  sort?: string[];
};
export type UpdateMeResponse = {
  /** User Id */
  userId: number;
  /** User Role */
  role: "STANDARD_USER" | "STAFF_USER" | "ADMIN";
  /** User Name */
  username: string;
  /** User Email */
  email: string;
  /** First Name */
  firstName: string;
  /** Last Name */
  lastName: string;
  /** User AccessToken */
  accessToken: string;
};
export type UpdateUserRequest = {
  /** User Name */
  username: string;
  /** First Name */
  firstName: string;
  /** Last Name */
  lastName: string;
  /** User Role */
  role: "STANDARD_USER" | "STAFF_USER" | "ADMIN";
};
export type UpdateUserResponse = {
  /** User Id */
  userId: number;
  /** User Role */
  role: "STANDARD_USER" | "STAFF_USER" | "ADMIN";
  /** User Name */
  username: string;
  /** User Email */
  email: string;
  /** First Name */
  firstName: string;
  /** Last Name */
  lastName: string;
};
export const {
  useCreateUserMutation,
  usePostApiV1AuthVerify2FaMutation,
  useSignOutMutation,
  useSignInMutation,
  useRefreshAccessTokenMutation,
  useGetUserListQuery,
  useUpdateMeMutation,
  useGetUserByIdQuery,
  useDeleteUserMutation,
  useUpdateUserMutation,
} = injectedRtkApi;
