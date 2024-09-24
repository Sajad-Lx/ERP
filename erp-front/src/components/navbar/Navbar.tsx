import { Burger, Drawer, ScrollArea, Tooltip, rem } from "@mantine/core";
import classes from "./Navbar.module.css";
import menuItems from "@/app/navbarItems";
import { NavLink } from "react-router-dom";
import { useState } from "react";
import { useDisclosure } from "@mantine/hooks";

export default function Navbar() {
	const [activeMainLink, setActiveMainLink] = useState(0);
	const [drawerOpened, { toggle: toggleDrawer, close: closeDrawer }] = useDisclosure(false);

	const mainLinks = menuItems.map((link, index) => (
		<Tooltip
			label={link.label}
			position="right"
			withArrow
			transitionProps={{ duration: 0 }}
			key={index}
		>
			<NavLink
				to={link.to}
				className={({ isActive }) => `${classes.mainLink} ${isActive ? classes.active : ""}`}
				onClick={() => setActiveMainLink(index)}
			>
				<link.icon style={{ width: rem(22), height: rem(22) }} stroke={1.5} />
			</NavLink>
		</Tooltip>
	));

	const subLinks = menuItems[activeMainLink].subMenu?.map((link, index) => (
		<NavLink
			className={({ isActive }) => `${classes.link} ${isActive ? classes.active : ""}`}
			to={link.to}
			key={index}
		>
			{link.label}
		</NavLink>
	));

	return (
		<nav className={classes.navbar}>
			<div className={classes.wrapper}>
				<div className={classes.aside}>
					{/* Main Links */}
					<Burger
						opened={drawerOpened}
						onClick={toggleDrawer}
						lineSize={2}
						size={"sm"}
						hiddenFrom="md"
						style={{ paddingTop: 20, paddingBottom: 30 }}
					/>
					{mainLinks}
				</div>

				{/* Sub Links */}
				<Drawer
					opened={drawerOpened}
					onClose={closeDrawer}
					offset={65}
					radius={"md"}
					size={"md"}
					className={classes.drawer}
					// scrollAreaComponent={ScrollArea.Autosize}
					title={menuItems[activeMainLink].label}
					withCloseButton={false}
					hiddenFrom="md"
					withOverlay={false}
				>
					<div>{subLinks && <div className={classes.subDrawer}>{subLinks}</div>}</div>
				</Drawer>
				{subLinks && <div className={classes.subMenu}>{subLinks}</div>}
			</div>
		</nav>
	);
}
