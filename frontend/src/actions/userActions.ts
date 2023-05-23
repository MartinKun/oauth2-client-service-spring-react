import { LoginRequestI, SignUpRequestI } from "../interfaces/requestInterface";
import { signup, login, getUser, activateUserAccount, refreshToken, logoutUser } from "../utils/APIUtils";
import { NavigateFunction } from 'react-router-dom';
import { ACCESS_TOKEN, REFRESH_TOKEN } from "../constants";
import { genericSuccess, failure, loading, loginSuccess } from "../redux/slices/user.slice";
import { AppDispatch } from "../redux/store";

const signUserUp = (signUpRequest: SignUpRequestI,
    navigate: NavigateFunction) => async (dispatch: AppDispatch) => {
        try {
            dispatch(loading({ message: "Loading", isLoading: true }))

            await signup(signUpRequest)

            dispatch(genericSuccess("We've sent a confirmation to your e-mail for verification."))

            navigate("/login");
        } catch (e) {
            if (!(e instanceof Error))
                throw e;

            dispatch(failure(e?.message || ""));
        }
    }

const loginUser = (loginRequest: LoginRequestI,
    navigate: NavigateFunction) => async (dispatch: AppDispatch) => {
        try {
            dispatch(loading({ message: "Loading", isLoading: true }))
            const res = await login(loginRequest)

            localStorage.setItem(ACCESS_TOKEN, res.accessToken);
            localStorage.setItem(REFRESH_TOKEN, res.refreshToken);

            const user = await getUser();

            dispatch(loginSuccess(user))

            navigate("/user/profile");
        } catch (e) {
            if (!(e instanceof Error))
                throw e;

            dispatch(failure(e?.message || ""))
        }
    }

const loadCurrentUser = () => async (dispatch: AppDispatch) => {
    try {
        dispatch(loading({ message: "Loading profile", isLoading: true }))

        if (!localStorage.getItem(ACCESS_TOKEN)) {
            dispatch(loading({ message: null, isLoading: false }))
            return;
        }

        const res = await getUser();

        if (res.status === 401) {

            const token = localStorage.getItem(REFRESH_TOKEN);

            if (!token) {
                dispatch(loading({ message: null, isLoading: false }))
                return;
            }

            const res = await refreshToken(token);
            if (res.status === 400) {
                dispatch(loading({ message: null, isLoading: false }))
                return;
            }

            localStorage.setItem(ACCESS_TOKEN, res.accessToken);
            localStorage.setItem(REFRESH_TOKEN, res.refreshToken);
        }

        const user = await getUser();

        dispatch(loginSuccess(user));

    } catch (e) {
        if (!(e instanceof Error))
            throw e;

        dispatch(failure(e?.message || ""))
    }
}

const activateAccount = (token: string) => async (dispatch: AppDispatch) => {
    try {
        dispatch(loading({ message: "Activating account", isLoading: true }))

        const res = await activateUserAccount(token);

        if (res.status === 200) {
            dispatch(genericSuccess("The account has been successfully activated"))
        } else {
            dispatch(failure(res.errors.error))
        }

    } catch (e) {
        if (!(e instanceof Error))
            throw e;

        dispatch(failure(e?.message || ""))
    }
}

const logout = () => async (dispatch: AppDispatch) => {
    try {
        dispatch(loading({ message: "Logging out", isLoading: true }))
        const refreshToken = localStorage.getItem(REFRESH_TOKEN);

        if (refreshToken != null) {
            await logoutUser(refreshToken)
        }

        localStorage.removeItem(ACCESS_TOKEN);
        localStorage.removeItem(REFRESH_TOKEN);

        window.location.reload();

    } catch (e) {
        if (!(e instanceof Error))
            throw e;

        dispatch(failure(e?.message || ""))
    }
}

export { signUserUp, loginUser, loadCurrentUser, activateAccount, logout }