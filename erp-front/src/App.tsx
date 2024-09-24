import "@mantine/core/styles.css";
import { HeaderTabs } from "@/components/headers/HeaderTabs";
import Navbar from "@/components/navbar/Navbar";
import classes from "@/app/App.module.css";
import Scaffold from "./components/scaffold/Scaffold";

export default function App() {
	return (
		<div className={classes.container}>
			<header>
				<HeaderTabs />
			</header>
			<div className={classes.body}>
				<Navbar />
				<main className={classes.content}>
					<Scaffold />
				</main>
			</div>
		</div>
	);
}
