import { ACCESS_TOKEN, API_BASE_URL } from "../constants";
import { LoginRequestI, SignUpRequestI } from "../interfaces/requestInterface";

type Options = {
    url: string;
    method: string;
    body?: string;
}

const request = (options: Options, token?: string | null) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    })
    if (token) {
        headers.append('Authorization', 'Bearer ' + token)
    }

    const defaults = { headers: headers };
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
        .then(response =>
            response.json().then(json => {
                if (!response.ok) {
                    return Promise.reject(json);
                }
                return json;
            }).catch(error => {
                return error;
            })
        );
};

export function signup(signupRequest: SignUpRequestI) {
    return request({
        url: API_BASE_URL + "/api/v1/auth/register",
        method: 'POST',
        body: JSON.stringify(signupRequest)
    });
}

export function login(loginRequest: LoginRequestI) {
    return request({
        url: API_BASE_URL + "/api/v1/auth/authenticate",
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function getUser() {
    const token = localStorage.getItem(ACCESS_TOKEN);
    return request({
        url: API_BASE_URL + "/api/v1/user/me",
        method: 'GET'
    }, token);
}

export function activateUserAccount(token: string) {
    return request({
        url: API_BASE_URL + "/api/v1/auth/activate-account",
        method: 'POST'
    }, token);
}

export function refreshToken(token: string) {
    return request({
        url: API_BASE_URL + "/api/v1/auth/refresh-token",
        method: 'POST'
    }, token);
}

export function logoutUser(token: string) {
    return request({
        url: API_BASE_URL + "/api/v1/auth/logout",
        method: 'POST'
    }, token);
}