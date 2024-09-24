import { Action, combineSlices, configureStore, Store, ThunkAction } from "@reduxjs/toolkit";
import { setupListeners } from "@reduxjs/toolkit/query";
import { authSlice } from "@/features/auth/authSlice";
import { listenerMiddleware } from "./listenerMiddleware";

const rootReducer = combineSlices(authSlice);

const makeStore = (preloadedState?: Partial<RootState>) => {
	const store: Store = configureStore({
		reducer: rootReducer,

		middleware: (getDefaultMiddleware) => {
			return getDefaultMiddleware().prepend(listenerMiddleware.middleware).concat();
		},
		preloadedState
	});

	setupListeners(store.dispatch);
	return store;
};

export const store = makeStore();

export type AppStore = typeof store;

export type RootState = ReturnType<typeof rootReducer>;

export type AppDispatch = AppStore["dispatch"];

export type AppThunk<ThunkReturnType = void> = ThunkAction<
	ThunkReturnType,
	RootState,
	unknown,
	Action
>;
