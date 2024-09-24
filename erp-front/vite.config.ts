import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import path from "path";

const port = parseInt(process.env.VITE_PORT || '8000', 10);

// https://vitejs.dev/config/
export default defineConfig({
	plugins: [react()],
	preview: {
		port: port,
		strictPort: true
	},
	server: {
		port: port,
		strictPort: true,
		host: true,
		origin: `http://0.0.0.0:${port}`
	},
	resolve: {
		alias: {
			"@": path.resolve(__dirname, "./src")
		}
	},
});
