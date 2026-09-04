import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface User {
  id: number;
  name: string;
  email: string;
}

/** Body sent when creating or updating a user - no `id`, the server owns it. */
export type UserInput = Omit<User, 'id'>;

/**
 * Thin HTTP client for the users API. One method per endpoint of UserController.
 * Requests go to `/api/...` and are forwarded to Spring Boot (:8090) by proxy.conf.json.
 */
@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly baseUrl = '/api/users';

  constructor(private readonly http: HttpClient) {}

  list(): Observable<User[]> {
    return this.http.get<User[]>(this.baseUrl);
  }

  get(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${id}`);
  }

  create(input: UserInput): Observable<User> {
    return this.http.post<User>(this.baseUrl, input);
  }

  update(id: number, input: UserInput): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/${id}`, input);
  }

  remove(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
