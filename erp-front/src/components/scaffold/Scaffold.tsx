import { Outlet } from "react-router-dom";
import classes from "./Scaffold.module.css";

export default function Scaffold() {
	return (
		<div className={classes.content}>
			<Outlet />
		</div>
	);
}
