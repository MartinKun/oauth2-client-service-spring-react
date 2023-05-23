export interface SignUpRequestI {
    name: string;
    email: string;
    password: string;
  };

  export interface LoginRequestI {
    email: string;
    password: string;
  };

  export interface UserI {
    id: string,
    name: string;
    email: string;
    imageUrl: string;
    emailVerified: boolean;
    provider: string;
    providerId: string;
  };