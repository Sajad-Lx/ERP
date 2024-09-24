import { createBrowserRouter, Navigate, Outlet, RouteObject } from "react-router-dom";
import App from "@/App";
import ErrorPage from "@/pages/error-page";
import { AuthPage } from "@/features/auth/AuthPage";
import { useAppSelector } from "@/app/hooks";
import { selectAccessToken } from "@/features/auth/authSlice";
import menuItems, { MenuItem } from "@/app/navbarItems";

const ProtectedRoutes = () => {
	// const accessToken = useAppSelector(selectAccessToken);
	// return accessToken ? <Outlet /> : <Navigate to={"/auth"} />;
	return <Outlet />;
};

const createRoutesFromMenu = (menuItems: MenuItem[]): RouteObject[] => {
	return menuItems.map((menu) => {
		return {
			path: menu.to,
			element: (
				<div>
					<div
						style={{
							display: "flex",
							justifyContent: "center",
							alignItems: "center",
							height: "90vh",
							width: "90vh"
						}}
					>
						{menu.label}
						<Outlet />
					</div>
				</div>
			),
			children: menu.subMenu
				? menu.subMenu.map((sub) => ({
						path: sub.to,
						element: (
							<div
								style={{
									display: "flex",
									justifyContent: "center",
									alignItems: "center",
									height: "90vh",
									width: "90vh"
								}}
							>
								{sub.label}
							</div>
						)
				  }))
				: []
		};
	});
};

export const router = createBrowserRouter([
	{
		path: "/auth",
		element: <AuthPage />
	},
	{
		path: "/",
		element: <ProtectedRoutes />,
		children: [{ path: "", element: <App />, children: [...createRoutesFromMenu(menuItems)] }],
		errorElement: <ErrorPage />
	},
	{
		path: "*",
		element: <Navigate to={"/"} />
	}
]);
