import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { jwtDecode } from 'jwt-decode';

interface LoginRequest {
  username?: string;
  userLoginId?: string;
  password: string;
}

interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
  tokenType: string;
  message: string;
}

interface JwtPayload {
  exp: number;
  iat?: number;
  sub?: string;
  [key: string]: any;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly API = 'http://localhost:8001/api/auth';
  private readonly accessTokenKey = 'access_token';
  private readonly refreshTokenKey = 'refresh_token';
  
  public isLoggedIn$ = new BehaviorSubject<boolean>(this.hasValidAccessToken());
  public currentUser$ = new BehaviorSubject<string | null>(this.getCurrentUsername());
  
  private refreshTimeoutId: any = null;
  private refreshInterval = 50000; // Refresh 10 seconds before expiry (60s - 10s)

  constructor(private http: HttpClient) {
    if (this.getAccessToken()) {
      this.scheduleRefresh();
    }
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    const body = {
      userLoginId: credentials.username || credentials.userLoginId,
      password: credentials.password
    };

    return this.http.post<AuthResponse>(`${this.API}/login`, body).pipe(
      tap(res => {
        this.setTokens(res.accessToken, res.refreshToken);
        this.isLoggedIn$.next(true);
        this.currentUser$.next(this.getCurrentUsername());
        this.scheduleRefresh();
      }),
      catchError(err => {
        return throwError(() => err);
      })
    );
  }

  signup(signupData: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API}/signup`, signupData).pipe(
      tap(res => {
        this.setTokens(res.accessToken, res.refreshToken);
        this.isLoggedIn$.next(true);
        this.currentUser$.next(this.getCurrentUsername());
        this.scheduleRefresh();
      }),
      catchError(err => {
        return throwError(() => err);
      })
    );
  }

  refreshToken(): Observable<AuthResponse> {
    const token = this.getAccessToken();
    if (!token) {
      return throwError(() => new Error('No access token'));
    }

    return this.http.post<AuthResponse>(
      `${this.API}/refresh`,
      {},
      {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }
    ).pipe(
      tap(res => {
        this.setTokens(res.accessToken, res.refreshToken);
        this.scheduleRefresh();
      }),
      catchError(err => {
        this.logout();
        return throwError(() => err);
      })
    );
  }

  logout(): void {
    this.clearTokens();
    this.isLoggedIn$.next(false);
    this.currentUser$.next(null);
    if (this.refreshTimeoutId) {
      clearTimeout(this.refreshTimeoutId);
      this.refreshTimeoutId = null;
    }
  }

  getAccessToken(): string | null {
    return localStorage.getItem(this.accessTokenKey);
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(this.refreshTokenKey);
  }

  private setTokens(accessToken: string, refreshToken: string): void {
    localStorage.setItem(this.accessTokenKey, accessToken);
    localStorage.setItem(this.refreshTokenKey, refreshToken);
  }

  private clearTokens(): void {
    localStorage.removeItem(this.accessTokenKey);
    localStorage.removeItem(this.refreshTokenKey);
  }

  private hasValidAccessToken(): boolean {
    const token = this.getAccessToken();
    if (!token) return false;

    try {
      const decoded = jwtDecode<JwtPayload>(token);
      const expiryTime = decoded.exp * 1000;
      return expiryTime > Date.now();
    } catch (error) {
      return false;
    }
  }

  private getCurrentUsername(): string | null {
    const token = this.getAccessToken();
    if (!token) return null;

    try {
      const decoded = jwtDecode<JwtPayload>(token);
      return decoded.sub || null;
    } catch (error) {
      return null;
    }
  }

  private scheduleRefresh(): void {
    if (this.refreshTimeoutId) {
      clearTimeout(this.refreshTimeoutId);
    }

    const token = this.getAccessToken();
    if (!token) return;

    try {
      const decoded = jwtDecode<JwtPayload>(token);
      const expiryTime = decoded.exp * 1000;
      const now = Date.now();
      const timeUntilRefresh = expiryTime - now - 10000; // Refresh 10 seconds before expiry

      if (timeUntilRefresh > 0) {
        this.refreshTimeoutId = setTimeout(() => {
          this.refreshToken().subscribe({
            next: () => console.log('Token refreshed successfully'),
            error: () => this.logout()
          });
        }, timeUntilRefresh);
      }
    } catch (error) {
      console.error('Error scheduling refresh:', error);
    }
  }

  isLoggedIn(): boolean {
    return this.hasValidAccessToken();
  }
}

