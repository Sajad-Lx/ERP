import cx from "clsx";
import { useState } from "react";
import {
	Container,
	Avatar,
	UnstyledButton,
	Group,
	Text,
	Menu,
	rem,
	useMantineTheme
} from "@mantine/core";
import {
	IconLogout,
	IconHeart,
	IconStar,
	IconMessage,
	IconSettings,
	IconSwitchHorizontal,
	IconChevronDown
} from "@tabler/icons-react";
import classes from "./HeaderTabs.module.css";
import { useAppDispatch, useAppSelector } from "@/app/hooks";
import { selectUser } from "@/features/auth/authSlice";
import { signOut } from "@/features/auth/authApi";
import { ThunkDispatch, Action } from "@reduxjs/toolkit";
import { RootState } from "@/app/store";

const userDemo = {
	name: "Jane Spoonfighter",
	email: "janspoon@fighter.dev",
	image: "https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/avatars/avatar-5.png"
};

// const tabs = [
// 	"Home",
// 	"Orders",
// 	"Education",
// 	"Community",
// 	"Forums",
// 	"Support",
// 	"Account",
// 	"Helpdesk"
// ];

export function HeaderTabs() {
	const dispatch = useAppDispatch<ThunkDispatch<RootState, void, Action>>();
	const user = useAppSelector(selectUser);

	const theme = useMantineTheme();
	const [userMenuOpened, setUserMenuOpened] = useState(false);

	const isLoggedIn = !!user;

	const onLogoutClicked = () => {
		dispatch(signOut());
	};

	return (
		<div className={classes.header}>
			<Container className={classes.mainSection} size="xl">
				<Group justify="space-between">
					<div>ERP</div>

					{isLoggedIn && (
						<Menu
							width={260}
							position="bottom-end"
							transitionProps={{ transition: "pop-top-right" }}
							onClose={() => setUserMenuOpened(false)}
							onOpen={() => setUserMenuOpened(true)}
							withinPortal
						>
							<Menu.Target>
								<UnstyledButton
									className={cx(classes.user, {
										[classes.userActive]: userMenuOpened
									})}
								>
									<Group gap={7}>
										<Avatar src={userDemo.image} alt={user.username} radius="xl" size={25} />
										<Text fw={500} size="sm" lh={1} mr={3}>
											{user.username}
										</Text>
										<IconChevronDown style={{ width: rem(12), height: rem(12) }} stroke={1.5} />
									</Group>
								</UnstyledButton>
							</Menu.Target>
							<Menu.Dropdown>
								<Menu.Item
									leftSection={
										<IconHeart
											style={{ width: rem(16), height: rem(16) }}
											color={theme.colors.red[6]}
											stroke={1.5}
										/>
									}
								>
									Liked posts
								</Menu.Item>
								<Menu.Item
									leftSection={
										<IconStar
											style={{ width: rem(16), height: rem(16) }}
											color={theme.colors.yellow[6]}
											stroke={1.5}
										/>
									}
								>
									Saved posts
								</Menu.Item>
								<Menu.Item
									leftSection={
										<IconMessage
											style={{ width: rem(16), height: rem(16) }}
											color={theme.colors.blue[6]}
											stroke={1.5}
										/>
									}
								>
									Your comments
								</Menu.Item>

								<Menu.Label>Settings</Menu.Label>
								<Menu.Item
									leftSection={
										<IconSettings style={{ width: rem(16), height: rem(16) }} stroke={1.5} />
									}
								>
									Account settings
								</Menu.Item>
								<Menu.Item
									leftSection={
										<IconSwitchHorizontal
											style={{ width: rem(16), height: rem(16) }}
											stroke={1.5}
										/>
									}
								>
									Change account
								</Menu.Item>
								<Menu.Item
									leftSection={
										<IconLogout style={{ width: rem(16), height: rem(16) }} stroke={1.5} />
									}
									onClick={onLogoutClicked}
								>
									Logout
								</Menu.Item>
							</Menu.Dropdown>
						</Menu>
					)}
				</Group>
			</Container>
		</div>
	);
}
