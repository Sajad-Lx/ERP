import { PlainUser } from "@/interface";

export const saveUserToLocalStorage = (user: PlainUser) => {
	localStorage.setItem("user", JSON.stringify(user));
};

export const saveUserToSessionStorage = (user: PlainUser) => {
	sessionStorage.setItem("user", JSON.stringify(user));
};

export const getUserFromStorage = () => {
	const userString = localStorage.getItem("user") || sessionStorage.getItem("user");
	let user: PlainUser | null = null;
	if (userString) {
		try {
			user = JSON.parse(userString) as PlainUser; // Parse the string and cast it to the User type
		} catch (error) {
			console.error("Error parsing user data", error);
		}
	}
	return user;
};

export const clearStorage = () => {
	localStorage.clear();
	sessionStorage.clear();
};

export const setAccessTokenSession = (accessToken: string) => {
	sessionStorage.setItem("accessToken", accessToken);
};

export const setAccessTokenLocal = (accessToken: string) => {
	localStorage.setItem("accessToken", accessToken);
};

export const getAccessToken = () => {
	return localStorage.getItem("accessToken") || sessionStorage.getItem("accessToken");
};
