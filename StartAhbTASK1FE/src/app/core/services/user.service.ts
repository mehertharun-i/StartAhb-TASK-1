import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  UserRequestDto,
  UserResponseDto,
  UpdateUserRequestDto,
  UpdateUserResponseDto
} from '../../shared/models/user.models';

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly API = 'http://localhost:8001/user';

  constructor(private http: HttpClient) {}

  /**
   * Create a new user
   * POST /user/create
   * RequestBody: UserRequestDto
   * Response: UserResponseDto
   */
  createUser(userData: UserRequestDto): Observable<UserResponseDto> {
    return this.http.post<UserResponseDto>(`${this.API}/create`, userData);
  }

  /**
   * Get a user by ID
   * GET /user/{id}
   * PathVariable: id
   * Response: UserResponseDto
   */
  getUserById(userId: number): Observable<UserResponseDto> {
    return this.http.get<UserResponseDto>(`${this.API}/${userId}`);
  }

  /**
   * Get all users
   * GET /user/all
   * Response: List<UserResponseDto>
   */
  getAllUsers(): Observable<UserResponseDto[]> {
    return this.http.get<UserResponseDto[]>(`${this.API}/all`);
  }

  /**
   * Update a user by ID
   * PUT /user/update/{id}
   * PathVariable: id
   * RequestBody: UpdateUserRequestDto
   * Response: UpdateUserResponseDto
   */
  updateUser(userId: number, updateData: UpdateUserRequestDto): Observable<UpdateUserResponseDto> {
    return this.http.put<UpdateUserResponseDto>(`${this.API}/update/${userId}`, updateData);
  }

  /**
   * Delete a user by ID
   * DELETE /user/delete/{id}
   * PathVariable: id
   * Response: void (204 No Content)
   */
  deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/delete/${userId}`);
  }
}
