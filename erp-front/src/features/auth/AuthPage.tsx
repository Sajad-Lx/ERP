import {
	TextInput,
	PasswordInput,
	Checkbox,
	Anchor,
	Paper,
	Title,
	Text,
	Container,
	Group,
	Button,
	Stack
} from "@mantine/core";
import classes from "./AuthPage.module.css";
import { z } from "zod";
import { RootState } from "@/app/store";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { useForm, zodResolver } from "@mantine/form";
import { createUser, signIn } from "./authApi";
import { useAppDispatch, useAppSelector } from "@/app/hooks";
import { upperFirst, useToggle } from "@mantine/hooks";
import { appTitle } from "@/constants";
import { selectRememberMe, selectUser, toggleRememberMe } from "./authSlice";
import { Action, ThunkDispatch } from "@reduxjs/toolkit";
import { useEffect } from "react";

const signInSchema = z.object({
	email: z.string().email({ message: "Invalid email" }),
	password: z
		.string()
		.min(8, { message: "Password must be at least 6 characters, max 20 characters" })
		.max(20, { message: "Password must be at least 6 characters, max 20 characters" })
});

const registerSchema = z
	.object({
		email: z.string().email({ message: "Invalid email" }),
		password: z
			.string()
			.min(8, { message: "Password must be at least 6 characters, max 20 characters" })
			.max(20, { message: "Password must be at least 6 characters, max 20 characters" }),
		confirmPassword: z.string(),
		username: z.string().min(4).max(20),
		firstName: z.string().min(2).max(20),
		lastName: z.string().min(2).max(20)
	})
	.refine((data) => data.password === data.confirmPassword, {
		message: "Password do not match",
		path: ["confirmPassword"]
	});

export function AuthPage() {
	const navigate = useNavigate();
	const dispatch = useAppDispatch<ThunkDispatch<RootState, void, Action>>();
	const rememberMe = useAppSelector(selectRememberMe);
	const user = useAppSelector(selectUser);

	useEffect(() => {
		if(user) {
			navigate('/dashboard');
		}
	}, [user, navigate]);

	const { status, error } = useSelector((state: RootState) => state.auth);
	const [type, toggle] = useToggle(["login", "register"]);

	const form = useForm({
		mode: "uncontrolled",
		initialValues: {
			email: "",
			password: "",
			confirmPassword: "",
			username: "",
			firstName: "",
			lastName: ""
		},
		validate: type === "login" ? zodResolver(signInSchema) : zodResolver(registerSchema)
	});

	const handleSubmit = async (values: typeof form.values) => {
		if (type === "register") {
			dispatch(
				createUser({
					username: values.username,
					email: values.email,
					password: values.password,
					firstName: values.firstName,
					lastName: values.lastName
				})
			);
		} else {
			dispatch(
				signIn({
					email: values.email,
					password: values.password
				})
			);
		}
	};

	return (
		<Container size={420} my={40}>
			<Title ta="center" className={classes.title}>
				Welcome to {appTitle}
			</Title>
			{type === "register" ? (
				<Text c="dimmed" size="sm" ta="center" mt={5}>
					Already have an account?{" "}
					<Anchor size="sm" component="button" type="button" onClick={() => toggle()}>
						Login
					</Anchor>
				</Text>
			) : (
				<Text c="dimmed" size="sm" ta="center" mt={5}>
					Do not have an account yet?{" "}
					<Anchor size="sm" component="button" type="button" onClick={() => toggle()}>
						Create account
					</Anchor>
				</Text>
			)}

			<form onSubmit={form.onSubmit(handleSubmit)}>
				<Paper withBorder shadow="md" p={30} mt={30} radius="md">
					<Stack>
						<TextInput
							label="Email"
							placeholder="your-email@email.com"
							required
							radius="md"
							error={form.errors.email}
							{...form.getInputProps("email")}
						/>
						<PasswordInput
							label="Password"
							placeholder="Your password"
							required
							radius="md"
							error={form.errors.password}
							{...form.getInputProps("password")}
						/>
						{type === "register" && (
							<>
								<PasswordInput
									label="Re-enter Password"
									placeholder="Re-enter password"
									required
									radius="md"
									error={form.errors.confirmPassword}
									{...form.getInputProps("confirmPassword")}
								/>
								<TextInput
									label="Username"
									placeholder="Enter username"
									required
									radius="md"
									error={form.errors.username}
									{...form.getInputProps("username")}
								/>
								<TextInput
									label="First Name"
									placeholder="John"
									required
									radius="md"
									error={form.errors.firstName}
									{...form.getInputProps("firstName")}
								/>
								<TextInput
									label="Last Name"
									placeholder="Doe"
									required
									radius="md"
									error={form.errors.lastName}
									{...form.getInputProps("lastName")}
								/>
							</>
						)}

						<Group justify="space-between" mt="lg">
							<Checkbox
								label="Remember me"
								checked={rememberMe}
								onChange={() => dispatch(toggleRememberMe())}
							/>
							<Anchor component="button" size="sm">
								Forgot password?
							</Anchor>
						</Group>
					</Stack>

					{error && (
						<Text c="red" size="sm" mt="md">
							{error}
						</Text>
					)}

					<Button fullWidth mt="xl" type="submit" loading={status === "loading"}>
						{upperFirst(type)}
					</Button>
				</Paper>
			</form>
		</Container>
	);
}
