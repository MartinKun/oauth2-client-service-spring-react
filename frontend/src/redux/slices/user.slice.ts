import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { UserI } from '../../interfaces/requestInterface';

interface State {
  authenticated: boolean;
  isLoading: boolean;
  loadingMessage: string | null;
  success: boolean;
  successMessage: string | null;
  error: boolean;
  errorMessage: string | null;
  currentUser: UserI | null;
}

interface Loading {
  isLoading: boolean;
  message: string | null;
}

const initialState: State = {
  authenticated: false,
  isLoading: false,
  loadingMessage: null,
  success: false,
  successMessage: null,
  error: false,
  errorMessage: null,
  currentUser: null
};

export const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    loading: (state, action: PayloadAction<Loading>) => {
      return {
        ...state,
        isLoading: action.payload.isLoading,
        loadingMessage: action.payload.message,
        success: false,
        successMessage: null,
        error: false,
        errorMessage: null
      };
    },
    loginSuccess: (state, action: PayloadAction<UserI>) => {
      return {
        ...state,
        authenticated: true,
        isLoading: false,
        loadingMessage: null,
        error: false,
        errorMessage: null,
        currentUser: action.payload
      };
    },
    genericSuccess: (state, action: PayloadAction<string>) => {
      return {
        ...state,
        authenticated: false,
        isLoading: false,
        loadingMessage: null,
        success: true,
        successMessage: action.payload,
        error: false,
        errorMessage: null,
        currentUser: null
      };
    },
    failure: (state, action: PayloadAction<string>) => {
      return {
        ...state,
        authenticated: false,
        isLoading: false,
        loadingMessage: null,
        success: false,
        successMessage: null,
        error: true,
        errorMessage: action.payload,
        currentUser: null
      };
    },
  },
});

export const { loginSuccess, loading, genericSuccess, failure } = userSlice.actions;

export default userSlice.reducer;