import React from "react";
import ReactDOM from "react-dom/client";
import { RouterProvider } from "react-router-dom";
import { Provider } from "react-redux";
import { store } from "@/app/store";
import { router } from "@/route/router";
import { MantineProvider } from "@mantine/core";
import { theme } from "@/theme";

ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
	<React.StrictMode>
		<Provider store={store}>
			<MantineProvider theme={theme} defaultColorScheme="dark">
				<RouterProvider router={router} />
			</MantineProvider>
		</Provider>
	</React.StrictMode>
);
