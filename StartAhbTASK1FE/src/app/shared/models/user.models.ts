export interface AddressClassRequestDto {
  street?: string;
  city?: string;
  state?: string;
  postalCode?: string;
  country?: string;
}

export interface AddressClassResponseDto {
  street?: string;
  city?: string;
  state?: string;
  postalCode?: string;
  country?: string;
}

export interface UserRequestDto {
  userFirstName: string;
  userLastName: string;
  userEmail: string;
  userPhoneNumber: string;
  userDateOfBirth: string; // ISO 8601 date format (YYYY-MM-DD)
  addressClass?: AddressClassRequestDto[];
  userLoginId: string;
  userPassword: string;
}

export interface UserResponseDto {
  userId: number;
  userFirstName: string;
  userLastName: string;
  userEmail: string;
  userPhoneNumber: string;
  userDateOfBirth: string;
  addressClass?: AddressClassResponseDto[];
}

export interface UpdateUserRequestDto {
  userFirstName: string;
  userLastName: string;
  userEmail: string;
  userPhoneNumber: string;
  userDateOfBirth: string;
  addressClass?: AddressClassRequestDto[];
}

export interface UpdateUserResponseDto {
  userId: number;
  userFirstName: string;
  userLastName: string;
  userEmail: string;
  userPhoneNumber: string;
  userDateOfBirth: string;
  addressClass?: AddressClassResponseDto[];
}
